package cs3500.threetrios.model;

import java.util.List;

/**
 * This interface is getters for the model. This returns the
 * state of the game.
 */
public interface ReadOnlyTTModel {
  /**
   * Gets the cell at the specific row and column of the grid.
   * @param row the row of the cell to get
   * @param col the column of the cell to get
   * @return the cell at the specified position
   * @throws IllegalArgumentException if row or col are invalid coordinates
   */
  Cell getCell(int row, int col);

  /**
   * Gets the current player whose turn it is.
   * @return the current player
   * @throws IllegalStateException if game has not started
   */
  Player getCurrentPlayer();

  /**
   * Gets the hand of cards for the specified player.
   * @param player the player whose hand to get
   * @return list of cards in the player's hand
   * @throws IllegalArgumentException if player is null or invalid
   * @throws IllegalStateException if game has not started
   */
  List<Card> getPlayerHand(Player player);

  /**
   * Checks if the game is over (all card cells are filled).
   * @return true if game is over, false otherwise
   * @throws IllegalStateException if game has not started
   */
  boolean isGameOver();

  /**
   * Gets the winner of the game based on card ownership.
   * @return the winning player, or NULL_PLAYER if tie
   * @throws IllegalStateException if game is not over or has not started
   */
  Player getWinner();

  /**
   * Gets all cards in the deck.
   * @return list of all cards in the deck
   */
  List<Card> getDeck();

  /**
   * Gets the current size of the deck.
   * @return number of cards in the deck
   */
  int getDeckSize();

  /**
   * Gets a card from the deck by its name.
   * @param name the name of the card to find
   * @return the card with the specified name
   * @throws IllegalArgumentException if name is not found in deck
   */
  Card getCardByName(String name);

  /**
   * Gets the type of cell at the specified coordinates.
   * @param row the row of the cell
   * @param col the column of the cell
   * @return the CellType (CARD_CELL or HOLE) at the position
   * @throws IllegalArgumentException if coordinates are invalid
   */
  CellType getCellType(int row, int col);

  /**
   * Gets the number of rows in the grid.
   * @return the number of rows
   */
  int getRows();

  /**
   * Gets the number of columns in the grid.
   * @return the number of columns
   */
  int getCols();

  /**
   * Checks if the game has been started.
   * @return true if game is started, false otherwise
   */
  boolean isGameStarted();

  /**
   * Gets the current score for the specified player.
   * Score is the sum of cards owned on grid and in hand.
   * @param player the player whose score to calculate
   * @return the player's current score
   * @throws IllegalArgumentException if player is null
   */
  int getPlayerScore(Player player);

  /**
   * Checks if a move to the specified position is legal for the current player.
   * @param row the row to check
   * @param col the column to check
   * @return true if move is legal, false otherwise
   * @throws IllegalArgumentException if coordinates are invalid
   */
  boolean isLegalMove(int row, int col);

  /**
   * Calculates how many opponent cards would be flipped if the given card
   * was played at the specified position.
   * @param card the card to simulate playing
   * @param row the row to simulate playing at
   * @param col the column to simulate playing at
   * @return number of opponent cards that would be flipped
   * @throws IllegalArgumentException if card is null or coordinates invalid
   */
  int getPotentialFlips(Card card, int row, int col);

  /**
   * Gets all legal moves available to the current player.
   * @return list of valid positions where current player can move
   * @throws IllegalStateException if game has not started
   */
  List<Position> getLegalMoves();

  /**
   * Creates a deep copy of the current game state.
   * @return a new ReadOnlyTTModel containing copy of current state
   */
  ReadOnlyTTModel getCopy();
}
