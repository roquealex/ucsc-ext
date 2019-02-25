// TreeUtils.java
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Random;

public class TreeUtils {

  public static <T> int getHeight(Node<T> root) {
    if (root == null) return 0;
    else {
      int hl = getHeight(root.left);
      int hr = getHeight(root.right);
      return (1 + (hl>hr?hl:hr));
    }
  }

  /*
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
      List<Node<T>> list = Arrays.asList(current.left,current.right);
      for (Node<T> dest : list) {
        if (dest!=null) {
          out.println(String.format("n%d_%s -> n%d_%s",n,current.key.toString(),cnt,dest.key.toString() ));
          q.add(dest);
          qn.add(cnt++);
        } else {
          out.println(String.format("n%d_%s -> null%d",n,current.key.toString(),nullCnt));
          nullCnt++;
        }
      }
    }
    out.println("}");
    out.close();
  }
  */

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

  /*
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
  */

  public static <T> Node<T> copyTree(Node<T> node) {
    if (node == null) return null;
    Node<T> lst = copyTree(node.left);
    Node<T> rst = copyTree(node.right);
    Node<T> nNode = new Node<T>(node.key);
    nNode.right = rst;
    nNode.left = lst;
    return nNode;
  }

  /*
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
  */

  public static Node<Integer> buildRandomSymmIntTree(int nullOneInN, int maxLevel, int max, Random rnd) {
    Node<Integer> root = new Node<Integer>(rnd.nextInt(max));
    root.left = TreeUtils.buildRandomIntTree(nullOneInN, maxLevel, max, rnd);
    root.right = copyMirrorTree(root.left);
    return root;
  }

  public static <T> Node<T> copyMirrorTree(Node<T> node) {
    if (node == null) return null;
    Node<T> lst = copyMirrorTree(node.left);
    Node<T> rst = copyMirrorTree(node.right);
    Node<T> nNode = new Node<T>(node.key);
    nNode.right = lst;
    nNode.left = rst;
    return nNode;
  }

}
