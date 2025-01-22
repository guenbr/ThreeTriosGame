import java.util.ArrayList;
import java.util.List;

import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.controller.GameStatusListener;
import cs3500.threetrios.model.GameGridModel;

/**
 * Mock model for testing the controller. Tracks method calls and simulates game state.
 */
public class MockPlayerModel implements GameGridModel {
  private final StringBuilder log;

  private Player currentPlayer;

  private boolean gameOver;

  private final List<Card> redHand;

  private final List<Card> blueHand;

  private final int rows = 3;

  private final int cols = 3;

  private final List<GameStatusListener> listeners;

  private final Cell[][] grid;

  /**
   * This is the constructor for the mock player model.
   * It initializes the fields respectively.
   */
  public MockPlayerModel() {
    this.log = new StringBuilder();
    this.currentPlayer = Player.RED;
    this.gameOver = false;
    this.listeners = new ArrayList<>();
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    redHand.add(new Card("RedCard1", AttackValues.ONE, AttackValues.TWO,
            AttackValues.THREE, AttackValues.FOUR));
    redHand.add(new Card("RedCard2", AttackValues.FIVE, AttackValues.SIX,
            AttackValues.SEVEN, AttackValues.EIGHT));
    blueHand.add(new Card("BlueCard1", AttackValues.TWO, AttackValues.THREE,
            AttackValues.FOUR, AttackValues.FIVE));
    blueHand.add(new Card("BlueCard2", AttackValues.SIX, AttackValues.SEVEN,
            AttackValues.EIGHT, AttackValues.NINE));
    grid = new Cell[rows][cols];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        grid[i][j] = new CardCell();
      }
    }
    grid[0][2] = null;
  }

  @Override
  public void createGrid(List<String> gridLines, List<Card> cards) {
    log.append("createGrid called\n");
  }

  @Override
  public void playCardToCardCell(Card card, int row, int col) {
    log.append(String.format("playCardToCardCell called: card=%s, row=%d, col=%d\n",
            card.getName(), row, col));

    if (!isLegalMove(row, col)) {
      throw new IllegalArgumentException("Invalid move position");
    }

    if (grid[row][col].isOccupied()) {
      throw new IllegalArgumentException("Cell already occupied");
    }

    grid[row][col].placeCard(card, currentPlayer);
    if (currentPlayer == Player.RED) {
      redHand.remove(card);
    } else {
      blueHand.remove(card);
    }
    currentPlayer = (currentPlayer == Player.RED) ? Player.BLUE : Player.RED;
    for (GameStatusListener listener : listeners) {
      listener.notifyTurnChange(currentPlayer);
    }

    checkGameOver();
  }

  @Override
  public void startGame(String directoryCards, String filenameCards,
                        String directoryBoard, String filenameBoard) {
    log.append("startGame called\n");
    for (GameStatusListener listener : listeners) {
      listener.notifyTurnChange(currentPlayer);
    }
  }

  @Override
  public void addModelStatusListener(GameStatusListener listener) {
    log.append("addModelStatusListener called\n");
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null");
    }
    listeners.add(listener);
  }

  @Override
  public Cell getCell(int row, int col) {
    log.append(String.format("getCell called: row=%d, col=%d\n", row, col));
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    return grid[row][col];
  }

  @Override
  public Player getCurrentPlayer() {
    log.append("getCurrentPlayer called\n");
    return currentPlayer;
  }

  @Override
  public boolean isGameOver() {
    log.append("isGameOver called\n");
    return gameOver;
  }

  @Override
  public Player getWinner() {
    log.append("getWinner called\n");
    if (!gameOver) {
      throw new IllegalStateException("Game is not over");
    }
    return currentPlayer;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    log.append(String.format("getPlayerHand called for %s\n", player));
    if (player == Player.RED) {
      return new ArrayList<>(redHand);
    } else if (player == Player.BLUE) {
      return new ArrayList<>(blueHand);
    }
    throw new IllegalArgumentException("Invalid player");
  }

  @Override
  public List<Card> getDeck() {
    log.append("getDeck called\n");
    List<Card> deck = new ArrayList<>(redHand);
    deck.addAll(blueHand);
    return deck;
  }

  @Override
  public int getDeckSize() {
    log.append("getDeckSize called\n");
    return redHand.size() + blueHand.size();
  }

  @Override
  public Card getCardByName(String name) {
    log.append(String.format("getCardByName called: %s\n", name));
    for (Card card : redHand) {
      if (card.getName().equals(name)) {
        return card;
      }
    }
    for (Card card : blueHand) {
      if (card.getName().equals(name)) {
        return card;
      }
    }
    throw new IllegalArgumentException("Card not found: " + name);
  }

  @Override
  public boolean isGameStarted() {
    log.append("isGameStarted called\n");
    return true;
  }

  @Override
  public int getPlayerScore(Player player) {
    log.append(String.format("getPlayerScore called for %s\n", player));
    if (player == Player.RED) {
      return redHand.size();
    } else if (player == Player.BLUE) {
      return blueHand.size();
    }
    throw new IllegalArgumentException("Invalid player");
  }

  @Override
  public CellType getCellType(int row, int col) {
    log.append(String.format("getCellType called: row=%d, col=%d\n", row, col));
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      throw new IllegalArgumentException("Invalid cell coordinates");
    }
    return grid[row][col] == null ? CellType.HOLE : CellType.CARD_CELL;
  }

  @Override
  public int getRows() {
    log.append("getRows called\n");
    return rows;
  }

  @Override
  public int getCols() {
    log.append("getCols called\n");
    return cols;
  }

  @Override
  public boolean isLegalMove(int row, int col) {
    log.append(String.format("isLegalMove called: row=%d, col=%d\n", row, col));
    if (row < 0 || row >= rows || col < 0 || col >= cols) {
      return false;
    }
    return grid[row][col] != null && !grid[row][col].isOccupied();
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    log.append(String.format("getPotentialFlips called: card=%s, row=%d, col=%d\n",
            card.getName(), row, col));
    return 0;
  }

  @Override
  public List<Position> getLegalMoves() {
    log.append("getLegalMoves called\n");
    List<Position> moves = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (isLegalMove(i, j)) {
          moves.add(new Position(i, j));
        }
      }
    }
    return moves;
  }

  @Override
  public ReadOnlyTTModel getCopy() {
    log.append("getCopy called\n");
    return this;
  }

  public String getLog() {
    return log.toString();
  }

  public void clearLog() {
    log.setLength(0);
  }

  private void checkGameOver() {
    int occupiedCells = 0;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        if (grid[i][j] != null && grid[i][j].isOccupied()) {
          occupiedCells++;
        }
      }
    }
    if (occupiedCells == 5) {
      gameOver = true;
      for (GameStatusListener listener : listeners) {
        listener.notifyGameOver(getWinner(), getPlayerScore(getWinner()));
      }
    }
  }

  public void setCurrentPlayer(Player player) {
    this.currentPlayer = player;
  }

  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }
}