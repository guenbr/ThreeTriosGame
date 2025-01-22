import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.MachinePlayer;
import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.strategy.Corner;
import cs3500.threetrios.strategy.MaxCardFlip;

/**
 * Tests for both HumanPlayer and MachinePlayer implementations.
 */
public class PlayerTest {
  private MockPlayerModel model;

  private MockViewListener listener;

  private HumanPlayer humanPlayer;

  private MachinePlayer machinePlayer;

  @Before
  public void setup() {
    model = new MockPlayerModel();
    listener = new MockViewListener();
    humanPlayer = new HumanPlayer(Player.RED);
    machinePlayer = new MachinePlayer(Player.RED, model, new Corner());
    Card mockCard1 = new Card("TestCard1", AttackValues.ONE, AttackValues.TWO,
            AttackValues.THREE, AttackValues.FOUR);
    Card mockCard2 = new Card("TestCard2", AttackValues.FIVE, AttackValues.SIX,
            AttackValues.SEVEN, AttackValues.EIGHT);
  }

  @Test
  public void testHumanPlayerConstruction() {
    Assert.assertEquals(Player.RED, humanPlayer.getTurn());
    Assert.assertTrue(humanPlayer.isHuman());
  }

  @Test
  public void testHumanPlayerBlueConstruction() {
    HumanPlayer bluePlayer = new HumanPlayer(Player.BLUE);
    Assert.assertEquals(Player.BLUE, bluePlayer.getTurn());
    Assert.assertTrue(bluePlayer.isHuman());
  }

  @Test
  public void testHumanPlayerNoAutoAction() {
    humanPlayer.addPlayerActionListener(listener);
    humanPlayer.performModelAction(model);
    Assert.assertEquals("", listener.getLog());
  }

  @Test
  public void testHumanPlayerNullListener() {
    Assert.assertThrows("cannot be null",
            NullPointerException.class, () ->
                    humanPlayer.addPlayerActionListener(null));
  }

  @Test
  public void testHumanPlayerViewListenerRegistration() {
    humanPlayer.addPlayerActionListener(listener);
    humanPlayer.performModelAction(model);
    Assert.assertEquals("", listener.getLog());
  }

  @Test
  public void testMachinePlayerConstruction() {
    Assert.assertEquals(Player.RED, machinePlayer.getTurn());
    Assert.assertFalse(machinePlayer.isHuman());
  }

  @Test
  public void testMachinePlayerWrongTurn() {
    machinePlayer.addPlayerActionListener(listener);
    model.setCurrentPlayer(Player.BLUE);
    machinePlayer.performModelAction(model);
    Assert.assertTrue(model.getLog().toString().contains("getCurrentPlayer"));
    Assert.assertEquals("", listener.getLog());
  }

  @Test
  public void testMachinePlayerGameOver() {
    machinePlayer.addPlayerActionListener(listener);
    model.setGameOver(true);
    machinePlayer.performModelAction(model);
    Assert.assertTrue(model.getLog().toString().contains("isGameOver"));
    Assert.assertEquals("", listener.getLog());
  }

  @Test
  public void testMachinePlayerCorrectTurn() {
    machinePlayer.addPlayerActionListener(listener);
    model.setCurrentPlayer(Player.RED);
    machinePlayer.performModelAction(model);
    String log = model.getLog().toString();
    Assert.assertTrue(log.contains("getCurrentPlayer"));
    Assert.assertTrue(log.contains("getLegalMoves"));
  }

  @Test
  public void testDifferentStrategies() {
    MachinePlayer cornerPlayer = new MachinePlayer(Player.RED, model, new Corner());
    MachinePlayer maxFlipPlayer = new MachinePlayer(Player.RED, model, new MaxCardFlip());
    MockViewListener cornerListener = new MockViewListener();
    MockViewListener maxFlipListener = new MockViewListener();
    cornerPlayer.addPlayerActionListener(cornerListener);
    maxFlipPlayer.addPlayerActionListener(maxFlipListener);
    model.setCurrentPlayer(Player.RED);
    model.clearLog();
    cornerPlayer.performModelAction(model);
    String cornerLog = model.getLog().toString();
    Assert.assertTrue(cornerLog.contains("getLegalMoves"));
    model.clearLog();
    maxFlipPlayer.performModelAction(model);
    String maxFlipLog = model.getLog().toString();
    Assert.assertTrue(maxFlipLog.contains("getLegalMoves"));
    Assert.assertTrue(maxFlipLog.contains("getPotentialFlips"));
  }

  @Test
  public void testMachinePlayerMultipleMoves() {
    machinePlayer.addPlayerActionListener(listener);
    model.setCurrentPlayer(Player.RED);
    machinePlayer.performModelAction(model);
    Assert.assertTrue(model.getLog().toString().contains("getLegalMoves"));
    model.clearLog();
    machinePlayer.performModelAction(model);
    Assert.assertTrue(model.getLog().toString().contains("getLegalMoves"));
  }

  @Test
  public void testMachinePlayerNullModel() {
    Assert.assertThrows("Model cannot be null",
            NullPointerException.class, () ->
                    new MachinePlayer(Player.RED, null, new Corner()));
  }

  @Test
  public void testMachinePlayerNullStrategy() {
    Assert.assertThrows("Strategy cannot be null",
            NullPointerException.class, () ->
                    new MachinePlayer(Player.RED, model, null));
  }

  @Test
  public void testMachinePlayerNullPlayer() {
    Assert.assertThrows("Player type cannot be null",
            NullPointerException.class, () ->
                    new MachinePlayer(null, model, new Corner()));
  }

  @Test
  public void testHumanPlayerNullPlayer() {
    Assert.assertThrows("Player type cannot be null",
            NullPointerException.class, () ->
                    new HumanPlayer(null));
  }

  @Test
  public void testHumanPlayerGameOver() {
    model.setGameOver(true);
    Assert.assertThrows("Cannot make move when game is over",
            IllegalStateException.class, () ->
                    humanPlayer.performModelAction(model));
  }

  @Test
  public void testHumanPlayerWrongTurn() {
    model.setCurrentPlayer(Player.BLUE);
    Assert.assertThrows("Cannot make move during opponent's turn",
            IllegalStateException.class, () ->
                    humanPlayer.performModelAction(model));
  }

  @Test
  public void testMachinePlayerViewListenerRegistration() {
    MockViewListener newListener = new MockViewListener();
    machinePlayer.addPlayerActionListener(newListener);
    model.setCurrentPlayer(Player.RED);
    machinePlayer.performModelAction(model);
    Assert.assertTrue(model.getLog().toString().contains("getLegalMoves"));
  }
}