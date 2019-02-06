public class ArrayBoard implements Board {

  private int dim;
  private boolean[][] board;

  ArrayBoard(int dim) {
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

  @Override
  public int getGridSize() {
    return dim;
  }

  @Override
  public boolean hit(int x, int y) {
    return board[y][x];
  }

}
