public class Node<T> {
  public T key;
  public Node<T> left;
  public Node<T> right;

  Node(T key) {
    this.key = key;
    left = right = null;
  }
}
