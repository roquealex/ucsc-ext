// KthLargestTest.java
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Random;
import java.util.Arrays;
//import java.util.stream.IntStream;

public class KthLargestTest {
  public static final KthLargest test = new KthLargest();

  @Test(expected = IllegalArgumentException.class)
  public void badHighTest() {
    int a[] = {1,2,3,4,5,6};
    System.out.println("Accesing a bad index");
    int result = test.findKthLargest(a,7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void badLowTest() {
    int a[] = {1,2,3,4,5,6};
    System.out.println("Accesing a bad index 0");
    int result = test.findKthLargest(a,0);
  }

  @Test
  public void identityTest() {
    int a[] = {10,9,8,7,6,5,4,3,2,1};
    System.out.println("Identity");
    for (int k = 1 ; k <= 10 ; k++) {
      int result = test.findKthLargest(a,k);
      assertEquals(10 + 1 - k, result);
    }
  }

  @Test
  public void random() {
    Random rand = new Random(1);
    //int size = rand.nextInt(20);
    int size = 15;
    //IntStream stream = rand.ints(0,size*2).limit(size);
    int a[] = rand.ints(0,size*2).limit(size).toArray();
    int k = rand.nextInt(size)+1;
    System.out.println(String.format("Random k=%d",k));
    int result = test.findKthLargest(a,k);
    System.out.println(Arrays.toString(a));
    Arrays.sort(a);
    System.out.println(Arrays.toString(a));
    assertEquals(a[a.length-k],result);
  }

}
