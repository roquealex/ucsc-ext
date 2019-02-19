// KthLargestTest.java
import org.junit.Test;
import org.junit.Ignore;
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

  @Test(expected = IllegalArgumentException.class)
  public void nullArr() {
    System.out.println("Null Array");
    int notinit[]= null;
    int result = test.findKthLargest(notinit,1);
  }

  @Test
  public void focused() {
    System.out.println("Focused");
    int a[]= {1};
    assertEquals(1,test.findKthLargest(a,1));
    int b[]= {4,-1,5,-2,1,0,3,-3,-4,2,-5};
    assertEquals(-5,test.findKthLargest(b,11));
    assertEquals(-4,test.findKthLargest(b,10));
    assertEquals(3,test.findKthLargest(b,3));
    assertEquals(4,test.findKthLargest(b,2));
    assertEquals(5,test.findKthLargest(b,1));
  }

  @Test
  public void random() {
    Random rand = new Random(1);
    int testVectors = 1000;
    for (int i = 0 ; i < testVectors ; i++ ) {
      System.out.println("Iteration "+i);
      int size = rand.nextInt(1000)+1;
      //int size = 15;
      System.out.println("Chosen size "+size);
      int a[] = rand.ints(0,(size*2)).limit(size).toArray();
      int k = rand.nextInt(size)+1;
      System.out.println(String.format("Random k=%d",k));
      int result = test.findKthLargest(a,k);
      //System.out.println(Arrays.toString(a));
      Arrays.sort(a);
      //System.out.println(Arrays.toString(a));
      assertEquals(a[a.length-k],result);
    }
  }

}
