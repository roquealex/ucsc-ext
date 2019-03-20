// ValidWordSequence.java
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.ArrayList;
import java.util.LinkedList;

public class ValidWordSequence {
  // Comparisson method recursive
  public static boolean isValidRec(String s, Dictionary d) {
    if (s==null || s.length()==0) return false;
    if (d.contains(s)) return true;
    for (int i = 1 ; i < s.length() ; i++) {
      String pre = s.substring(0,i);
      if (d.contains(pre) ) {
        String post = s.substring(i);
        if (isValidRec(post,d)) {
          return true;
        }
      }
    }
    return false;
  }

  // Dynamic: Saves in a bidimentional array with the matches
  public static boolean isValid(String s, Dictionary d) {
    if (s==null || s.length()==0) return false;
    Boolean results[][] = new Boolean[s.length()][s.length()];
    return isValidDyn(s, d, 0, s.length(),results);
  }

  // beginIdx inclusive
  // endIdx exclusive
  private static boolean isValidDyn(String s, Dictionary d, int beginIdx, int endIdx, Boolean[][] results) {
    //String sub = s.substring(beginIdx,endIdx);
    //if (d.contains(sub)) return true;
    if (substringIsInDictionary(s, d, beginIdx, endIdx, results)) return true;
    for (int i = beginIdx+1 ; i < endIdx ; i++) {
      //String pre = s.substring(beginIdx,i);
      //if (d.contains(pre) ) {
      if (substringIsInDictionary(s, d, beginIdx, i, results)) {
        if (isValidDyn(s,d,i,endIdx,results)) {
          return true;
        }
      }
    }
    return false;
  }

  // beginIdx inclusive
  // endIdx exclusive
  private static boolean substringIsInDictionary(String s, Dictionary d, int beginIdx, int endIdx, Boolean[][] results) {
    if (results[beginIdx][endIdx-1]==null) {
      String sub = s.substring(beginIdx,endIdx);
      boolean result = d.contains(sub);
      results[beginIdx][endIdx-1] = new Boolean(result);
      return result;
    } else {
      boolean result = results[beginIdx][endIdx-1].booleanValue();
      return result;
    }
  }

  // Memorizes in a linear space but cuadratic time
  public static boolean isValidSquare(String s, Dictionary d) {
    if (s==null || s.length()==0) return false;
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

}

