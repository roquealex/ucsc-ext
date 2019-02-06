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

          int vLimit;
          boolean fixedX;
          if (!north && !south) {
            // Horizontal
            vLimit = x;
            fixedX = false;
          } else if (!east && !west) {
            // Vertical
            vLimit = y;
            fixedX = true;
          } else {
            // Error condition
            return null;
          }

          // Find the first coordinate of the ship
          int v;
          for (v = vLimit -(SHIP_SIZE-1) ; v < vLimit  ; v++) {
            if(v >= 0 && ((fixedX)?board.hit(x,v):board.hit(v,y))) {
              break;
            }
          }
          // Create a bi dimensional array to return
          // {{x1,y1},{x2,y2},{x3,y3}}
          result = new int[SHIP_SIZE][];
          for (int row = 0 ; row < result.length ; row++) {
            result[row] = (fixedX)?new int[]{x,v+row}:new int[]{v+row,y};
          }
          return result;
        }
      }
    }
    return null;
  }

}
