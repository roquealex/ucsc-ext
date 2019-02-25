// TreeToListTest.java
import org.junit.Test;
//import org.junit.Ignore;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.util.Random;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Arrays;

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

  public <T> void inOrderJList(Node<T> root, LinkedList<T> list) {
    if (root == null) return;
    inOrderJList(root.left,list);
    list.add(root.key);
    inOrderJList(root.right,list);
  }

  @Test
  public void random() {
    Random rnd = new Random(1);
    Node<Integer> random;
    System.out.println("Random Test");
    for (int i = 0 ; i < 10000 ; i++) {
      int height = rnd.nextInt(25)+1;
      int maxVal = rnd.nextInt(5000)+3;
      int nullCoef = rnd.nextInt(10)+2;
      System.out.printf("rep %d, h:%d , max:%d, null:%d\n",i,height,maxVal,nullCoef);
      random = TreeUtils.buildRandomIntTree(nullCoef, height, maxVal, rnd );
      //random = TreeUtils.buildRandomIntTree(5, 10, 500, rnd );
      Node<Integer> copy = TreeUtils.copyTree(random);
      //inOrderPrint(random);
      Node<Integer> head = TreeToList.createDList(copy);
      Node<Integer> ptr = head;
      Node<Integer> tail = null;

      LinkedList<Integer> list = new LinkedList<>();

      inOrderJList(random, list);
      Iterator<Integer> it = list.iterator();

      System.out.println("Forward");
      while(it.hasNext()) {
        int exp = it.next();
        //System.out.printf("exp:%d , act:%d\n",exp,ptr.key);
        assertEquals(exp,ptr.key.longValue());
        tail = ptr;
        ptr = ptr.right;
      }
      assertNull(ptr);

      System.out.println("Backward");
      it = list.descendingIterator();
      ptr = tail;
      while(it.hasNext()) {
        int exp = it.next();
        //System.out.printf("exp:%d , act:%d\n",exp,ptr.key);
        assertEquals(exp,ptr.key.longValue());
        ptr = ptr.left;
      }
      assertNull(ptr);
    } // for
  }

}
