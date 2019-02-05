import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Reporter {

  public static void help() {
    System.err.println("Usage:");
    System.err.println("  java Reporter column threshold n");
    System.err.println("");
    System.err.println("params:");
    System.err.println("  column - The number of column starting from 0");
    System.err.println("  threshold - number used in expression value > threshold");
    System.err.println("  n - Consecutive times that we want the previous expression to be true");
    System.err.println("");
    System.err.println("Note : Program will not echo stdin if piped");
    System.exit(1);
  }

  public static void checkParams(String[] params) {
    if (params.length != 3) {
      System.err.println("ERROR: program recieves exactly 3 parameters");
      help();
    }
  }

  public static int parseIntParam(String param, String name) {
    int num=0;
    try {
      num = Integer.parseInt(param);
      return num;
    } catch (NumberFormatException e) {
      System.err.println(String.format("ERROR: Parameter %s has to be integer: %s",name,param));
      help();
    }
    return num;
  }

  public static void main(String[] args) throws IOException {
    InputStreamReader is = new InputStreamReader(System.in);
    BufferedReader bf = new BufferedReader(is);
    checkParams(args);
    int column = parseIntParam(args[0],"column");
    int threshold = parseIntParam(args[1],"threshold");
    int n = parseIntParam(args[2],"n");

    int graceLineNum = 2;
    // Counters
    int lineNum = 1;
    int consecutive = 0;
    System.out.println("Buffered test");
    String s;
    while((s = bf.readLine())!=null) {
      try {
        String colStr = s.split("\\s+")[column];
        int num = Integer.parseInt(colStr);
        //System.out.println("Int -"+num);
        if (num > threshold) {
          consecutive++;
          if (consecutive == n) {
            System.out.println(
                String.format("Column %s went above the threshold %d %d consecutive times at line number %d",
                  column,threshold,n,lineNum));
          }
        } else {
          consecutive = 0;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        // There is a grace period for the first 2 lines which could be headers
        if (lineNum > graceLineNum ) {
          System.err.println(String.format("ERROR: Column of interest %d is out of the valid range",column));
          help();
        }
      } catch (NumberFormatException e) {
        if (lineNum > graceLineNum ) {
          System.err.println(String.format("ERROR: Column of interest %d has a value that is not an integer: %s",column,s.split("\\s+")[column]));
          help();
        }
      }
      lineNum++;
    }

  }
}
