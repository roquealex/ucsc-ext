//KthLargest.java
import java.lang.IllegalArgumentException;

public class KthLargest {
  public int findKthLargest(int[] nums, int k) throws IllegalArgumentException {
    if (k < 1 || k > nums.length) {
      throw new IllegalArgumentException(
          String.format("Argument k must be between 1 and %d",nums.length)
          );
    }
    return nums[k-1];
  }
}
