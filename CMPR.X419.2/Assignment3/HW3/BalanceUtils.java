import java.text.*;
import java.util.Stack;
import java.io.Reader;
import java.io.StringReader;
import java.io.FileReader;
import java.io.IOException;

public class BalanceUtils {

  public static boolean isBalancedString(String str) throws IOException {
    StringReader reader = new StringReader(str);
    return isBalanced(reader);
  }

  public static boolean isBalancedFile(String filename) throws IOException {
    FileReader reader = new FileReader(filename);
    return isBalanced(reader);
  }

  public static boolean isBalanced(Reader reader) throws IOException {
    Stack<Character> stack = new Stack<Character>();
    int intChar;
    while ((intChar=reader.read()) != -1) {
      char c = (char)intChar;
      switch(c) {
        case '{' :
        case '(' :
        case '[' :
        case '<' :
          stack.push(c);
          break;
        case '}' :
          if (stack.empty()) return false;
          else if (stack.pop() != '{') return false;
          break;
        case ')' :
          if (stack.empty()) return false;
          else if (stack.pop() != '(') return false;
          break;
        case ']' :
          if (stack.empty()) return false;
          else if (stack.pop() != '[') return false;
          break;
        case '>' :
          if (stack.empty()) return false;
          else if (stack.pop() != '<') return false;
          break;
      }
    }
    return (stack.empty());
  }

  public static void main(String s[]) throws IOException {
    System.out.println("Balance test");
    //String test = "{ ff { } dd}";
    String test = "{ ff [<{ }>] dd}";
    //String test = "{ ff [<{ }>] dd";
    //String test = "ff [<{ }>] dd}";
    //String test = "ff [{tt }x<ss>] dd";
    //String test = "{ ff [<{ }>}] dd";
    System.out.println(isBalancedString(test));
    System.out.println(isBalancedFile("QueueUtil.java"));
    Stack<Character> stack = new Stack<Character>();
    stack.push('a');
    stack.push('b');
    stack.push('c');
    System.out.println(stack.pop());
    System.out.println(stack.pop());
    System.out.println(stack.pop());
  }
}
