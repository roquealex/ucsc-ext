import java.util.stream.IntStream;

public class ShipFinder {
  private Board board;

  ShipFinder(Board b) {
    board = b;
  }

  public int[][] find3UnitShip() {
    int dim = board.getGridSize();
    final int SHIP_SIZE = 3;
    int[][] result;
    for (int y = 0 ; y < dim ; y++) {
      for (int x = y%SHIP_SIZE ; x < dim ; x+= SHIP_SIZE) {
        if (board.hit(x,y)) {
          // Try the suroundings 
          boolean north, south, east, west;
          north = y!=0 && board.hit(x,y-1);
          south = y!=(dim-1) && board.hit(x,y+1);
          west = x!=0 && board.hit(x-1,y);
          east = x!=(dim-1) && board.hit(x+1,y);
          System.out.println("Hit "+x+","+y);
          System.out.println("N "+north);
          System.out.println("S "+south);
          System.out.println("E "+east);
          System.out.println("W "+west);
          if (!north && !south) {
            // Horizontal
            System.out.println("H");
            // find the first hit
            int v;
            for (v = x-(SHIP_SIZE-1) ; v < x ; v++) {
              System.out.println(v);
              if(v >= 0 && board.hit(v,y)) {
                System.out.println("Hit at "+v+","+y);
                break;
              }
            }
            //int limit = v+SHIP_SIZE;
            System.out.println("Start at "+v);
            result = new int[SHIP_SIZE][];
            for (int row = 0 ; row < result.length ; row++) {
              result[row] = new int[]{v+row,y};
            }
            return result;
          } else if (!east && !west) {
            // Vertical
            System.out.println("V");
            // find the first hit
            int v;
            for (v = y-(SHIP_SIZE-1) ; v < y ; v++) {
              System.out.println(v);
              if(v >= 0 && board.hit(x,v)) {
                System.out.println("Hit at "+x+","+v);
                break;
              }
            }
            //int limit = v+SHIP_SIZE;
            System.out.println("Start at "+v);
            result = new int[SHIP_SIZE][];
            for (int row = 0 ; row < result.length ; row++) {
              result[row] = new int[]{x,v+row};
            }
            return result;
          } else {
            // Error condition
            return null;
          }
        }
      }
    }
    return null;
  }

  /*
  public static void main(String[] args) {
    Board b = new Board(10);
    //b.setCoord(3,3);
    b.print();
    int dim = b.getGridSize();
    for (int y = 0 ; y < dim ; y++) {
      for (int x = y%3 ; x < dim ; x+= 3) {
        b.setCoord(x,y);
      }
    }
    b.setCoord(3,4);
    b.setCoord(3,5);
    b.setCoord(3,6);
    b.print();
  }
   */
}
