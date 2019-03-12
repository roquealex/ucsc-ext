public class EvenPartitionsUtils {

  public static void printEvenPartitions(String s) throws IllegalArgumentException {
    if (s == null) return;
    int length = s.length();
    if (length ==0) return;
    char last = s.charAt(length-1);
    //System.out.println(last);
    int i = 0;
    // Get rid of leading zeros
    while ((i < length) && s.charAt(i) == '0') i++;
    printEvenPartitions(s.substring(i), s.substring(0,i));
  }

  private static void printEvenPartitions(String s, String prefix ) throws IllegalArgumentException {
    int i = 0;
    int length = s.length();
    // We can remove all the chekcs for length since we know it ends in even
    //while ((i < length) && s.charAt(i) == '0') i++;
    while ((i < length) && !isEvenDigit(s.charAt(i))) i++;
    if (i==length) return;
    if (i==(length-1)) {
      System.out.println(prefix+s);
    } else {
      //boolean isZero = s.charAt(i)=='0';
      //boolean leadingZero = isZero && i==0;
      String nextStr = s.substring(i+1);
      StringBuffer buff = new StringBuffer(prefix);
      buff.append(s.substring(0,i+1));
      printEvenPartitions(nextStr, buff.toString());
      buff.append(", ");
      //if (!leadingZero)
      printEvenPartitions(nextStr, buff.toString());
    }
  }

  /*
  public static void printEvenPartitionsNoZero(String s) throws IllegalArgumentException {
    if (s == null) return;
    int length = s.length();
    if (length ==0) return;
    char last = s.charAt(length-1);
    //System.out.println(last);
    int i = 0;
    // Get rid of leading zeros
    while ((i < length) && s.charAt(i) == '0') i++;
    printEvenPartitionsNoZero(s.substring(i), s.substring(0,i));
  }

  private static void printEvenPartitionsNoZero(String s, String prefix ) throws IllegalArgumentException {
    int i = 0;
    int length = s.length();
    // We can remove all the chekcs for length since we know it ends in even
    //while ((i < length) && s.charAt(i) == '0') i++;
    while ((i < length) && !isEvenDigit(s.charAt(i))) i++;
    if (i==length) return;

    int evenPos = i;
    i++;
    while ((i < length) && s.charAt(i) == '0') i++;
    int nextPos = i;

    if (nextPos==(length)) {
      System.out.println(prefix+s);
    } else {
      //boolean isZero = s.charAt(i)=='0';
      //boolean leadingZero = isZero && i==0;
      String nextStr = s.substring(nextPos);
      StringBuffer buff = new StringBuffer(prefix);
      buff.append(s.substring(0,nextPos));
      printEvenPartitionsNoZero(nextStr, buff.toString());
      buff.append(", ");
      printEvenPartitionsNoZero(nextStr, buff.toString());
      for(i = evenPos+1 ; i < nextPos ; i++) {
        buff = new StringBuffer(prefix);
        buff.append(s.substring(0,i));
        buff.append(", ");
        buff.append(s.substring(i,nextPos));
        printEvenPartitionsNoZero(nextStr, buff.toString());
      }

    }
  }
  */

  private static boolean isEvenDigit(char c) throws IllegalArgumentException {
    if (!Character.isDigit(c)) throw new IllegalArgumentException();
    return (Character.digit(c,10)%2)==0;
  }

  /*
  public static void main(String[] args) {
    System.out.println("Hi");
    String sample = "123rsddw89";
    for (int i = 0 ; i < sample.length() ; i++) {
      System.out.println(sample.charAt(i));
      //System.out.println(isEvenDigit(sample.charAt(i)));
      if(Character.isDigit(sample.charAt(i))) {
        int dig = Character.digit(sample.charAt(i),10);
        System.out.println(dig*-1);
        System.out.println(isEvenDigit(sample.charAt(i)));
      } else {
        System.out.println("Not a digit");
      }
    }
    //printEvenPartitions("Hellox");
    printEvenPartitions("000356");
    printEvenPartitions("1234");
    printEvenPartitions("12345");
    printEvenPartitions("123456");
    printEvenPartitions("12345678");
    printEvenPartitions("175676039");
    printEvenPartitions("434569278");
    printEvenPartitions("00434569278");
    printEvenPartitions("802243260");
    printEvenPartitions("10");
    printEvenPartitions("200");
    printEvenPartitions("1324");

    //printEvenPartitionsNoZero("2004");
    //printEvenPartitionsNoZero("2000");
    //printEvenPartitionsNoZero("1252008");
    //printEvenPartitionsNoZero("100");
    //printEvenPartitionsNoZero("1004");
    //printEvenPartitionsNoZero("802243260");

  }
  */

}
