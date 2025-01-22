package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;

/**
 * This is a default mock model that creates a regular default
 * model. This is used when testing on a regular no case model.
 */
public class MockDefaultCornerModel extends MockThreeTriosModel {
  private final List<Card> hand;

  /**
   * Constructs the default mock model.
   * @param hand takes in a list of cards for the hand.
   */
  public MockDefaultCornerModel(List<Card> hand) {
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
