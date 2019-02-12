// Palindrome.java
public class Palindrome {

  public static boolean isPalindromeSentence(String sentence) {
    int leftIdx = 0;
    int rightIdx = sentence.length();

    // Empty string is not a palindrome
    if (rightIdx==0) return false;

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
    return true;
  }

}

