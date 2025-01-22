package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;

/**
 * This model assists with the testing of corner strategy.
 * It hard codes a mock model that has no corners.
 * This results the test in making the default move since
 * there is no corners on the model.
 */
public class MockNoCornerModel extends MockThreeTriosModel {
  private final List<Card> hand;

  /**
   * This constructs the mock model and initializes.
   * @param hand is the current players hand.
   */
  public MockNoCornerModel(List<Card> hand) {
    super(hand, false, 3, 3);
    this.hand = hand;
  }

  @Override
  public CellType getCellType(int row, int col) {
    return row == 1 && col == 1 ? CellType.CARD_CELL : CellType.HOLE;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return hand;
  }
}
