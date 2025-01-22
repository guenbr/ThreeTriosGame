package cs3500.threetrios.model;

/**
 * This enum is used to define directional relationships between cards on the game grid, helping
 * with attack directions, and interactions during the game. Each direction has an opposite in the
 * grid (on the cards based where they are), allowing battle phases to occur.
 */
public enum Direction {
  NORTH, SOUTH, EAST, WEST; // The four allowed directions (no southeast, southwest etc...)

  /**
   * These are the returned directions when a direction is set. For example North's opposite is
   * south, which you can imagine on the grid.
   * @return the opposite direction.
   */
  public Direction opposite() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      default: throw new IllegalStateException("Unexpected value: " + this);
    }
  }
}
