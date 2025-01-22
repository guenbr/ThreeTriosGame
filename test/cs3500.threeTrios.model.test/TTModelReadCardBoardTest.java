import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.HoleCell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * The purpose of this class TTModelReadCardBoardTest, is to ensure
 * the validity of the implementation. This checks if the exceptions are caught
 * by the edge cases that the implementation checks for. This test class also
 * checks for correct implementation behavior based on assignment 5 requirements
 */
public class TTModelReadCardBoardTest {
  private ThreeTriosGridModel model;

  @Before
  public void setUp() {
    model = new ThreeTriosGridModel();
  }

  @Test
  public void testIsGameStarted() {
    Assert.assertFalse(model.isGameStarted());

    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Assert.assertTrue(model.isGameStarted());
  }

  @Test
  public void testGetColsAndGetRows() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Assert.assertEquals(3, model.getCols());
    Assert.assertEquals(3, model.getRows());
  }

  @Test
  public void testGetCellValidCoordinates() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Cell cell = model.getCell(0, 0);
    Assert.assertNotNull(cell);
    Assert.assertTrue(cell instanceof CardCell);

    cell = model.getCell(0, 2);
    Assert.assertNotNull(cell);
    Assert.assertTrue(cell instanceof HoleCell);
  }

  @Test
  public void testGetCurrentPlayerAfterGameStart() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Assert.assertEquals(Player.RED, model.getCurrentPlayer());
  }

  @Test
  public void testPlayCardToValidCell() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card cardToPlay = redHand.get(0);

    // Play the card to a valid cell (e.g., (0,0) which is a CardCell)
    model.playCardToCardCell(cardToPlay, 0, 0);

    // Verify the card is placed
    Cell cell = model.getCell(0, 0);
    Assert.assertTrue(cell.isOccupied());
    Assert.assertEquals(cardToPlay, cell.getCard());
    Assert.assertEquals(Player.RED, cell.getOwner());  // Check cell owner instead of card owner
    Assert.assertFalse(model.getPlayerHand(Player.RED).contains(cardToPlay));
    Assert.assertEquals(Player.BLUE, model.getCurrentPlayer());
  }

  @Test
  public void testIsGameOverAtStart() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    Assert.assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverAfterAllCardsPlayed() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    List<Card> redHand = model.getPlayerHand(Player.RED);
    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
    model.playCardToCardCell(redHand.get(0), 0, 0);
    model.playCardToCardCell(blueHand.get(0), 1,0);
    model.playCardToCardCell(redHand.get(0), 1, 1);
    model.playCardToCardCell(blueHand.get(0), 1, 2);
    model.playCardToCardCell(redHand.get(0), 2, 2);
    Assert.assertTrue(model.isGameOver());
  }

  @Test
  public void testGetCellType() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    Assert.assertEquals(CellType.CARD_CELL, model.getCellType(0, 0));
    Assert.assertEquals(CellType.HOLE, model.getCellType(0, 2));
  }

  @Test
  public void testGetInvalidCellType() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    Assert.assertThrows("Invalid coordinates",
            IllegalArgumentException.class, () ->
                    model.getCellType(-1, 0));
  }

  /**
   * Our implementation makes it so the hands are randomized every time.
   * Thus, when these tests run, it might return false since it randomized
   * the wrong hand.
   */
  @Test
  public void testGetCardByName() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Card card = model.getCardByName("CorruptKing");
    Assert.assertEquals("CorruptKing", card.getName());

    Assert.assertThrows("Card is not contained in deck",
            IllegalArgumentException.class, () ->
                    model.getCardByName("notContainedInHand"));
  }

  @Test
  public void testEnsureHandSizeIsValid() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    List<Card> redHand = model.getPlayerHand(Player.RED);
    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
    // board.config has 5 cardCells
    int cellNumber = 5;
    int expectedHandSize = (cellNumber + 1) / 2;
    Assert.assertEquals(expectedHandSize, redHand.size());
    Assert.assertEquals(expectedHandSize, blueHand.size());
  }

  @Test
  public void testGetAdjacentPosition() {
    Position position = new Position(2, 2);

    Position north = model.getAdjacentPosition(position, Direction.NORTH);
    Assert.assertEquals(1, north.getRow());
    Assert.assertEquals(2, north.getCol());

    Position south = model.getAdjacentPosition(position, Direction.SOUTH);
    Assert.assertEquals(3, south.getRow());
    Assert.assertEquals(2, south.getCol());

    Position east = model.getAdjacentPosition(position, Direction.EAST);
    Assert.assertEquals(2, east.getRow());
    Assert.assertEquals(3, east.getCol());

    Position west = model.getAdjacentPosition(position, Direction.WEST);
    Assert.assertEquals(2, west.getRow());
    Assert.assertEquals(1, west.getCol());
  }

  /**
   * Our implementation makes it so the hands are randomized every time.
   * Thus, when these tests run, it might return false since it randomized
   * the wrong hand.
   */
  @Test
  public void testBattlePhase() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card cardToPlayRed = redHand.get(0);

    model.playCardToCardCell(cardToPlayRed, 1, 1);
    Cell redCell = model.getCell(1,1);
    Assert.assertEquals(Player.RED, redCell.getOwner());

    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
    Card cardToPlayBlue = blueHand.get(0);

    model.playCardToCardCell(cardToPlayBlue, 1, 2);

    Cell blueCell = model.getCell(1,1);
    Assert.assertTrue(blueCell.isOccupied());
    Assert.assertEquals(Player.BLUE, blueCell.getOwner());
  }

  /**
   * Our implementation makes it so the hands are randomized every time.
   * Thus, when these tests run, it might return false since it randomized
   * the wrong hand.
   */
  @Test
  public void testGetWinner() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    List<Card> redHand = model.getPlayerHand(Player.RED);
    List<Card> blueHand = model.getPlayerHand(Player.BLUE);


    model.playCardToCardCell(redHand.get(0), 0, 0);
    model.playCardToCardCell(blueHand.get(0), 1,0);

    model.playCardToCardCell(redHand.get(0), 1, 1);
    model.playCardToCardCell(blueHand.get(0), 1, 2);

    model.playCardToCardCell(redHand.get(0), 2, 2);

    Assert.assertTrue(model.isGameOver());

    Player winner = model.getWinner();
    Assert.assertEquals(Player.NULL_PLAYER, winner);
  }

  @Test
  public void testGetCurrentPlayerGameNotStarted() {
    Assert.assertThrows("Cannot get player when game has not started",
            IllegalStateException.class, () ->
                    model.getCurrentPlayer());
  }

  @Test
  public void testGetPlayerHandExceptions() {
    Assert.assertThrows("Cannot have null value",
            IllegalArgumentException.class, () ->
                    model.getPlayerHand(null));
    Assert.assertThrows("Cannot have null value",
            IllegalStateException.class, () ->
                    model.getPlayerHand(Player.BLUE));
  }

  @Test
  public void testStartGameWithNullArguments() {
    // DirectoryCards is null
    Assert.assertThrows("startGame args cannot be null",
            IllegalArgumentException.class, () ->
                    model.startGame(null,
                            "docs/cardsWorksWithAll.config",
                            "docs", "docs/board.config"));

    // FilenameCards is null
    Assert.assertThrows("startGame args cannot be null",
            IllegalArgumentException.class, () ->
                    model.startGame("docs", null,
                            "docs", "docs/board.config"));

    // DirectoryBoard is null
    Assert.assertThrows("startGame args cannot be null",
            IllegalArgumentException.class, () ->
                    model.startGame("docs",
                            "docs/cardsWorksWithAll.config",
                            null, "docs/board.config"));

    // FilenameBoard is null
    Assert.assertThrows("startGame args cannot be null",
            IllegalArgumentException.class, () ->
                    model.startGame("docs",
                            "docs/cardsWorksWithAll.config",
                            "docs", null));
  }

  @Test
  public void testStartGameWithInvalidGridConfig() {
    // Provide a grid file that doesn't exist
    Assert.assertThrows("Cannot read Grid.config file",
            IllegalArgumentException.class, () ->
                    model.startGame("docs",
                            "docs/cardsWorksWithAll.config",
                            "docs", "nonexistentGrid.config"));
  }

  @Test
  public void testStartGameWithInvalidCardConfig() {
    // Provide a card file that doesn't exist
    Assert.assertThrows("Cannot read Card.config file",
            IllegalArgumentException.class, () ->
                    model.startGame("docs", "nonexistentCards.config",
                            "docs", "docs/board.config"));
  }

  @Test
  public void testGetCellWithInvalidCoordinates() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCell(-1, 0));

    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCell(0, -1));
    int numRows = model.getRows();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCell(numRows, 0));
    int numCols = model.getCols();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCell(0, numCols));
  }

  @Test
  public void testPlayCardToCardCellInvalidCoordinates() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card card = redHand.get(0);

    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.playCardToCardCell(card, -1, 0));
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.playCardToCardCell(card, 0, -1));
    int numRows = model.getRows();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.playCardToCardCell(card, numRows, 0));
    int numCols = model.getCols();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.playCardToCardCell(card, 0, numCols));
  }

  @Test
  public void testPlayCardToCardCellCannotPlaceCard() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card card = redHand.get(0);
    Assert.assertThrows("Cannot place a card on this cell",
            IllegalArgumentException.class, () ->
                    model.playCardToCardCell(card, 0, 2));
  }

  @Test
  public void testGetWinnerGameNotStarted() {
    Assert.assertThrows("Game has not started, testing helper",
            IllegalStateException.class, () ->
                    model.getWinner());
  }

  @Test
  public void testGetWinnerGameNotOver() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    Assert.assertFalse(model.isGameOver());
    Assert.assertThrows("Game is not over",
            IllegalStateException.class, () ->
                    model.getWinner());
  }

  @Test
  public void testGetCellTypeWithInvalidCoordinates() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    // Negative row index
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCellType(-1, 0));

    // Negative column index
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCellType(0, -1));

    // Row index out of bounds
    int numRows = model.getRows();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCellType(numRows, 0));

    // Column index out of bounds
    int numCols = model.getCols();
    Assert.assertThrows("Invalid cell coordinates",
            IllegalArgumentException.class, () ->
                    model.getCellType(0, numCols));
  }

  @Test
  public void testGetCardByNameCardNotInDeck() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    Assert.assertThrows("Is not contained in deck",
            IllegalArgumentException.class, () ->
                    model.getCardByName("NonExistentCard"));
  }

  @Test
  public void testGetAdjacentPositionWithNullDirection() {
    Position position = new Position(0, 0);

    Assert.assertThrows("Invalid direction",
            NullPointerException.class, () ->
                    model.getAdjacentPosition(position, null));
  }

  @Test
  public void testGetPlayerHandWithInvalidPlayer() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");

    // Assuming Player.NULL_PLAYER is considered invalid
    Assert.assertThrows("Invalid player",
            IllegalArgumentException.class, () ->
                    model.getPlayerHand(Player.NULL_PLAYER));
  }

  @Test
  public void testGetPlayerHandBeforeGameStarted() {
    Assert.assertThrows("Game has not been started",
            IllegalStateException.class, () ->
                    model.getPlayerHand(Player.RED));
  }

  @Test
  public void testIsGameOverBeforeGameStarted() {
    Assert.assertThrows("Game has not started, testing helper",
            IllegalStateException.class, () ->
                    model.isGameOver());
  }

  @Test
  public void testGetCurrentPlayerBeforeGameStarted() {
    Assert.assertThrows("Game has not started, testing helper",
            IllegalStateException.class, () ->
                    model.getCurrentPlayer());
  }

  @Test
  public void testInvalidCardConfig() {
    Assert.assertThrows("Not enough cards to start game",
            IllegalArgumentException.class, () ->
                    model.startGame("docs",
                            "docs/cardsWorksWithOne.config",
                            "docs", "BoardWithNoHoles.config"));
  }
}