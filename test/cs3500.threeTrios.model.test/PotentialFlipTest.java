import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * Tests for the potential flip calculations and model copying functionality in Three Trios.
 * Verifies that the game correctly identifies potential card flips, handles invalid inputs,
 * and maintains proper state independence when copying the game model.
 */
public class PotentialFlipTest {
  private ThreeTriosGridModel model;

  @Before
  public void setUp() {
    model = new ThreeTriosGridModel();
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
  }

  @Test
  public void testGetPotentialFlipsNoAdjacent() {
    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card card = redHand.get(0);
    int flips = model.getPotentialFlips(card, 0, 0);
    Assert.assertEquals(0, flips);
  }

  @Test
  public void testGetPotentialFlipsNullCard() {
    Assert.assertThrows("cannot be null",
            IllegalArgumentException.class, () ->
                    model.getPotentialFlips(null, 0, 0));
  }

  @Test
  public void testGetPotentialFlipsInvalidPosition() {
    List<Card> redHand = model.getPlayerHand(Player.RED);
    Assert.assertThrows("invalid position",
            IllegalArgumentException.class, () ->
                    model.getPotentialFlips(redHand.get(0), -1, 0));
  }

  @Test
  public void testCopyBasicState() {
    ReadOnlyTTModel copy = model.getCopy();
    Assert.assertEquals(model.getRows(), copy.getRows());
    Assert.assertEquals(model.getCols(), copy.getCols());
    Assert.assertEquals(model.getCurrentPlayer(), copy.getCurrentPlayer());
    Assert.assertEquals(model.isGameStarted(), copy.isGameStarted());
    Assert.assertEquals(model.isGameOver(), copy.isGameOver());
  }

  @Test
  public void testCopyPlayerHands() {
    ReadOnlyTTModel copy = model.getCopy();
    Assert.assertEquals(model.getPlayerHand(Player.RED).size(),
            copy.getPlayerHand(Player.RED).size());
    Assert.assertEquals(model.getPlayerHand(Player.BLUE).size(),
            copy.getPlayerHand(Player.BLUE).size());
    List<Card> originalRedHand = model.getPlayerHand(Player.RED);
    List<Card> copyRedHand = copy.getPlayerHand(Player.RED);
    for (int i = 0; i < originalRedHand.size(); i++) {
      Assert.assertNotEquals(originalRedHand.get(i), copyRedHand.get(i));
      Assert.assertEquals(originalRedHand.get(i).getName(), copyRedHand.get(i).getName());
    }
  }

  @Test
  public void testCopyIndependence() {
    ReadOnlyTTModel copy = model.getCopy();
    List<Card> redHand = model.getPlayerHand(Player.RED);
    model.playCardToCardCell(redHand.get(0), 0, 0);
    Assert.assertNotEquals(model.getCell(0, 0).isOccupied(),
            copy.getCell(0, 0).isOccupied());
    Assert.assertNotEquals(model.getCurrentPlayer(), copy.getCurrentPlayer());
  }

  @Test
  public void testCopyGrid() {
    ReadOnlyTTModel copy = model.getCopy();
    Assert.assertEquals(model.getRows(), copy.getRows());
    Assert.assertEquals(model.getCols(), copy.getCols());

    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        Assert.assertEquals(model.getCellType(row, col), copy.getCellType(row, col));
        Cell originalCell = model.getCell(row, col);
        Cell copyCell = copy.getCell(row, col);
        Assert.assertEquals(originalCell.isOccupied(), copyCell.isOccupied());

        if (originalCell.isOccupied()) {
          Assert.assertNotEquals(originalCell.getCard(), copyCell.getCard());
          Assert.assertEquals(originalCell.getCard().getName(),
                  copyCell.getCard().getName());
          Assert.assertEquals(originalCell.getOwner(), copyCell.getOwner());
        }
      }
    }
  }

  @Test
  public void testCopyAfterMoves() {
    List<Card> redHand = model.getPlayerHand(Player.RED);
    model.playCardToCardCell(redHand.get(0), 0, 0);
    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
    model.playCardToCardCell(blueHand.get(0), 1, 0);
    ReadOnlyTTModel copy = model.getCopy();
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        Cell originalCell = model.getCell(row, col);
        Cell copyCell = copy.getCell(row, col);
        if (originalCell.isOccupied()) {
          Assert.assertEquals(originalCell.getOwner(), copyCell.getOwner());
        }
      }
    }
  }

  @Test
  public void testGetLegalMovesFullBoard() {
    List<Card> redHand = model.getPlayerHand(Player.RED);
    List<Card> blueHand = model.getPlayerHand(Player.BLUE);

    model.playCardToCardCell(redHand.get(0), 0, 0);
    model.playCardToCardCell(blueHand.get(0), 1, 0);
    model.playCardToCardCell(redHand.get(0), 1, 1);
    model.playCardToCardCell(blueHand.get(0), 1, 2);
    model.playCardToCardCell(redHand.get(0), 2, 2);
    List<Position> legalMoves = model.getLegalMoves();
    Assert.assertTrue(legalMoves.isEmpty());
  }

  @Test
  public void testGetLegalMovesGameNotStarted() {
    ThreeTriosGridModel newModel = new ThreeTriosGridModel();
    Assert.assertThrows("game not started legal moves",
            IllegalStateException.class, () -> newModel.getLegalMoves());
  }

  @Test
  public void testGetLegalMovesWithHoles() {
    model = new ThreeTriosGridModel();
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/boardWithHoleAllCCCanReach.config");

    List<Position> legalMoves = model.getLegalMoves();
    for (Position pos : legalMoves) {
      Cell cell = model.getCell(pos.getRow(), pos.getCol());
      Assert.assertTrue(cell instanceof CardCell);
      Assert.assertFalse(cell.isOccupied());
    }
  }

  @Test
  public void testGetLegalMovesMaintainsIndependence() {
    List<Position> moves1 = model.getLegalMoves();
    List<Card> redHand = model.getPlayerHand(Player.RED);
    model.playCardToCardCell(redHand.get(0), 0, 0);

    List<Position> moves2 = model.getLegalMoves();
    Assert.assertEquals(5, moves1.size());
    Assert.assertEquals(4, moves2.size());
  }
}