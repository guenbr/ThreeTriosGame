import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.strategy.Corner;
import cs3500.threetrios.strategy.StrategicThreeTrios;
import cs3500.threetrios.strategy.mocks.MockCornerEqualModel;
import cs3500.threetrios.strategy.mocks.MockDefaultCornerModel;
import cs3500.threetrios.strategy.mocks.MockNoCornerModel;
import cs3500.threetrios.strategy.mocks.MockThreeTriosModel;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the Corner strategy implementation in the Three Trios game.
 * Verifies that the strategy correctly prefers corner positions, selects high-value cards,
 * handles tie-breaking situations, and responds appropriately to illegal game states.
 */
public class TestCornerStrategy {
  private Card mockCard1;
  private Card mockCard2;
  private List<Card> mockHand;
  private StrategicThreeTrios cornerStrategy;

  @Before
  public void setUp() {
    mockCard1 = new Card("Card1", AttackValues.ONE, AttackValues.ONE,
            AttackValues.ONE, AttackValues.ONE);
    mockCard2 = new Card("Card2", AttackValues.THREE, AttackValues.THREE,
            AttackValues.THREE, AttackValues.THREE);
    mockHand = Arrays.asList(mockCard1, mockCard2);
    cornerStrategy = new Corner();
  }

  @Test
  public void testCornerStrategyPrefersCorners() {
    ReadOnlyTTModel model = new MockDefaultCornerModel(mockHand);
    Position pos = cornerStrategy.optimalPosition(model, mockCard1, Player.RED);
    Assert.assertNotNull(pos);
    Assert.assertTrue(isCorner(model, pos.getRow(), pos.getCol()));
  }

  @Test
  public void testCornerStrategySelectsHighValueCard() {
    ReadOnlyTTModel model = new MockDefaultCornerModel(mockHand);
    Card selected = cornerStrategy.optimalCard(model, Player.RED);
    Assert.assertNotNull(selected);
    Assert.assertEquals(mockCard2, selected);
  }

  @Test
  public void testCornerStrategyBreaksTiesWithPosition() {
    ReadOnlyTTModel model = new MockCornerEqualModel(mockHand);
    Position pos = cornerStrategy.optimalPosition(model, mockCard1, Player.RED);
    Assert.assertEquals(0, pos.getRow());
    Assert.assertEquals(1, pos.getCol());
  }

  @Test
  public void testGameOverCheck() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, true, 3, 3);
    Assert.assertThrows("game not started",
            IllegalStateException.class, () ->
                    cornerStrategy.optimalPosition(model, mockCard1, Player.RED));
  }

  @Test
  public void testNoLegalMoves() {
    ReadOnlyTTModel model = new MockNoCornerModel(mockHand);
    Position pos = cornerStrategy.optimalPosition(model, mockCard1, Player.RED);
    Assert.assertNull(pos);
  }

  /**
   * Determines if a given position is a corner position on the game board.
   * A corner is defined as a legal move position that has exactly two adjacent card cells.
   *
   * @param model the game model to check
   * @param row the row position to check
   * @param col the column position to check
   * @return true if the position is a corner, false otherwise
   */
  private boolean isCorner(ReadOnlyTTModel model, int row, int col) {
    if (!model.isLegalMove(row, col)) {
      return false;
    }

    int exposedCount = 0;

    if (row > 0 && model.getCellType(row - 1, col) == CellType.CARD_CELL) {
      exposedCount++;
    }

    if (row < model.getRows() - 1
            && model.getCellType(row + 1, col) == CellType.CARD_CELL) {
      exposedCount++;
    }

    if (col > 0 && model.getCellType(row, col - 1) == CellType.CARD_CELL) {
      exposedCount++;
    }

    if (col < model.getCols() - 1
            && model.getCellType(row, col + 1) == CellType.CARD_CELL) {
      exposedCount++;
    }

    return exposedCount == 2;
  }
}