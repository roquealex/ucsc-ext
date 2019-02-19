// KthLargestTest.java

public class Test {
  public static final KthLargest test = new KthLargest();
  public static void main(String s[]) {
    int a[] = {1,2,3,4,5,6};
    System.out.println("Accesing a bad index");
    int result = test.findKthLargest(a,7);
    System.out.println("Result: "+result);
  }
}
