import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.strategy.MaxCardFlip;
import cs3500.threetrios.strategy.mocks.MockDefaultFlipModel;
import cs3500.threetrios.strategy.mocks.MockFlipEqualModel;
import cs3500.threetrios.strategy.mocks.MockMultipleMaxFlipsModel;
import cs3500.threetrios.strategy.mocks.MockThreeTriosModel;
import cs3500.threetrios.strategy.StrategicThreeTrios;

/**
 * Test suite for MaxCardFlip strategy in the Three Trios game.
 */

public class TestMaxFlipStrategy {
  private Card mockCard1;
  private Card mockCard2;
  private Card mockCard3;
  private List<Card> mockHand;
  private StrategicThreeTrios maxFlipStrategy;

  @Before
  public void setUp() {
    mockCard1 = new Card("Card1", AttackValues.ONE, AttackValues.ONE,
            AttackValues.ONE, AttackValues.ONE);
    mockCard2 = new Card("Card2", AttackValues.THREE, AttackValues.THREE,
            AttackValues.THREE, AttackValues.THREE);
    mockCard3 = new Card("Card3", AttackValues.FIVE, AttackValues.FIVE,
            AttackValues.FIVE, AttackValues.FIVE);
    mockHand = Arrays.asList(mockCard1, mockCard2, mockCard3);
    maxFlipStrategy = new MaxCardFlip();
  }

  @Test
  public void testMaxFlipFindsHighestFlip() {
    ReadOnlyTTModel model = new MockDefaultFlipModel(mockHand, mockCard3);
    Position pos = maxFlipStrategy.optimalPosition(model, mockCard3, Player.RED);
    Assert.assertNotNull(pos);
    Assert.assertEquals(1, pos.getRow());
    Assert.assertEquals(1, pos.getCol());
  }

  @Test
  public void testMaxFlipSelectsBestCard() {
    ReadOnlyTTModel model = new MockDefaultFlipModel(mockHand, mockCard3);
    Card selected = maxFlipStrategy.optimalCard(model, Player.RED);
    Assert.assertNotNull(selected);
    Assert.assertEquals(mockCard3, selected);
  }

  @Test
  public void testMaxFlipBreaksTiesWithPosition() {
    ReadOnlyTTModel model = new MockFlipEqualModel(mockHand);
    Position pos = maxFlipStrategy.optimalPosition(model, mockCard1, Player.RED);
    Assert.assertEquals(0, pos.getRow());
    Assert.assertEquals(0, pos.getCol());
  }

  @Test
  public void testEmptyHand() {
    ReadOnlyTTModel model = new MockDefaultFlipModel(new ArrayList<>(), null);
    Card selected = maxFlipStrategy.optimalCard(model, Player.RED);
    Assert.assertNull(selected);
  }

  @Test
  public void testMultipleMaxFlipPositions() {
    ReadOnlyTTModel model = new MockMultipleMaxFlipsModel(mockHand, mockCard2);
    Position pos = maxFlipStrategy.optimalPosition(model, mockCard2, Player.RED);
    Assert.assertEquals(0, pos.getRow());
    Assert.assertEquals(1, pos.getCol());
  }

  @Test
  public void testNullModel() {
    Assert.assertThrows("model cannot be null",
            NullPointerException.class, () ->
                    maxFlipStrategy.optimalCard(null, Player.RED));
  }

  @Test
  public void testNullPlayer() {
    ReadOnlyTTModel model = new MockDefaultFlipModel(mockHand, mockCard2);
    Assert.assertThrows("player cannot be null",
            NullPointerException.class, () ->
                    maxFlipStrategy.optimalCard(model, null));
  }

  @Test
  public void testNullCard() {
    ReadOnlyTTModel model = new MockDefaultFlipModel(mockHand, mockCard2);
    Assert.assertThrows("card cannot be null",
            NullPointerException.class, () ->
                    maxFlipStrategy.optimalPosition(model, null, Player.RED));
  }

  @Test
  public void testGameOverCheck() {
    ReadOnlyTTModel model = new MockThreeTriosModel(mockHand, true, 3, 3);
    Assert.assertThrows("game not started",
            IllegalStateException.class, () ->
                    maxFlipStrategy.optimalPosition(model, mockCard1, Player.RED));
  }
}