public class Test {
  public static void main(String[] args) {
    Board b = new Board(10);
    //b.setCoord(3,3);
    //b.print();
    int dim = b.getGridSize();
    /*
    for (int y = 0 ; y < dim ; y++) {
      for (int x = y%3 ; x < dim ; x+= 3) {
        b.setCoord(x,y);
      }
    }
    */
    /*
    b.setCoord(3,4);
    b.setCoord(3,5);
    b.setCoord(3,6);
    */
    //b.setCoord(0,0);
    b.setCoord(1,0);
    b.setCoord(2,0);
    b.setCoord(3,0);

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
