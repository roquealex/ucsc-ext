import java.util.Optional;
import java.util.function.BiFunction;
import java.util.ArrayList;

public class ValidWordSequenceTest {

  public static class PerfInfo {
    public PerfInfo(String name) {
      this.name = name;
    }
    public String toCsv() {
      return String.format("%s,%d,%d,%d,%s",name,wordSize,setSize,time,result?"TRUE":"FALSE");
    }
    public String name;
    public int wordSize;
    public int setSize;
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
      perf.wordSize = s.length();
      perf.wordSize = s.length();
      perf.time = timeElapsed;
      perf.result = result;
    }
    return result;
  }

  public static void main(String s[]) {
    int seed = 1;
    Dictionary dic = new FileDictionary("English3000.txt");
    dic.show();
    WordRandomizer wr = new WordRandomizer(dic,seed);

    //System.out.println(wr.createRandomValid(10));
    //System.out.println(wr.createRandomWord(10));
    //System.out.println(wr.createRandomMix(10,10));

    String str = wr.createRandomValid(1000);
    //String str = wr.createRandomMix(3500,10);

    System.out.println(str);

    ArrayList<BiFunction<String,Dictionary,Boolean>> funcs = new ArrayList<>();
    funcs.add(ValidWordSequence::isValidRec);
    funcs.add(ValidWordSequence::isValid);
    funcs.add(ValidWordSequence::isValidSquare);
    for(BiFunction<String,Dictionary,Boolean> func : funcs) {
      System.out.println(isValidProfiler(str,dic,func));
    }
    //System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValidRec));
    //System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValid));
    //System.out.println(isValidProfiler(str,dic,ValidWordSequence::isValidSquare));
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

