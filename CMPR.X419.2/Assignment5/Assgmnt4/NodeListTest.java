import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;

public class NodeListTest {

  public static void main(String s[]) throws Exception {
    Random rnd = new Random(4);
    Node<Integer> random;
    for (int i = 0 ; i < 1000 ; i++) {
    System.out.println("Random Tree");
    //random = buildRandomIntTree(5, 6, 5, rnd );
    random = TreeUtils.buildRandomIntTree(5, 10, 500, rnd );
    //random = buildRandomIntBST(1000, 50000, rnd );
    TreeUtils.dotTree(random,"random.dot");
    Node<Integer> copy = TreeUtils.copyTree(random);
    //inOrderPrint(random);
    Node<Integer> head = TreeToList.createDList(copy);
    Node<Integer> ptr = head;
    Node<Integer> tail = null;

    LinkedList<Integer> list = new LinkedList<>();

    inOrderJList(random, list);
    Iterator<Integer> it = list.iterator();

    System.out.println("JList");
    //ptr = null;
    while(it.hasNext()) {
      int exp = it.next();
      assert (ptr!=null) : "Null list";
      System.out.printf("exp:%d , act:%d\n",exp,ptr.key);
      assert (exp==ptr.key) : "Different";
      tail = ptr;
      ptr = ptr.right;
    }
    assert (ptr==null) : "Different Size";

    System.out.println("List");
    while(ptr!=null) {
      System.out.println(ptr.key);
      tail = ptr;
      ptr = ptr.right;
    }
    System.out.println("Desc JList");
    //descOrderPrint(random);
    it = list.descendingIterator();

    ptr = tail;

    while(it.hasNext()) {
      int exp = it.next();
      assert (ptr!=null) : "Null list";
      System.out.printf("exp:%d , act:%d\n",exp,ptr.key);
      assert (exp==ptr.key) : "Different";
      tail = ptr;
      ptr = ptr.left;
    }
    assert (ptr==null) : "Different Size";


    System.out.println("Desc list");
    while(ptr!=null) {
      System.out.println(ptr.key);
      ptr = ptr.left;
    }
    } //for...

  }

  public static <T> void inOrderJList(Node<T> root, LinkedList<T> list) {
    if (root == null) return;
    inOrderJList(root.left,list);
    list.add(root.key);
    inOrderJList(root.right,list);
  }

}
