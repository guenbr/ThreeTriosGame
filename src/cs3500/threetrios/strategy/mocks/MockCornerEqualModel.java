package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;

/**
 * This mock model hard codes a grid that has equal corners.
 * This helps with testing for checking the corners.
 */
public class MockCornerEqualModel extends MockThreeTriosModel {
  private final List<Card> hand;

  /**
   * this constructs the mock model and initializes to use.
   * @param hand takes in the list of cards.
   */
  public MockCornerEqualModel(List<Card> hand) {
    super(hand, false, 3, 3);
    this.hand = hand;
  }

  @Override
  public CellType getCellType(int row, int col) {
    if ((row == 0 || row == getRows() - 1) &&
            (col == 0 || col == getCols() - 1)) {
      return CellType.CARD_CELL;
    }
    return CellType.HOLE;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return hand;
  }
}
