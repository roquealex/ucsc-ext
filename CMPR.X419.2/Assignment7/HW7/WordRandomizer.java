// WordRandomizer.java
import java.util.Iterator;
import java.util.Random;
import java.util.Optional;
import java.util.stream.*;

public class WordRandomizer {

  String[] validWords;
  Random rnd;

  public WordRandomizer(Dictionary dic,int seed) {
    int size = dic.size();
    validWords = new String[size];
    Iterator<String> it = dic.iterator();
    int i = 0 ;
    while(it.hasNext()) {
      String nextStr = it.next();
      validWords[i] = nextStr;
      i++;
    }
    rnd = new Random(seed);
  }

  public String createRandomValid(int count) {
    Optional<StringBuffer> sb = rnd.ints(count, 0, validWords.length)
      .mapToObj(x -> new StringBuffer(validWords[x]))
      .reduce((x,y) -> x.append(y));
    return sb.get().toString();
  }

  public String createRandomWord(int maxSize) {
    int count = rnd.nextInt(maxSize)+1;
    String str =
      rnd.ints(count,Character.digit('a',36),Character.digit('z',36)+1)
      .mapToObj(x -> Character.forDigit(x,36))
      .reduce(new StringBuffer(),(x,y) -> x.append(y),(x,y) -> x.append(y))
      .toString();
    return str;
  }

  public String createRandomMix(int count, int maxSize) {
    StringBuffer sb =
      rnd.ints(count, -1*validWords.length, validWords.length)
      .mapToObj(x -> (x>=0)?validWords[x]:createRandomWord(maxSize))
      .reduce(new StringBuffer(),(x,y) -> x.append(y),(x,y) -> x.append(y));
    return sb.toString();
  }

  public void show() {
    for (String s : validWords) {
      System.out.println(s);
    }
  }

}
