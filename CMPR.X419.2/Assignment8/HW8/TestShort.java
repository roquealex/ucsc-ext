//import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;

import java.util.PriorityQueue;
public class TestShort {

  public static class WordDistance implements Comparable<WordDistance> {
    public int distance;
    public String word;

    public WordDistance(String word, int distance) {
      this.word = word;
      this.distance = distance;
    }

    public String toString() {
      StringBuffer buff = new StringBuffer("(");
      buff.append(word);
      buff.append(',');
      buff.append(distance);
      buff.append(')');
      return buff.toString();
    }

    @Override
    public int compareTo(WordDistance o) {
      if (this.distance < o.distance) return -1;
      else if (this.distance > o.distance) return 1;
      else return 0;
    }
  }

  /*
  public static
  boolean isThereValidPath(Dictionary validWords, String startWord, String endWord) {
    if(startWord.length() != endWord.length()) return false;
    HashSet<String> visited = new HashSet<>();
    // Inverting source and destination so we get
    return isThereValidPathDFS(validWords, startWord, endWord, visited);
    //return false;
  }

  public static
  boolean isThereValidPathDFS(Dictionary validWords, String src, String dst, HashSet<String> visited) {
    // If we visited already don't search again
    if (visited.contains(src)) return false;
    // If any word is not in the validWordsionary, there is no path
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
  */

  public static void main(String s[]) {
    System.out.println("Hello");
    String test = "cat";
    Dictionary validWords = new FileDictionary("English3000.txt");
    ValidPath validPathDFS = new ValidPathDFS();
    // False:
    //boolean res = validPathDFS.isThereValidPath(validWords, "cat", "pig"); // it should reach from pie
    //boolean res = validPathDFS.isThereValidPath(validWords, "cat", "dog"); // good
    //boolean res = validPathDFS.isThereValidPath(validWords, "pepper", "people");
    boolean res = validPathDFS.isThereValidPath(validWords, "head", "tail");
    System.out.println("The result was: "+res);

    WordDistance a1 = new WordDistance("Hello",1);
    WordDistance a2 = new WordDistance("World",2);
    WordDistance a3 = new WordDistance("again",3);
    WordDistance a4 = new WordDistance("and again",3);

    /*
    PriorityQueue<WordDistance> pq = new PriorityQueue<>();
    pq.add(a2);
    pq.add(a3);
    pq.add(a1);
    pq.add(a4);
    System.out.println(pq.poll());
    System.out.println(pq.poll());
    System.out.println(pq.poll());
    System.out.println(pq.poll());
    */


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

    String startWord = "head";
    String endWord = "head";
    int wordLength = startWord.length();
    // Find all the valid words from the dictionary with the given legnth:
    //

    PriorityQueue<WordDistance> pq = new PriorityQueue<>();

    Iterator<String> it = validWords.iterator();
    while (it.hasNext()) {
      String word = it.next();
      if (word.length()==wordLength) {
        pq.add(
            new WordDistance(
              word,
              word.equals(startWord)?0:Integer.MAX_VALUE
              )
            );
      }
    }
    //System.out.println(pq.poll());

    /*
    HashMap<String,StringDistance> visited = new HashMap<>();

    WordDistance current;
    while ((current = pq.poll())!= null) {
      // If the smallest is infinity there is no path
      if (current.distance == Integer.MAX_VALUE) return false;

    }
    */

  }

}

