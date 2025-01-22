package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;

/**
 * This mock model assists with testing the max flip strategy.
 * It has numerous options to choose froms and chooses the first
 * picked one.
 */
public class MockMultipleMaxFlipsModel extends MockThreeTriosModel {

  private final Card highFlipCard;

  /**
   * Constructs the mock model.
   * @param hand current player hand.
   * @param highFlipCard is the card selected that is most optimal.
   */
  public MockMultipleMaxFlipsModel(List<Card> hand, Card highFlipCard) {
    super(hand, false, 3, 3);
    this.highFlipCard = highFlipCard;
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    if ((row == 0 && col == 1) || (row == 1 && col == 1)) {
      return card == highFlipCard ? 3 : 1;
    }
    return 0;
  }
}
