package cs3500.threetrios.controller;

import cs3500.threetrios.model.Player;

/**
 * An interface for listening to game status events.
 * It defines methods to handle changes in the game state, such as
 * turn changes, game over events, and invalid move attempts.
 */
public interface GameStatusListener {
  /**
   * Notify when it's a player's turn.
   * @param currentPlayer the player whose turn it is.
   */
  void notifyTurnChange(Player currentPlayer);

  /**
   * Notifies when game ends.
   * @param winner winning player.
   * @param score final score.
   */
  void notifyGameOver(Player winner, int score);

  /**
   * Notify when an invalid move is attempted.
   * @param message the specific error message.
   */
  void notifyInvalidMove(String message);
}
