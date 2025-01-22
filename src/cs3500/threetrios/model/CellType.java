package cs3500.threetrios.model;

/**
 * Enum class to show type of cell (for whether user is allowed to play on cell or not).
 */
public enum CellType {
  HOLE, // Hole on the grid where cards cannot be placed because they are essentially blockers.
  CARD_CELL; // Cells where cards can be placed.
}
