// FileDictionary.java
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDictionary implements Dictionary {
  private HashSet<String> dictionary;
  private static final String inputFile = "words.txt.gz";

  public FileDictionary(String filename) {
    dictionary = new HashSet<>(10000);
    int extIdx = filename.lastIndexOf('.');
    boolean gzip = false;
    if ( extIdx >= 0) {
      String ext = filename.substring(extIdx);
      gzip = ext.equals(".gz");
      //System.out.println("Found compress "+gzip);
    }
    try {
      InputStream istream = new FileInputStream(filename);
      if (gzip) {
        istream = new GZIPInputStream(istream);
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(istream,"UTF-8"));

      String line = null;
      while ((line = reader.readLine()) != null) {
        dictionary.add(line.toLowerCase());
      }
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  @Override
  public boolean contains(String word) {
    return dictionary.contains(word);
  }

  @Override
  public void show() {
    Iterator<String> it = dictionary.iterator();
    while (it.hasNext()) {
      String str = it.next();
      System.out.println(str);
    }
  }

  @Override
  public int size() {
    return dictionary.size();
  }

  @Override
  public Iterator<String> iterator() {
    return dictionary.iterator();
  }

}
