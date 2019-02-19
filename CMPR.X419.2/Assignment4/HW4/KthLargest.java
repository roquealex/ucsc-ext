//KthLargest.java
//import java.lang.IllegalArgumentException;
import java.util.PriorityQueue;

public class KthLargest {
  public int findKthLargest(int[] nums, int k) throws IllegalArgumentException {
    if (nums==null) {
      throw new IllegalArgumentException("Array should not be null");
    }
    if (k < 1 || k > nums.length) {
      throw new IllegalArgumentException(
          String.format("Argument k must be between 1 and %d",nums.length)
          );
    }
    PriorityQueue<Integer> heap = new PriorityQueue<Integer>(k);
    for (int i = 0 ; i < nums.length ; i++) {
      if (i < k) {
        heap.add(nums[i]);
      } else {
        if (heap.peek() < nums[i]) {
          int dummy = heap.poll();
          heap.add(nums[i]);
        }
      }
    }
    //System.out.println(heap.size());
    //return nums[k-1];
    return heap.peek();
  }
}
