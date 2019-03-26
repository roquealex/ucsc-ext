import java.util.HashSet;

public class ValidPathDFS implements ValidPath {

  public boolean isThereValidPath(Dictionary validWords, String startWord, String endWord) {
    if(startWord.length() != endWord.length()) return false;
    HashSet<String> visited = new HashSet<>();
    // Inverting source and destination so we get the right order
    return isThereValidPathDFS(validWords, endWord, startWord, visited);
    //return false;
  }

  public boolean isThereValidPathDFS(Dictionary validWords, String src, String dst, HashSet<String> visited) {
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

  public String replaceCharacter(String orig, int pos, char c) {
    StringBuffer buff = new StringBuffer();
    if (pos!=0) {
      buff.append(orig.substring(0,pos));
    }
    buff.append(c);
    if (pos!=orig.length()-1) {
      buff.append(orig.substring(pos+1));
    }
    return buff.toString();
  }

}


