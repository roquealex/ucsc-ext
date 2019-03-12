public class EvenPartitions {

  public static int countEvenPartitions(String s) throws IllegalArgumentException {
    if (s == null || s.length() == 0) return 0;
    return countEvenPartitions(s, 0);
  }

  private static int countEvenPartitions(String s, int startIdx) throws IllegalArgumentException {
    // The public method will make sure we are not empty or null
    boolean isEven = isEvenDigit(s.charAt(startIdx));
    // Base case last character
    if (startIdx == s.length()-1) {
      return isEven?1:0;
    } 
    int nextEvenPart = countEvenPartitions(s,startIdx+1);
    if (isEven) {
      // When even there is 2 posibilities this is concatenated
      // with all the combinations or it is the end of a partition
      return 2*nextEvenPart;
    } else {
      // When odd the odd number it can't be the end of a partition
      // so it depends on the below combinations
      return nextEvenPart;
    }
  }

  private static boolean isEvenDigit(char c) throws IllegalArgumentException {
    if (!Character.isDigit(c)) throw new IllegalArgumentException();
    return (Character.digit(c,10)%2)==0;
  }

}
