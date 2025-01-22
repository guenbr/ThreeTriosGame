package cs3500.threetrios.strategy.mocks;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.ReadOnlyTTModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock model for testing strategies.
 */
public class MockThreeTriosModel implements ReadOnlyTTModel {
  private final List<Card> mockHand;
  private final boolean gameOver;
  private final int rows;
  private final int cols;

  /**
   * Creates a basic mock model with default settings.
   */
  public MockThreeTriosModel() {
    this(new ArrayList<>(), false, 3, 3);
  }

  /**
   * Creates a mock model with specified hand and game state.
   * @param mockHand the hand to return for players
   * @param gameOver whether the game is over
   * @param rows number of rows in grid
   * @param cols number of columns in grid
   */
  public MockThreeTriosModel(List<Card> mockHand, boolean gameOver, int rows, int cols) {
    this.mockHand = mockHand;
    this.gameOver = gameOver;
    this.rows = rows;
    this.cols = cols;
  }

  @Override
  public Cell getCell(int row, int col) {
    return null;
  }

  @Override
  public Player getCurrentPlayer() {
    return Player.RED;
  }

  @Override
  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public Player getWinner() {
    return null;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    return mockHand;
  }

  @Override
  public List<Card> getDeck() {
    return new ArrayList<>();
  }

  @Override
  public int getDeckSize() {
    return 0;
  }

  @Override
  public Card getCardByName(String name) {
    return null;
  }

  @Override
  public boolean isGameStarted() {
    return true;
  }

  @Override
  public int getPlayerScore(Player player) {
    return 0;
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    return 0;
  }

  @Override
  public List<Position> getLegalMoves() {
    return new ArrayList<>();
  }

  @Override
  public ReadOnlyTTModel getCopy() {
    return null;
  }

  @Override
  public int getRows() {
    return rows;
  }

  @Override
  public int getCols() {
    return cols;
  }

  @Override
  public CellType getCellType(int row, int col) {
    return CellType.HOLE;
  }

  @Override
  public boolean isLegalMove(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }
}
