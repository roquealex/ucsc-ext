// TestQueueUtils.java
import java.util.Queue;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class TestQueueUtils {

  public static void main(String[] s) {
    LinkedList<Integer> ll= new LinkedList<Integer>();
    ll.add(1);
    ll.add(2);
    ll.add(3);
    ll.add(4);
    QueueUtils.reverseQueue(ll);
    System.out.println("reversing");
    for (int i : ll) {
      System.out.println(i);
    }
  }

}

