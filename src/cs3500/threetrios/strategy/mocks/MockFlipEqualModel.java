package cs3500.threetrios.strategy.mocks;

import java.util.List;

import cs3500.threetrios.model.Card;

/**
 * This mock model assists with the testing of equal potential flips.
 * It outputs 2 as potential flips thus it results back to the tie cases.
 */
public class MockFlipEqualModel extends MockThreeTriosModel {

  /**
   * This initializes the mock model.
   * @param hand is the hand of the player.
   */
  public MockFlipEqualModel(List<Card> hand) {
    super(hand, false, 3, 3);
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    return 2;
  }
}