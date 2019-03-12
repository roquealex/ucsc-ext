import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Random;

public class Fortune {

  public static String END_OF_LINE = System.getProperty("line.separator");
  public static Pattern EMPTY_LINE = Pattern.compile("^\\s*$");

  public static void main(String[] s) {
    //String filename = "test.txt";
    String filename = "riddles";
    //String filename = "out.txt";
    ArrayList<String> quotes= new ArrayList<>();
    try {
      BufferedReader in
        = new BufferedReader(new FileReader(filename));
      StringBuilder buff = new StringBuilder();
      String line;
      boolean empty = true;
      while( (line = in.readLine()) != null ) {
        if (line.equals("%")) {
          if (!empty) {
            quotes.add(buff.toString());
          } else {
            quotes.add(END_OF_LINE);
          }
          buff.setLength(0);
          empty = true;
        } else {
          if (!EMPTY_LINE.matcher(line).matches()) empty = false;
          buff.append(line);
          buff.append(END_OF_LINE);
        }
        //System.out.println(line);
      }
      //System.out.print(buff.toString());
    } catch (FileNotFoundException e) {
      System.err.printf("Filename %s does not exist\n",filename);
      System.exit(1);
    } catch (IOException e) {
      System.exit(2);
    }
    int size = quotes.size();
    if (size==0) {
      System.err.printf("Filename %s had no quotes\n",filename);
      System.exit(3);
    }
    Random rand = new Random(System.currentTimeMillis());
    // Select at random one:
    //System.out.println(size);
    int selected = rand.nextInt(size);
    //System.out.println(selected);
    System.out.print(quotes.get(selected));
  }
}

