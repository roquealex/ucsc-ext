// TreeToList.java
public class TreeToList {

  public static <T> Node<T> createDList(Node<T> root) {
    if (root==null) return null;
    Node<T> head = createCircularDList(root);
    Node<T> tail = head.left;
    head.left = null;
    tail.right = null;
    return head;
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

}
