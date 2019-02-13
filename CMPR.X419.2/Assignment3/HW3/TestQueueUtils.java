import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.LinkedList;

public class TestQueueUtils {

  @Test
  public void basicTestReverse() {
    LinkedList<Integer> ll= new LinkedList<Integer>();
    int i;
    for (i = 0 ; i < 5 ; i++) {
      ll.add(i);
    }
    QueueUtils.reverseQueue(ll);
    for (int x : ll) {
      //System.out.println(x);
      assertEquals(x,--i);
    }
  }

  @Test
  public void otherTestReverse() {
    LinkedList<Integer> ll = new LinkedList<Integer>(Arrays.asList(-231,23,322,-54,2,9,1,345,-7));
    Integer[] arr = ll.toArray(new Integer[0]);
    int i = arr.length;
    QueueUtils.reverseQueue(ll);
    for (Integer x : ll) {
      //System.out.println(x);
      assertEquals(x,arr[--i]);
    }
  }

  @Test
  public void emptyTestReverse() {
    LinkedList<Integer> ll = new LinkedList<Integer>();
    assertEquals(ll.size(),0);
    QueueUtils.reverseQueue(ll);
    assertEquals(ll.size(),0);
  }


}

