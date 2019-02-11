import java.util.Queue;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class QueueUtil {

  public static <T> void reverseQueue(Queue<T> q) {
    T e = q.poll();
    if (e != null) {
      reverseQueue(q);
      q.add(e);
    }
  }

  public static void main(String[] s) {
    LinkedList<Integer> ll= new LinkedList<Integer>();
    ll.add(1);
    ll.add(2);
    ll.add(3);
    ll.add(4);
    reverseQueue(ll);
    for (int i : ll) {
      System.out.println(i);
    }
    System.out.println("reversing");
  }

}

