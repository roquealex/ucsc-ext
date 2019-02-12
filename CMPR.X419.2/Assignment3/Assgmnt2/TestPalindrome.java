// TestPalindrome.java
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestPalindrome {

  @Test
  public void testPalindrome() {
    String[] palinSentences = new String[]{
      // From the web:
      "Eva, Can I Stab Bats In A Cave?",
      "A Man, A Plan, A Canal-Panama!",
      "Madam In Eden, I'm Adam",
      "Mr. Owl Ate My Metal Worm",
      "A Santa Lived As a Devil At NASA...",
      "Dammit! I'm Mad!",
      "Was It A Rat I Saw?",
      "Do Geese See God?",
      // Examples
      "Ma......a.m!",
      "No!....devil...,.'....lived......on",
      // Messy cases
      "a",
      "%$%#$%#A&^%&*%$#",
      "%AB$%#$%#Ba&^%&*%$#",
      "AB$%#$B&^%&*%$#a",
      "%aB$%X#$%#BA&^%&*%$#",
      "AbX$%#$B&^%&*%$#A",
      // Numbers
      "32.23",
      "12.5e-521"
    };
    System.out.println("Test Palindrome");
    for (String test : palinSentences) {
      assertTrue(
          "Expecting Palindrome '"+test+"'",
          Palindrome.isPalindromeSentence(test));
    }

  }

  @Test
  public void testNonPalindrome() {
    System.out.println("Test Non Palindrome");
    String[] noPalinSentences = new String[]{
      "This is a random sentence",
      "It is harder to match",
      ".$%@#$!()",
      "",
      "Mr. Owl Ate May Metal Worm",
      "A Man, A Plan, A Camel-Panama!"
    };
    for (String test : noPalinSentences) {
      assertFalse(
          "Expecting Non Palindrome '"+test+"'",
          Palindrome.isPalindromeSentence(test));
    }
  }
}

