import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Arrays;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.Random;

public class NodeTest {

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

  public static Node<Integer> buildRandomSymmIntTree(int nullOneInN, int maxLevel, int max, Random rnd) {
    Node<Integer> root = new Node<Integer>(rnd.nextInt(max));
    root.left = buildRandomIntTree(nullOneInN, maxLevel, max, rnd);
    root.right = copyMirrorTree(root.left);
    return root;
  }

  public static <T> Node<T> copyMirrorTree(Node<T> node) {
    //Stack<Node<T>> s = new Stack<Node<T>>();
    //Node<T> curr = node;
    if (node == null) return null;
    Node<T> lst = copyMirrorTree(node.left);
    Node<T> rst = copyMirrorTree(node.right);
    Node<T> nNode = new Node<T>(node.key);
    nNode.right = lst;
    nNode.left = rst;
    return nNode;
  }

  /*
  public static <T> void dotTree(Node<T> node) {
    Stack<Node<T>> s = new Stack<Node<T>>();
    Stack<String> name = new Stack<String>;
    Node<T> curr = node;
    System.out.println("digraph {");
    int n = 0;
    while (curr != null || !s.empty()) {
      if (curr == null) {
        curr = s.pop();
      } else {
      }
      for (Node<T> dest : Arrays.asList(curr.left,curr.right)) {
        if (dest!=null) {
          System.out.println(curr.key+" -> "+dest.key );
        } else {
          System.out.println(curr.key+" -> null"+n );
          n++;
        }
      }
      if (curr.right!=null) {
        s.push(curr.right);
      }
      curr = curr.left;
    }
    System.out.println("}");
  }
  */


  public static <T> boolean areMirroredTrees(Node<T> left, Node<T> right) {
    if (left==null && right==null) {
      return true;
    } else if (left==null || right==null) {
      return false;
    } else {
      if (left.key == right.key) {
        return (areMirroredTrees(left.left,right.right) && areMirroredTrees(left.right,right.left));
      } else {
        return false;
      }
    }

  }

  public static <T> boolean isSymmetric(Node<T> root) {
    if (root != null) {
      return areMirroredTrees(root.left, root.right);
    } else {
      // Null tree is not considered symmetric
      return false;
    }
  }

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
    Node<Integer> mirror = copyMirrorTree(l1);
    dotTree(l1,"tree.dot");
    dotTree(mirror,"mirror.dot");
    Node<Integer> symm = new Node<Integer>(0);
    symm.left = l1;
    symm.right = mirror;
    dotTree(symm,"symm.dot");
    boolean symmTest = isSymmetric(symm);
    //boolean symmTest = isSymmetric(mirror);
    System.out.println("Tree is symm: "+symmTest);
    Random rnd = new Random(4);
    //Node<Integer> random = buildRandomIntTree(5, 5, 10, rnd );
    Node<Integer> random;
    int counter=0;
    /*
    // Look for false positive
    do {
      System.out.println(counter++);
      random = buildRandomIntTree(5, 6, 5, rnd );
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
      random = buildRandomSymmIntTree(nullCoef, height, maxVal, rnd );
    } while(counter < 100000000 && isSymmetric(random));
    dotTree(random,"random.dot");
  }

}
