// TestBalanceUtils.java
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.io.IOException;

public class TestBalanceUtils {

  @Test
  public void testBalance() throws IOException {
    System.out.println("Balance test");
    String[] balanced = new String[]{
      "{ ff { } dd}",
      "ff [{tt }x<ss>] dd",
      "ff [{t[ggfg](dff)t }x<ss>] dd",
      "{ ff [<{ }>] dd}"
    };
    for (String test : balanced) {
      //System.out.println(BalanceUtils.isBalancedString(test));
      assertTrue(
          "String is not considered balanced '"+test+"'",
          BalanceUtils.isBalancedString(test));
    }
    //System.out.println(BalanceUtils.isBalancedFile("QueueUtil.java"));
  }

  @Test
  public void testImbalance() throws IOException {
    System.out.println("Imbalance test");
    String[] imbalanced = new String[]{
      "{ ff {  dd}",
      "ff [{tt }xss>] dd",
      "ff [tt }x<ss>] dd",
      "ff [<{ }>] dd}",
      "{ ff [<{ }>}] dd",
      "{ ff [<{ }>] dd"
    };
    for (String test : imbalanced) {
      assertFalse(
          "String is considered balanced '"+test+"'",
          BalanceUtils.isBalancedString(test));
    }
  }

  @Test
  public void fileBalanceTest() throws IOException {
    System.out.println("File Balance test");
    assertTrue(BalanceUtils.isBalancedFile("QueueUtils.java"));
  }

  @Test
  public void fileImbalanceTest() throws IOException {
    System.out.println("File Imbalance test");
    assertFalse(BalanceUtils.isBalancedFile("BalanceUtils.java"));
  }



}
