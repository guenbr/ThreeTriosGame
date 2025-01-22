package cs3500.threetrios.strategy.mocks;

import cs3500.threetrios.model.AttackValues;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock model to generate the transcript for the corner strategy.
 * It showcases what the methods check and the steps it takes.
 */
public class MockTranscriptModel implements ReadOnlyTTModel {
  private final List<Card> mockHand;
  private final int rows;
  private final int cols;

  /**
   * Constructs the mock model and initializes the variables.
   */
  public MockTranscriptModel() {
    this.rows = 3;
    this.cols = 3;
    this.mockHand = new ArrayList<>();
    mockHand.add(new Card("Card1", AttackValues.SEVEN, AttackValues.THREE,
            AttackValues.NINE, AttackValues.A));
    mockHand.add(new Card("Card2", AttackValues.TWO, AttackValues.EIGHT,
            AttackValues.NINE, AttackValues.NINE));
  }

  @Override
  public Cell getCell(int row, int col) {
    System.out.println(String.format("Checking cell at position (%d, %d)", row, col));
    return new CardCell();
  }

  @Override
  public CellType getCellType(int row, int col) {
    System.out.println(String.format("Getting cell type at position (%d, %d)", row, col));
    return CellType.CARD_CELL;
  }

  @Override
  public boolean isLegalMove(int row, int col) {
    System.out.println(String.format("Checking if move is legal at (%d, %d)", row, col));
    return true;
  }

  @Override
  public List<Card> getPlayerHand(Player player) {
    System.out.println(String.format("Getting hand for player %s", player));
    return mockHand;
  }

  @Override
  public int getPotentialFlips(Card card, int row, int col) {
    System.out.println(String.format("Calculating potential flips for card %s at (%d, %d)",
            card.getName(), row, col));
    return 0;
  }

  @Override
  public Player getCurrentPlayer() {
    return Player.RED;
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public Player getWinner() {
    return null;
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
}
