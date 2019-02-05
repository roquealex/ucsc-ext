public class Board {

  private int dim;
  private boolean[][] board;

  Board(int dim) {
    this.dim = dim;
    board = new boolean[dim][dim];
  }

  public void print() {
    for (int y = 0 ; y < dim ; y++) {
      for (int x = 0 ; x < dim ; x++) {
        System.out.print(hit(x,y)?"[X]":"[ ]");
      }
      System.out.println("");
    }
  }

  public void setCoord(int x, int y) {
    board[y][x] = true;
  }

  public int getGridSize() {
    return dim;
  }

  public boolean hit(int x, int y) {
    return board[y][x];
  }

}
