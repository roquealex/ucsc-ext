import java.util.Random;

public class NodeTest {

  public static void main(String s[]) throws Exception {
    Node<Integer> l1 = new Node<Integer>(1);
    Node<Integer> l2_1 = new Node<Integer>(2);
    Node<Integer> l2_2 = new Node<Integer>(3);
    Node<Integer> l3_1 = new Node<Integer>(4);
    Node<Integer> l4_2 = new Node<Integer>(9);
    l1.left = l2_1;
    l1.right = l2_2;
    l2_1.left = l3_1;
    l3_1.right = l4_2;
    //System.out.println("Node val is "+l1.key);
    //System.out.println("Height is "+getHeight(l1));
    Node<Integer> mirror = TreeUtils.copyMirrorTree(l1);
    TreeUtils.dotTree(l1,"tree.dot");
    TreeUtils.dotTree(mirror,"mirror.dot");
    Node<Integer> symm = new Node<Integer>(0);
    symm.left = l1;
    symm.right = mirror;
    TreeUtils.dotTree(symm,"symm.dot");
    boolean symmTest = SymmetricTreeUtils.isSymmetric(symm);
    //boolean symmTest = isSymmetric(mirror);
    System.out.println("Tree is symm: "+symmTest);
    Random rnd = new Random(1);
    //Node<Integer> random = TreeUtils.buildRandomIntTree(5, 5, 10, rnd );
    Node<Integer> random;
    int counter=0;
    /*
    // Look for false positive
    do {
      System.out.println(counter++);
      random = TreeUtils.buildRandomIntTree(5, 6, 5, rnd );
    } while(getHeight(random)<5 || !isSymmetric(random));
    */
    // All symm:
    do {
      System.out.println(counter);
      counter++;
      int height = rnd.nextInt(10)+1;
      int maxVal = rnd.nextInt(10)+3;
      int nullCoef = rnd.nextInt(10)+2;
      System.out.printf("h:%d , max:%d, null:%d\n",height,maxVal,nullCoef);
      random = TreeUtils.buildRandomSymmIntTree(nullCoef, height, maxVal, rnd );
    } while(counter < 100000000 && SymmetricTreeUtils.isSymmetric(random));
    TreeUtils.dotTree(random,"random.dot");
  }

}
