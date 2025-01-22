package cs3500.threetrios.view;

import cs3500.threetrios.model.Player;

/**
 * This interface defines the method to listen for interaction like card and grid cell clicks.
 */
public interface ViewListener {

  /**
   * Handled when a player clicks on a card.
   * @param player the player who is clicking the card.
   * @param cardIndex the index of the card being clicked.
   */
  void cardClickedListener(Player player, int cardIndex);

  /**
   * Handled when a player clicks on specific cell.
   * @param row row of the cell being clicked.
   * @param col col of the cell being clicked.
   */
  void gridCellClickedListener(int row, int col);
}