import java.text.*;
import java.util.Stack;

public class BalanceUtils {
  public static boolean isBalanced(String str){
    Stack<Character> stack = new Stack<Character>();
    CharacterIterator it = new StringCharacterIterator(str);
    for (char c = it.first() ; c != CharacterIterator.DONE ; c = it.next()) {
      //System.out.println(c);
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

  public static void main(String s[]) {
    System.out.println("Balance test");
    //String test = "{ ff { } dd}";
    //String test = "{ ff [<{ }>] dd}";
    //String test = "{ ff [<{ }>] dd";
    //String test = "ff [<{ }>] dd}";
    //String test = "ff [{tt }x<ss>] dd";
    String test = "{ ff [<{ }>}] dd";
    System.out.println(isBalanced(test));
    Stack<Character> stack = new Stack<Character>();
    stack.push('a');
    stack.push('b');
    stack.push('c');
    System.out.println(stack.pop());
    System.out.println(stack.pop());
    System.out.println(stack.pop());
  }
}
