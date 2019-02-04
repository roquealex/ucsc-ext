import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Reporter {
  public static void main(String[] args) throws IOException {
    InputStreamReader is = new InputStreamReader(System.in);
    BufferedReader bf = new BufferedReader(is);
    int column = 2;
    int threshold = 370;
    int n = 2;
    int graceLineNum = 2;
    // Counters
    int lineNum = 1;
    int consecutive = 0;
    System.out.println("Buffered test");
    String s;
    while((s = bf.readLine())!=null) {
      //System.out.println("Line: "+s);
      //System.out.println("Line Read: "+s);
      try {
        String colStr = s.split("\\s+")[column];
        //System.out.println("String -"+colStr);
        int num = Integer.parseInt(colStr);
        System.out.println("Int -"+num);
        if (num > threshold) {
          consecutive++;
          if (consecutive == n) {
            System.out.println(
                String.format("Column %s went above the threshold %d %d times",
                  column,threshold,n));
          }
        } else {
          consecutive = 0;
        }
      } catch (ArrayIndexOutOfBoundsException e) {
        // There is a grace period for the first 2 lines which could be headers
        if (lineNum > graceLineNum ) {
          System.err.println(String.format("Column of interest %d is out of the valid range",column));
          return;
        }
      } catch (NumberFormatException e) {
        if (lineNum > graceLineNum ) {
          System.err.println(String.format("Column of interest %d has a value that is not an integer: %s",column,s.split("\\s+")[column]));
          return;
        }
      }
      lineNum++;
    }

  }
}
