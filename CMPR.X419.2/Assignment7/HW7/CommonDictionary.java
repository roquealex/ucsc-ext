import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CommonDictionary {
  private HashSet<String> dictionary;
  private static final String inputFile = "words.txt.gz";

  public CommonDictionary() {
    dictionary = new HashSet<>(10000);
    try {
      InputStream gzipStream = new GZIPInputStream(new FileInputStream(inputFile));
      BufferedReader reader = new BufferedReader(new InputStreamReader(gzipStream,"UTF-8"));

      String line = null;
      while ((line = reader.readLine()) != null) {
        dictionary.add(line.toLowerCase());
      }
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  public boolean contains(String word) {
    return dictionary.contains(word);
  }

  public void show() {
    Iterator<String> it = dictionary.iterator();
    while (it.hasNext()) {
      String str = it.next();
      System.out.println(str);
    }
  }

  public static void main(String s[]) {
    CommonDictionary dic = new CommonDictionary();
    dic.show();
    System.out.println(dic.contains("cat"));
    System.out.println(dic.contains("dog"));
    System.out.println(dic.contains("Dog"));
    System.out.println(dic.contains("bark"));
  }
}
