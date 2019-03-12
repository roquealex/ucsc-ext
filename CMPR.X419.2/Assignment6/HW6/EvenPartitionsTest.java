// EvenPartitionsTest.java
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertEquals;

public class EvenPartitionsTest {

  @Test(expected = IllegalArgumentException.class)
  public void singleNonDigit() {
    System.out.println("Single Non Digit");
    String test = "X";
    EvenPartitions.countEvenPartitions(test);
  }

  @Test(expected = IllegalArgumentException.class)
  public void endsInNonDigit() {
    System.out.println("Ends in Non Digit");
    String test = "1248a";
    EvenPartitions.countEvenPartitions(test);
  }

  @Test(expected = IllegalArgumentException.class)
  public void nonDigit() {
    System.out.println("Non Digit");
    String test = "234sd14d";
    EvenPartitions.countEvenPartitions(test);
  }

  @Test
  public void specialCases() {
    System.out.println("Special cases and base cases");
    assertEquals(0, EvenPartitions.countEvenPartitions(null));
    assertEquals(0, EvenPartitions.countEvenPartitions(""));
    assertEquals(0, EvenPartitions.countEvenPartitions("7"));
    assertEquals(1, EvenPartitions.countEvenPartitions("6"));
  }

  @Test
  public void examples() {
    System.out.println("Examples");
    assertEquals(1, EvenPartitions.countEvenPartitions("10"));
    assertEquals(2, EvenPartitions.countEvenPartitions("22"));
    assertEquals(0, EvenPartitions.countEvenPartitions("333"));
  }

  @Test
  public void test() {
    System.out.println("Numbers");
    String test;

    test = "345678";
    //1234
    //12, 34
    assertEquals(4, EvenPartitions.countEvenPartitions(test));

    test = "1234";
    //1234
    //12, 34
    assertEquals(2, EvenPartitions.countEvenPartitions(test));

    test = "2004";
    // Zero is considered even:
    //2004
    //200, 4
    //20, 04
    //20, 0, 4
    //2, 004
    //2, 00, 4
    //2, 0, 04
    //2, 0, 0, 4
    assertEquals(8, EvenPartitions.countEvenPartitions(test));

    test = "434569278";
    // 16 partitions
    //434569278
    //4345692, 78
    //43456, 9278
    //43456, 92, 78
    //434, 569278
    //434, 5692, 78
    //434, 56, 9278
    //434, 56, 92, 78
    //4, 34569278
    //4, 345692, 78
    //4, 3456, 9278
    //4, 3456, 92, 78
    //4, 34, 569278
    //4, 34, 5692, 78
    //4, 34, 56, 9278
    //4, 34, 56, 92, 78
    assertEquals(16, EvenPartitions.countEvenPartitions(test));

    test = "291567806543";
    // No partitions since last number is odd
    assertEquals(0, EvenPartitions.countEvenPartitions(test));

    test = "802243260";
    assertEquals(128, EvenPartitions.countEvenPartitions(test));
  }

}
