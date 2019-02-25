// TreeToListTest.java
import org.junit.Test;
//import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
//import java.util.Random;
//import java.util.Arrays;

public class TreeToListTest {
  //public static final TreeTo test = new KthLargest();
  @Test
  public void basic() {
  //public static void main(String[] args) throws Exception {
    System.out.println("Basic test");
    String exp[] = {"cat","dog","bird","mouse","monkey","fish"};
    Node<String> n0 = new Node<>(exp[0]);
    Node<String> n1 = new Node<>(exp[1]);
    Node<String> n2 = new Node<>(exp[2]);
    Node<String> n3 = new Node<>(exp[3]);
    Node<String> n4 = new Node<>(exp[4]);
    Node<String> n5 = new Node<>(exp[5]);
    Node<String> root = n3;
    n3.left = n1;
    n1.left = n0;
    n1.right = n2;
    n3.right = n5;
    n5.left = n4;
    Node<String> head = TreeToList.createDList(root);
    Node<String> ptr = head;
    Node<String> tail = null;
    for (String str : exp) {
      assertEquals(str,ptr.key);
      tail = ptr;
      ptr = ptr.right;
    }
    assertNull(ptr);
    ptr = tail;
    for(int i= exp.length-1 ; i >= 0 ; i--) {
      assertEquals(exp[i],ptr.key);
      ptr = ptr.left;
    }
    assertNull(ptr);
  }

  @Test
  public void nullTest() {
    System.out.println("Null Test");
    Node<String> root = null;
    assertNull(root);
    Node<String> head = TreeToList.createDList(root);
    assertNull(head);
  }

  @Test
  public void singleTest() {
    System.out.println("Single Test");
    double exp = 12.3;
    Node<Double> root = new Node<>(exp);
    assertEquals(exp,root.key.doubleValue(),0.0);
    assertNull(root.left);
    assertNull(root.right);
    Node<Double> head = TreeToList.createDList(root);
    assertEquals(exp,head.key.doubleValue(),0.0);
    assertNull(head.left);
    assertNull(head.right);
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



  /*
  @Test(expected = IllegalArgumentException.class)
  public void badHighTest() {
    int a[] = {1,2,3,4,5,6};
    System.out.println("Accesing a bad index");
    int result = test.findKthLargest(a,7);
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
  */

}
