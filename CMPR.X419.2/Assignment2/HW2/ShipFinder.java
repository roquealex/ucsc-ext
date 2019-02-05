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
            int sx;
            for (sx = x-(SHIP_SIZE-1) ; sx < x ; sx++) {
              System.out.println(sx);
              if(sx >= 0 && board.hit(sx,y)) {
                System.out.println("Hit at "+sx+","+y);
                break;
              }
            }
            //int limit = sx+SHIP_SIZE;
            System.out.println("Start at "+sx);
            result = new int[SHIP_SIZE][];
            for (int row = 0 ; row < result.length ; row++) {
              result[row] = new int[]{sx+row,y};
            }
            return result;
            //return new int[SHIP_SIZE][2];
            //return IntStream.range(sx,sx+SHIP_SIZE).mapToObj(nx -> new int[]{nx,y}).toArray();
          } else {
            // Vertical
            System.out.println("V");
            return new int[SHIP_SIZE][2];
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
