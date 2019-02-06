public class Test {
  public static void main(String[] args) {
    Board b = new Board(10);
    int dim = b.getGridSize();
    /*
    b.setCoord(3,4);
    b.setCoord(3,5);
    b.setCoord(3,6);
    */

    b.setCoord(4,3);
    b.setCoord(5,3);
    b.setCoord(6,3);

    b.print();

    ShipFinder sf = new ShipFinder(b);
    int[][] ship = sf.find3UnitShip();
    if(ship!= null) {
      System.out.println("Ship found:");
      for (int row = 0 ; row < ship.length ; row++) {
        System.out.println(String.format("(%d,%d)",ship[row][0],ship[row][1]));
      }
    } else {
      System.out.println("No Ship found");
    }
  }
}
