package cs3500.threetrios.view;

/**
 * This interface sets the methods for handling interactions with the grid panel in the game view.
 * Interactions include like clicking the grid cell or refreshing the grid itself.
 */
public interface GridPanelGUI {

  /**
   * This is handling the grid cell being clicked.
   * @param row the row of the gird cell being clicked.
   * @param col the column of the grid cell being clicked.
   */
  void handleGridClick(int row, int col);

  /**
   * This refreshes the grid panel in the view.
   */
  void refresh();
}
