package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;

/**
 * This mock model assists with testing the max flip.
 * It is hard coded that the middle of the grid should
 * give the most flips. Thus, the answer it should output.
 */
public class MockDefaultFlipModel extends MockThreeTriosModel {
  private final Card highFlipCard;

  /**
   * This construct the particular model.
   * @param hand is hard coded of the player.
   * @param highFlipCard takes in a card that play to grid.
   */
  public MockDefaultFlipModel(List<Card> hand, Card highFlipCard) {
    super(hand, false, 3, 3);
    this.highFlipCard = highFlipCard;
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    if (row == 1 && col == 1) {
      return card == highFlipCard ? 3 : 1;
    }
    return 0;
  }
}
