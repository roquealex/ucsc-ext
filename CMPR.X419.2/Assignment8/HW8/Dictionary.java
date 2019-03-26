// Dictionary.java
import java.util.Iterator;

public interface Dictionary {
  public boolean contains(String word);
  public void show();
  public int size();
  public Iterator<String> iterator();
}
