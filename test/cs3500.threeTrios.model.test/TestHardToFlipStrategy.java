import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.strategy.HardToFlip;
import cs3500.threetrios.strategy.StrategicThreeTrios;
import cs3500.threetrios.strategy.mocks.MockThreeTriosModel;

/**
 * Test class for the HardToFlip strategy implementation.
 */
public class TestHardToFlipStrategy {
  private Card strongCard;
  private List<Card> mockHand;
  private StrategicThreeTrios hardToFlipStrategy;

  @Before
  public void setUp() {
    // Create cards with different attack values
    strongCard = new Card("StrongCard",
            AttackValues.NINE, AttackValues.NINE,
            AttackValues.NINE, AttackValues.NINE);
    Card mediumCard = new Card("MediumCard",
            AttackValues.FIVE, AttackValues.FIVE,
            AttackValues.FIVE, AttackValues.FIVE);
    Card weakCard = new Card("WeakCard",
            AttackValues.ONE, AttackValues.ONE,
            AttackValues.ONE, AttackValues.ONE);
    mockHand = Arrays.asList(weakCard, mediumCard, strongCard);
    hardToFlipStrategy = new HardToFlip();
  }

  @Test
  public void testOptimalCardSelectsHighestAttackValues() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, false, 3, 3);
    Card selected = hardToFlipStrategy.optimalCard(model, Player.RED);
    Assert.assertEquals("Should select card with highest attack values",
            strongCard, selected);
  }

  @Test
  public void testOptimalPositionSelectsLeastExposed() {
    // Create a custom mock model where center position has more exposed sides
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, false, 3, 3) {
      @Override
      public CellType getCellType(int row, int col) {
        // Make corners holes to force selection of edge positions
        if ((row == 0 || row == 2) && (col == 0 || col == 2)) {
          return CellType.HOLE;
        }
        return CellType.CARD_CELL;
      }
    };

    Position pos = hardToFlipStrategy.optimalPosition(model, strongCard, Player.RED);
    Assert.assertNotNull("Should find a valid position", pos);

    // Should prefer edge position over center due to less exposure
    Assert.assertTrue("Should select position with fewer exposed sides",
            (pos.getRow() == 0 && pos.getCol() == 1) ||
                    (pos.getRow() == 1 && pos.getCol() == 0) ||
                    (pos.getRow() == 1 && pos.getCol() == 2) ||
                    (pos.getRow() == 2 && pos.getCol() == 1));
  }

  @Test
  public void testBreaksTiesWithUppermostLeftmost() {
    // Create a mock model where all positions are equally exposed
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, false, 3, 3) {
      @Override
      public CellType getCellType(int row, int col) {
        return CellType.CARD_CELL;
      }
    };

    Position pos = hardToFlipStrategy.optimalPosition(model, strongCard, Player.RED);
    Assert.assertNotNull("Should find a valid position", pos);
    Assert.assertEquals("Should select uppermost position", 0, pos.getRow());
    Assert.assertEquals("Should select leftmost position", 0, pos.getCol());
  }

  @Test
  public void testHandlesEmptyHand() {
    ReadOnlyTTModel model = new MockThreeTriosModel(Arrays.asList(), false, 3, 3);
    Card selected = hardToFlipStrategy.optimalCard(model, Player.RED);
    Assert.assertNull("Should return null for empty hand", selected);
  }

  @Test
  public void testGameOverException() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, true, 3, 3);
    Assert.assertThrows("Cannot get optimal position when game over",
            IllegalStateException.class, () ->
                    hardToFlipStrategy.optimalPosition(model, strongCard, Player.RED));
  }

  @Test
  public void testNullModelException() {
    Assert.assertThrows("model cannot be null",
            NullPointerException.class, () ->
                    hardToFlipStrategy.optimalCard(null, Player.RED));
  }

  @Test
  public void testNullPlayerException() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, false, 3, 3);
    Assert.assertThrows("player cannot be null",
            NullPointerException.class, () ->
                    hardToFlipStrategy.optimalCard(model, null));
  }

  @Test
  public void testNullCardException() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, false, 3, 3);
    Assert.assertThrows("card cannot be null",
            NullPointerException.class, () ->
                    hardToFlipStrategy.optimalPosition(model, null, Player.RED));
  }
}
