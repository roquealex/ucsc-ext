import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Iterator;
import java.util.Arrays;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Random;

import javafx.util.Pair;

public class NodeListTest {

  public static <T> int getHeight(Node<T> node) {
    if (node == null) return 0;
    else {
      int hl = getHeight(node.left);
      int hr = getHeight(node.right);
      return (1 + (hl>hr?hl:hr));
    }
  }

  public static <T> void dotTree(Node<T> node, String filename) throws IOException {
    Queue<Node<T>> q = new LinkedList<Node<T>>();
    Queue<Integer> qn = new LinkedList<Integer>();
    PrintWriter out = new PrintWriter(filename);
    q.add(node);
    int cnt = 0;
    int nullCnt = 0;
    qn.add(cnt++);
    Node<T> current = null;
    out.println("digraph {");
    while((current=q.poll())!= null) {
      int n = qn.remove();
      //Node<T> arr[] = new Node<T>[2]{current.left,current.right};
      List<Node<T>> list = Arrays.asList(current.left,current.right);
      for (Node<T> dest : list) {
        if (dest!=null) {
          //System.out.println(current.key+" -> "+dest.key );
          out.println(String.format("n%d_%d -> n%d_%d",n,current.key,cnt,dest.key ));
          q.add(dest);
          qn.add(cnt++);
        } else {
          out.println(String.format("n%d_%d -> null%d",n,current.key,nullCnt));
          nullCnt++;
        }
      }
    }
    out.println("}");
    out.close();
  }

  public static Node<Integer> buildRandomIntTree(int nullOneInN, int maxLevel, int max, Random rnd) {
    Node<Integer> root = new Node<Integer>(rnd.nextInt(max));
    Queue<Node<Integer>> q = new LinkedList<Node<Integer>>();
    q.add(root);
    Node<Integer> sep = new Node<Integer>(-1);
    q.add(sep);
    Node<Integer> curr = null;
    int level = 1;
    while((curr=q.poll())!= null) {
      if (curr == sep) {
        level++;
        if (q.peek() != null) {
          q.add(sep);
        }
      } else {
        if (level < maxLevel && rnd.nextInt(nullOneInN)!=0) {
          curr.left = new Node<Integer>(rnd.nextInt(max));
          q.add(curr.left);
        }
        if (level < maxLevel && rnd.nextInt(nullOneInN)!=0) {
          curr.right = new Node<Integer>(rnd.nextInt(max));
          q.add(curr.right);
        }
      }
    }
    return root;
  }
  public static Node<Integer> buildRandomIntBST(int numNodes, int max, Random rnd) {
    if (numNodes <= 0 ) return null;
    numNodes--;
    Node<Integer> root = new Node<Integer>(rnd.nextInt(max));
    while(numNodes > 0) {
      Node<Integer> node = new Node<Integer>(rnd.nextInt(max));
      Node<Integer> ptr = root;
      boolean done = false;
      while(!done) {
        if (node.key > ptr.key) {
          if (ptr.right==null) {
            ptr.right = node;
            done = true;
          } else {
            ptr = ptr.right;
          }
        } else {
          if (ptr.left==null) {
            ptr.left = node;
            done = true;
          } else {
            ptr = ptr.left;
          }
        }
      }
      numNodes--;
    }
    return root;
  }

  public static <T> Node<T> copyTree(Node<T> node) {
    if (node == null) return null;
    Node<T> lst = copyTree(node.left);
    Node<T> rst = copyTree(node.right);
    Node<T> nNode = new Node<T>(node.key);
    nNode.right = rst;
    nNode.left = lst;
    return nNode;
  }

  public static <T> void inOrderPrint(Node<T> node) {
    if (node == null) return;
    inOrderPrint(node.left);
    System.out.println(node.key);
    inOrderPrint(node.right);
  }

  public static <T> void descOrderPrint(Node<T> node) {
    if (node == null) return;
    descOrderPrint(node.right);
    System.out.println(node.key);
    descOrderPrint(node.left);
  }

  public static <T> void inOrderJList(Node<T> root, LinkedList<T> list) {
    if (root == null) return;
    inOrderJList(root.left,list);
    list.add(root.key);
    inOrderJList(root.right,list);
  }


  public static <T> Node<T> createCircularDList(Node<T> root) {
    if (root==null) return null;
    Node<T> left = createCircularDList(root.left);
    Node<T> right = createCircularDList(root.right);
    Node<T> tail = null;
    if (left != null) {
      tail = left.left;
      tail.right = root;
      root.left = tail;
    } else {
      left = root;
    }
    if (right != null) {
      tail = right.left;
      root.right = right;
      right.left = root;
    } else {
      tail = root;
    }
    left.left = tail;
    tail.right = left;
    return left;
  }

  public static <T> Node<T> createDList(Node<T> root) {
    Node<T> head = createCircularDList(root);
    Node<T> tail = head.left;
    head.left = null;
    tail.right = null;
    return head;
  }

  public static void main(String s[]) throws Exception {
    Random rnd = new Random(4);
    Node<Integer> random;
    for (int i = 0 ; i < 1000 ; i++) {
    System.out.println("Random Tree");
    //random = buildRandomIntTree(5, 6, 5, rnd );
    random = buildRandomIntTree(5, 10, 500, rnd );
    //random = buildRandomIntBST(1000, 50000, rnd );
    dotTree(random,"random.dot");
    Node<Integer> copy = copyTree(random);
    //inOrderPrint(random);
    Node<Integer> head = createDList(copy);
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

}
