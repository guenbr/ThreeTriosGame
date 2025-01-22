package cs3500.threetrios.model;

import java.util.Objects;

/**
 * This class is the position of the grid.
 */
public class Position {
  private final int row;
  private final int col;

  /**
   * The position of a card in the grid when the game is being played. 
   * @param row row of the grid where the cell is. 
   * @param col column is the specific place.
   */
  public Position(int row, int col) {
    this.row = row;
    this.col = col;
  }

  /**
   * Gets the row at a given time of cardCell.
   * @return the row.
   */
  public int getRow() {
    return row;
  }

  /**
   * Like the row, it gets the column of that specific position.
   * @return the column.
   */
  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Position)) {
      return false;
    }
    Position other = (Position) obj;
    return this.row == other.row && this.col == other.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }

}
