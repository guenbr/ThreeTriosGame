package cs3500.threetrios.controller;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.view.ViewListener;

/**
 * Common interface for both human and machine players in the game.
 * This interface defines the functionality that all player types
 * must implement. It handles player actions, turn management,
 * and player type identification.
 */
public interface ThreeTriosPlayer {

  /**
   * Adds a listener for this player's actions.
   * Both human and computer players must be capable of emitting events to notify
   * the controller.
   *
   * @param listener the view listener to be added
   * @throws IllegalArgumentException if the listener is null
   */
  void addPlayerActionListener(ViewListener listener);

  /**
   * Performs an action on the game model based on the player's type.
   * For human players, waits for user input.
   * For computer players, executes automatically based on the
   * given strategy.
   *
   * @param model the current state of the game
   * @throws IllegalArgumentException if the model is null
   * @throws IllegalStateException   if the game is over, or it's not
   *                                  this player's turn.
   */
  void performModelAction(ReadOnlyTTModel model);

  /**
   * Gets the player type red/blue associated with the player.
   *
   * @return the Player enum value representing this player's type
   */
  Player getTurn();

  /**
   * Determines whether this player is human or machine.
   *
   * @return true if this is a human player, false if it's a computer player.
   */
  boolean isHuman();
}
