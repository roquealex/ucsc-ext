import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {

  public static void help() {
    System.out.println("TestRunner needs one parameter which is the class containing tests:");
    System.out.println("java TestRunner <TestClassName>");
    System.exit(1);
  }

  public static void main(String[] args) throws ClassNotFoundException {
    if (args.length != 1) help();
    Result result = JUnitCore.runClasses(Class.forName(args[0]));
    for (Failure failure : result.getFailures()) {
      System.out.println(failure.toString());
    }
    System.out.println("Test: "+(result.wasSuccessful()?"PASSED":"FAILED"));
  }

} 
