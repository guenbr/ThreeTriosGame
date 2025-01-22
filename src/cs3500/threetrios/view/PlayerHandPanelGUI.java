package cs3500.threetrios.view;

/**
 * The interface defines the methods for handling interactions with the player's hand panel in the
 * game view (clicking on cards, refreshing the hand,and getting the selected card index).
 */
public interface PlayerHandPanelGUI {

  /**
   * Handles the click action on a card in the player's hand.
   * @param index of the card being clicked on in players hand.
   */
  void handleCardClick(int index);

  /**
   * Refreshes the player's hand panel in view.
   */
  void refresh();

  /**
   * Gets the index of the current selected card in player's hand.
   * @return the index of the current selected card in player's hand.
   */
  int getSelectedCardIndex();

}
