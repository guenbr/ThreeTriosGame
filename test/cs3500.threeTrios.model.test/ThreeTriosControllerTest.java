import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import cs3500.threetrios.controller.ThreeTriosController;
import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.MachinePlayer;
import cs3500.threetrios.controller.ThreeTriosPlayer;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosGridModel;
import cs3500.threetrios.strategy.Corner;
import cs3500.threetrios.view.ThreeTriosGUI;

/**
 * Tests controller's handling of turns, moves, game state changes, and error conditions.
 */
public class ThreeTriosControllerTest {
  private ThreeTriosGridModel model;

  private ThreeTriosGUI view;

  private ThreeTriosPlayer humanPlayer;

  private ThreeTriosController humanController;

  private ThreeTriosController machineController;

  private List<Card> redHand;

  private List<Card> blueHand;

  private ThreeTriosGUI redView;

  @Before
  public void setUp() {
    model = new ThreeTriosGridModel();
    model.startGame("docs", "cardsWorksWithAll.config",
            "docs", "board.config");
    redView = new ThreeTriosGUI(model);
    ThreeTriosGUI blueView = new ThreeTriosGUI(model);
    humanPlayer = new HumanPlayer(Player.RED);
    ThreeTriosPlayer machinePlayer = new MachinePlayer(Player.BLUE, model, new Corner());
    humanController = new ThreeTriosController(model, humanPlayer, redView, Player.RED);
    machineController = new ThreeTriosController(model, machinePlayer, blueView, Player.BLUE);
    redHand = model.getPlayerHand(Player.RED);
    blueHand = model.getPlayerHand(Player.BLUE);
  }

  @Test
  public void testConstructorNullModel() {
    Assert.assertThrows("Model cannot be null",
            NullPointerException.class, () ->
                    new ThreeTriosController(null, humanPlayer, redView, Player.RED));
  }

  @Test
  public void testConstructorNullPlayer() {
    Assert.assertThrows("Player cannot be null",
            NullPointerException.class, () ->
                    new ThreeTriosController(model, null, redView, Player.RED));
  }

  @Test
  public void testConstructorNullView() {
    Assert.assertThrows("View cannot be null",
            NullPointerException.class, () ->
                    new ThreeTriosController(model, humanPlayer, null, Player.RED));
  }

  @Test
  public void testConstructorNullPlayerType() {
    Assert.assertThrows("Player type cannot be null",
            NullPointerException.class, () ->
                    new ThreeTriosController(model, humanPlayer, redView, null));
  }

  @Test
  public void testInitialGameState() {
    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
    Assert.assertFalse(model.isGameOver());
    Assert.assertEquals(3, redHand.size());
    Assert.assertEquals(3, blueHand.size());
  }

  @Test
  public void testGridClickBeforeCardSelection() {
    humanController.gridCellClickedListener(0, 0);
    Assert.assertFalse(model.getCell(0, 0).isOccupied());
  }

  @Test
  public void testCardSelectionWrongPlayer() {
    machineController.cardClickedListener(Player.BLUE, 0);
    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testValidMove() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 0);
    Assert.assertTrue(model.getCell(0, 0).isOccupied());
    Assert.assertEquals(Player.RED, model.getCell(0, 0).getOwner());
    Assert.assertEquals(Player.BLUE, model.getCurrentPlayer());
  }

  @Test
  public void testPlayToOccupiedCell() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 0);
    machineController.cardClickedListener(Player.BLUE, 0);
    machineController.gridCellClickedListener(0, 0);
    Assert.assertEquals(Player.RED, model.getCell(0, 0).getOwner());
  }

  @Test
  public void testPlayToHoleCell() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 2);
    Assert.assertFalse(model.getCell(0, 2).isOccupied());
  }

  @Test
  public void testGameOverNotification() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 0);
    machineController.cardClickedListener(Player.BLUE, 0);
    machineController.gridCellClickedListener(1, 0);
    humanController.cardClickedListener(Player.RED, 1);
    humanController.gridCellClickedListener(1, 1);
    machineController.cardClickedListener(Player.BLUE, 1);
    machineController.gridCellClickedListener(1, 2);
    humanController.cardClickedListener(Player.RED, 2);
    humanController.gridCellClickedListener(2, 2);

    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testInvalidMoveOutOfBounds() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(-1, -1);
    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testSelectingOpponentCard() {
    humanController.cardClickedListener(Player.BLUE, 0);
    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testMachinePlayerAutoMove() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 0);
    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testMultipleCardSelections() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.cardClickedListener(Player.RED, 1);
    humanController.gridCellClickedListener(0, 0);
    Assert.assertTrue(model.getCell(0, 0).isOccupied());
    Assert.assertEquals(redHand.get(1), model.getCell(0, 0).getCard());
  }

  @Test
  public void testInvalidCardIndex() {
    humanController.cardClickedListener(Player.RED, -1);
    humanController.gridCellClickedListener(0, 0);
    Assert.assertFalse(model.getCell(0, 0).isOccupied());
  }

  @Test
  public void testPlayAfterGameOver() {
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 0);
    machineController.cardClickedListener(Player.BLUE, 0);
    machineController.gridCellClickedListener(1, 0);
    humanController.cardClickedListener(Player.RED, 1);
    humanController.gridCellClickedListener(1, 1);
    machineController.cardClickedListener(Player.BLUE, 1);
    machineController.gridCellClickedListener(1, 2);
    humanController.cardClickedListener(Player.RED, 2);
    humanController.gridCellClickedListener(2, 2);
    humanController.cardClickedListener(Player.RED, 0);
    humanController.gridCellClickedListener(0, 1);
    Assert.assertFalse(model.getCell(0, 1).isOccupied());
  }
}