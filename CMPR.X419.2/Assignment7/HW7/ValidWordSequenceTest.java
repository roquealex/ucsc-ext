// ValidWordSequenceTest.java
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.function.BiFunction;
import java.util.ArrayList;
import java.io.*;

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

  public static class FuncInfo{
    public FuncInfo(String name, BiFunction<String,Dictionary,Boolean> func) {
      this.func = func;
      info = new PerfInfo(name);
    }
    public BiFunction<String,Dictionary,Boolean> func;
    public PerfInfo info;
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
      perf.setSize = d.size();
      perf.time = timeElapsed;
      perf.result = result;
    }
    return result;
  }

  private static PrintWriter out;
  private static Dictionary dic;
  private static WordRandomizer wr;
  private static ArrayList<FuncInfo> funcList;

  public static final int TOTAL = 200;
  public static final int REP = 10;

  @BeforeClass
  public static void setupTests() throws IOException {
    int seed = 1;
    out = new PrintWriter(new BufferedWriter(new FileWriter("out.csv")));
    out.println("name,wordSize,setSize,time,result");
    dic = new FileDictionary("English3000.txt");
    //dic.show();
    wr = new WordRandomizer(dic,seed);
    funcList = new ArrayList<>();
    funcList.add(new FuncInfo("isValidRec",ValidWordSequence::isValidRec));
    funcList.add(new FuncInfo("isValid",ValidWordSequence::isValid));
    funcList.add(new FuncInfo("isValidSquare",ValidWordSequence::isValidSquare));
  }
  
  @Test
  public void testEmpty() {
    System.out.println("Test Empty");
    String testStr = null;
    for(FuncInfo func : funcList) {
      boolean result = isValidProfiler(testStr,dic,func.func,null);
      System.out.println(result);
      assertFalse(result);
    }
    testStr = "";
    for(FuncInfo func : funcList) {
      boolean result = isValidProfiler(testStr,dic,func.func,null);
      System.out.println(result);
      assertFalse(result);
    }
  }

  @Test
  public void testValid() {
    for (int i = 1 ; i <= TOTAL ; i++) {
      for (int j = 0 ; j < REP ; j++) {
        String testStr = wr.createRandomValid(i);
        System.out.printf("%d words\n",i);
        for(FuncInfo func : funcList) {
          boolean result = isValidProfiler(testStr,dic,func.func,func.info);
          System.out.println(result);
          assertTrue(result);
          out.println(func.info.toCsv());
        }
      }
    }
  }

  @Test
  public void testRandom() {
    for (int i = 1 ; i <= TOTAL ; i++) {
      for (int j = 0 ; j < REP ; j++) {
        String testStr = wr.createRandomMix(i,10);
        System.out.printf("%d words\n",i);
        boolean first = true;
        boolean prevResult = false;
        for(FuncInfo func : funcList) {
          boolean result = isValidProfiler(testStr,dic,func.func,func.info);
          System.out.println(result);
          out.println(func.info.toCsv());
          if (!first) {
            assertEquals(prevResult,result);
          } else {
            first = false;
          }
          prevResult = result;
        }
      }
    }
  }

  @AfterClass
  public static void cleanupTests() /*throws IOException*/ {
    out.close();
  }
 
  /*
  public static void main(String s[]) throws Exception {

    ValidWordSequenceTest test = new ValidWordSequenceTest();
    test.setupTests();
    test.testEmpty();
    test.testValid();
    test.testRandom();
    test.cleanupTests();

  }
  */

}

