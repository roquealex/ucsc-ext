// QueueUtils.java
import java.util.Queue;

public class QueueUtils {

  public static <T> void reverseQueue(Queue<T> q) {
    T e = q.poll();
    if (e != null) {
      reverseQueue(q);
      q.add(e);
    }
  }

}

