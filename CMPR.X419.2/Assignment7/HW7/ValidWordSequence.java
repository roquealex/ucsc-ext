import java.util.Optional;
import java.util.function.BiFunction;

public class ValidWordSequence {
  // Comparisson method
  public static boolean isValidRec(String s, Dictionary d) {
    if (d.contains(s)) return true;
    for (int i = 1 ; i < s.length() ; i++) {
      String pre = s.substring(0,i);
      //System.out.println(pre + " - " + s.substring(i));
      if (d.contains(pre) ) {
        String post = s.substring(i);
        if (isValidRec(post,d)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public static boolean isValid(String s, Dictionary d) {
    Boolean results[][] = new Boolean[s.length()][s.length()];
    return isValidDyn(s, d, 0, s.length(),results);
  }

  // beginIdx inclusive
  // endIdx exclusive
  private static boolean isValidDyn(String s, Dictionary d, int beginIdx, int endIdx, Boolean[][] results) {
    if (results[beginIdx][endIdx-1]!=null) {
      return (results[beginIdx][endIdx-1].booleanValue());
    } else {
      String sub = s.substring(beginIdx,endIdx);
      if (d.contains(sub)) return setAndReturn(true,beginIdx,endIdx,results);
      for (int i = beginIdx+1 ; i < endIdx ; i++) {
        String pre = s.substring(beginIdx,i);
        String post = s.substring(i,endIdx);
        //System.out.println(pre + " - " + post);
        if (d.contains(pre) ) {
          if (isValidDyn(s,d,i,endIdx,results)) {
            return setAndReturn(true,beginIdx,endIdx,results);
          }
        }
      }
    }
    //return false;
    return setAndReturn(false,beginIdx,endIdx,results);
  }
  private static boolean setAndReturn(boolean result, int beginIdx, int endIdx, Boolean[][] results) {
    assert(results[beginIdx][endIdx-1]==null);
    //assert(false);
    results[beginIdx][endIdx-1] = new Boolean(result);
    return result;
  }
  
  public static boolean isValidSquare(String s, Dictionary d) {
    int len = s.length();
    boolean results[] = new boolean[len];
    for (int i = (len-1) ; i >= 0 ; i--) {
      String sub = s.substring(i);
      //System.out.println(sub);
      if (d.contains(sub)) {
        results[i] = true;
      } else {
        for (int j = i+1 ; j < len ; j++) {
          if (results[j]) {
            String subsub = s.substring(i,j);
            //System.out.println(subsub);
            if (d.contains(subsub)) {
              results[i] = true;
              break;
            }
          }
        }
      }
    }
    return results[0];
  }

  public static class PerfInfo {
    public PerfInfo(String name) {
      this.name = name;
    }
    public String toCsv() {
      return String.format("%s,%d,%d,%s",name,size,time,result?"TRUE":"FALSE");
    }
    public String name;
    public int size;
    public long time;
    public boolean result;
  }

  public static boolean isValidProfiler(String s, Dictionary d,
      BiFunction<String,Dictionary,Boolean> f) {
    return isValidProfiler(s, d, f,null);
  }
  public static boolean isValidProfiler(String s, Dictionary d,
      BiFunction<String,Dictionary,Boolean> f, PerfInfo perf) {
    long start = System.nanoTime();
    boolean result = f.apply(s,d);
    long finish = System.nanoTime();
    long timeElapsed = finish - start;
    if (perf==null) {
      System.out.println("Time elapsed "+timeElapsed);
    } else {
      perf.size = s.length();
      perf.time = timeElapsed;
      perf.result = result;
    }
    return result;
  }

  public static void main(String s[]) {
    int seed = 1;
    FileDictionary dic = new FileDictionary("words.txt");
    dic.show();
    WordRandomizer wr = new WordRandomizer(dic,seed);

    //System.out.println(wr.createRandomValid(10));
    //System.out.println(wr.createRandomWord(10));
    //System.out.println(wr.createRandomMix(10,10));

    String str = wr.createRandomValid(1000);
    //String str = wr.createRandomMix(1000,10);

    System.out.println(str);
    System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValidRec));
    System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValid));
    System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValidSquare));
    //System.out.println(isValidSquare(str,dic));

    /*
    boolean b = isValidRec(
    "catdogbirdfishwaterwindfiresnowsunmoonlightdarkphoneguitarrazorbookskateshoe",
    dic);

    boolean b = isValidRec(
    "moonlighteverlastingxdjdsfalsladhalhdglahdlgjahfdlkjghafldjkghalfjkhgakdfjh",
    dic);
    System.out.println(b);
    System.out.println(b);

    //String str = "snowbunnysun";
    String str = 
    //"moonlighteverlastingxdjdsfalsladhalhdglahdlgjahfdlkjghafldjkghalfjkhgakdfjh";
    "catdogbirdfishwaterwindfiresnowsunmoonlightdarkphoneguitarrazorbookskateshoe";

    b = isValidRec(str,dic);
    b = isValid(str, dic);
    b = isValidSquare(str, dic);
    System.out.println(b);
    */
  }
}

/*
s - nowbunnysun
sn - owbunnysun
sno - wbunnysun
snow - bunnysun
b - unnysun
bu - nnysun
bun - nysun
bunn - ysun
bunny - sun
bunnys - un
bunnysu - n
snowb - unnysun
snowbu - nnysun
snowbun - nysun
snowbunn - ysun
snowbunny - sun
snowbunnys - un
snowbunnysu - n
*/
