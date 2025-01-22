import cs3500.threetrios.model.Player;
import cs3500.threetrios.view.ViewListener;

/**
 * Mock ViewListener to track interactions with the players.
 */
class MockViewListener implements ViewListener {
  private final StringBuilder log;
  private int cardIndex;
  private int row;
  private int col;

  /**
   * This is to initialize the fields.
   */
  public MockViewListener() {
    this.log = new StringBuilder();
    this.cardIndex = -1;
    this.row = -1;
    this.col = -1;
  }

  @Override
  public void cardClickedListener(Player player, int cardIndex) {
    this.cardIndex = cardIndex;
    log.append(String.format("Card clicked by %s: %d\n", player, cardIndex));
  }

  @Override
  public void gridCellClickedListener(int row, int col) {
    this.row = row;
    this.col = col;
    log.append(String.format("Grid clicked at: %d,%d\n", row, col));
  }

  public String getLog() {
    return log.toString();
  }

  public int getCardIndex() {
    return cardIndex;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }
}