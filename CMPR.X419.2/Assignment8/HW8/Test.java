public class Test {

  static String changeCharacter(String orig, int pos, char c) {
    StringBuffer buff = new StringBuffer();
    if (pos!=0) {
      buff.append(orig.substring(0,pos));
    }
    buff.append('X');
    if (pos!=orig.length()-1) {
      buff.append(orig.substring(pos+1));
    }
    //System.out.println(buff.toString());
    return buff.toString();
  }

  public static void main(String s[]) {
    System.out.println("Hello");
    String test = "cat";
    for (int pos = 0 ; pos < test.length() ; pos++) {
      String str = changeCharacter(test, pos, 'X');
      System.out.println(str);
    }
      /*
    for (char c = 'a' ; c <= 'z' ; c++) {
      System.out.println(c);
    }
    */
  }
}


