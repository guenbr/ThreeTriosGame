package cs3500.threetrios.controller;

import cs3500.threetrios.model.Player;

/**
 * An interface to listen for player actions in the game.
 * It defines methods to handle player events, such as selecting cards
 * from their hand and selecting positions on the game grid.
 */
public interface PlayerActionListener {
  /**
   * Notify when a card is selected from hand.
   * @param player the player selecting the card
   * @param cardIndex index of selected card
   */
  void notifyCardSelected(Player player, int cardIndex);

  /**
   * Notify when a grid position is selected.
   * @param row selected row
   * @param col selected column
   */
  void notifyPositionSelected(int row, int col);
}
