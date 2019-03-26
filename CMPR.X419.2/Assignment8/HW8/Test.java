import java.util.HashSet;

public class Test {

  public static
  boolean isThereValidPath(Dictionary validWords, String startWord, String endWord) {
    if(startWord.length() != endWord.length()) return false;
    HashSet<String> visited = new HashSet<>();
    return isThereValidPathDFS(validWords, startWord, endWord, visited);
    //return false;
  }

  public static
  boolean isThereValidPathDFS(Dictionary validWords, String src, String dst, HashSet<String> visited) {
    // If we visited already don't search again
    if (visited.contains(src)) return false;
    // If any word is not in the dictionary, there is no path
    if(!validWords.contains(src) || !validWords.contains(dst)) return false;

    visited.add(src);
    if(src.equals(dst)) {
      System.out.println(src);
      return true;
    }

    for (int pos = 0 ; pos < src.length() ; pos++) {
      char orig = src.charAt(pos);
      for (char c = 'a' ; c <= 'z' ; c++) {
        if (c != orig) {
          String str = replaceCharacter(src, pos, c);
          boolean result = isThereValidPathDFS(validWords, str, dst, visited);
          if (result) {
            System.out.println(src);
            return true;
          }
          //System.out.println(str);
        }
      }
    }
    return false;

  }

  public static
  String replaceCharacter(String orig, int pos, char c) {
    StringBuffer buff = new StringBuffer();
    if (pos!=0) {
      buff.append(orig.substring(0,pos));
    }
    buff.append(c);
    if (pos!=orig.length()-1) {
      buff.append(orig.substring(pos+1));
    }
    //System.out.println(buff.toString());
    return buff.toString();
  }

  public static void main(String s[]) {
    System.out.println("Hello");
    String test = "cat";
    Dictionary dict = new FileDictionary("English3000.txt");
    // False:
    //boolean res = isThereValidPath(dict, "cat", "pig"); // it should reach from pie
    //boolean res = isThereValidPath(dict, "cat", "dog"); // good
    boolean res = isThereValidPath(dict, "pepper", "people");
    System.out.println("The result was: "+res);
    /*
    for (int pos = 0 ; pos < test.length() ; pos++) {
      char orig = test.charAt(pos);
      for (char c = 'a' ; c <= 'z' ; c++) {
        if (c != orig) {
          String str = replaceCharacter(test, pos, c);
          System.out.println(str);
        }
      }
      //String str = replaceCharacter(test, pos, 'X');
      //System.out.println(str);
    }
    */
  }
}


