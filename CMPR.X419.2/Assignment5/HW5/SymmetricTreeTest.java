// SymmetricTreeTest.java
import org.junit.Test;
//import org.junit.Ignore;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Random;

public class SymmetricTreeTest {

  @Test
  public void basic() {
    System.out.println("Basic test");

    Node<Integer> l1 = new Node<Integer>(1);
    Node<Integer> l2_1 = new Node<Integer>(2);
    Node<Integer> l2_2 = new Node<Integer>(3);
    Node<Integer> l3_1 = new Node<Integer>(4);
    Node<Integer> l4_2 = new Node<Integer>(9);
    l1.left = l2_1;
    l1.right = l2_2;
    l2_1.left = l3_1;
    l3_1.right = l4_2;
    Node<Integer> mirror = TreeUtils.copyMirrorTree(l1);
    assertFalse(SymmetricTreeUtils.isSymmetric(l1));
    assertFalse(SymmetricTreeUtils.isSymmetric(mirror));
    Node<Integer> symm = new Node<Integer>(0);
    symm.left = l1;
    symm.right = mirror;
    //try {TreeUtils.dotTree(symm,"symm.dot"); }
    //catch (Exception e) {}
    boolean symmTest = SymmetricTreeUtils.isSymmetric(symm);
    assertTrue(symmTest);
  }

  @Test
  public void nullTest() {
    System.out.println("Null test");
    Node<Integer> root = null;
    assertTrue(SymmetricTreeUtils.isSymmetric(root));
  }

  @Test
  public void single() {
    System.out.println("Single test");
    Node<Double> root = new Node<>(2.5);
    assertTrue(SymmetricTreeUtils.isSymmetric(root));
  }

  @Test
  public void randomSymmetry() {
    System.out.println("Symmetry test");
    Random rnd = new Random(1);
    Node<Integer> random;
    int counter=0;
    do {
      int height = rnd.nextInt(22)+1;
      int maxVal = rnd.nextInt(1000)+3;
      int nullCoef = rnd.nextInt(10)+3;
      System.out.printf("n:%d, h:%d , max:%d, null:%d\n",counter,height,maxVal,nullCoef);
      random = TreeUtils.buildRandomSymmIntTree(nullCoef, height, maxVal, rnd );
      assertTrue(SymmetricTreeUtils.isSymmetric(random));
      counter++;
    } while(counter < 10000 );
  }

  @Test
  public void randomAsymmetry() throws Exception {
    System.out.println("Asymmetry test");
    Random rnd = new Random(1);
    Node<Integer> random;
    int counter=0;
    do {
      int height = rnd.nextInt(10)+10;
      int maxVal = rnd.nextInt(10)+5;
      int nullCoef = rnd.nextInt(10)+2;
      System.out.printf("n:%d, h:%d , max:%d, null:%d\n",counter,height,maxVal,nullCoef);
      random = TreeUtils.buildRandomIntTree(nullCoef, height, maxVal, rnd );
      //TreeUtils.dotTree(random,"random.dot");
      // Eliminating shorter than 4 trees since they
      // have high probability of symmetry
      assertTrue(!SymmetricTreeUtils.isSymmetric(random)||TreeUtils.getHeight(random)<4);
      counter++;
    } while(counter < 10000 );
  }

}
