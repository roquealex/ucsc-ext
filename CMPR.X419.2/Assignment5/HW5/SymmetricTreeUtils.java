// SymmetricTreeUtils.java
public class SymmetricTreeUtils {

  private static <T> boolean areMirroredTrees(Node<T> left, Node<T> right) {
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
      // Null tree is considered symmetric by
      // extension of the 1 element case
      return true;
    }
  }

}
