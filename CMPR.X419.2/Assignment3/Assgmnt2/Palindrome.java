public class Palindrome {

  public static boolean isPalindromeSentence(String sentence) {
    int leftIdx = 0;
    int rightIdx = sentence.length();
    boolean result = true;
    /*
    for (int i = 0 ; i < rightIdx ; i++ ) {
      System.out.println(sentence.charAt(i));
    }
    */
    while (leftIdx != rightIdx) {
      // Advance left
      boolean notEmpty;
      while( (notEmpty = leftIdx < sentence.length())
          && !Character.isLetterOrDigit(sentence.charAt(leftIdx)) ) {
        leftIdx++;
      }
      // Empty case decided to be false:
      if (!notEmpty) return false; 
      // Reached the other side we are done
      if (leftIdx == rightIdx) return true;

      while( !Character.isLetterOrDigit(sentence.charAt(--rightIdx)) );

      // Same charcter at the end
      if (leftIdx == rightIdx) return true;

      if (Character.toLowerCase(sentence.charAt(leftIdx))
            !=Character.toLowerCase(sentence.charAt(rightIdx))){
        return false;
            }
      leftIdx++;
    }
    return result;
  }

  public static void main(String arg[]) {
    //String test = "This is sisi HT";
    String[] palinSentences = new String[]{
      "Eva, Can I Stab Bats In A Cave?",
      "A Man, A Plan, A Canal-Panama!",
      "Madam In Eden, I'm Adam",
      "Mr. Owl Ate My Metal Worm",
      "A Santa Lived As a Devil At NASA...",
      "Dammit! I'm Mad!",
      "Was It A Rat I Saw?",
      "Do Geese See God?",
      "Ma......a.m!",
      "No!....devil...,.'....lived......on",
      "32.23",
      "12.5e-521"
    };
    System.out.println("Hello Pal");
    for (String test : palinSentences) {
      System.out.println(isPalindromeSentence(test));
    }

    String[] noPalinSentences = new String[]{
      "This is a random sentence",
      "It is harder to match",
      ".$%@#$!()",
      "Mr. Owl Ate May Metal Worm",
      "A Man, A Plan, A Camel-Panama!"
    };
    for (String test : noPalinSentences) {
      System.out.println(isPalindromeSentence(test));
    }
  }
}

