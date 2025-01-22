package cs3500.threetrios.view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;

/**
 * This class is the GUI for the ThreeTrios game we made. It sets up the layout for displaying the
 * game grid, the players' hands, and the current player's turn info. It implements the
 * ViewListener to handle user interactions(such as selecting cards and clicking on grid cells).
 */
public class ThreeTriosGUI extends JFrame implements ViewListener {

  private int previousSelectedCard = -1;

  private GridPanel gridPanel;

  private PlayerHandPanel redHandPanel;

  private PlayerHandPanel blueHandPanel;

  private JPanel displayPlayerTurn;

  private JLabel playerTurnLabel;

  private ReadOnlyTTModel model;


  /**
   * Constructs a new model that sets up the main window, creates the game panel (grid and player
   * hand) to organize them into a layout.
   * @param model the read-only model representing the game state.
   */
  public ThreeTriosGUI(ReadOnlyTTModel model) {
    this.model = model;
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setSize(1250, 750);
    this.setMinimumSize(new Dimension(800, 400));
    this.setLocationRelativeTo(null);

    gridPanel = new GridPanel(model);
    gridPanel.setListener(this);
    redHandPanel = new PlayerHandPanel(model, Player.RED);
    redHandPanel.setListener(this);
    blueHandPanel = new PlayerHandPanel(model, Player.BLUE);
    blueHandPanel.setListener(this);

    this.setLayout(new BorderLayout());

    displayPlayerTurn = new JPanel();
    playerTurnLabel = new JLabel("Current player: " + model.getCurrentPlayer());
    displayPlayerTurn.removeAll();
    displayPlayerTurn.add(playerTurnLabel);

    this.add(displayPlayerTurn, BorderLayout.NORTH);
    redHandPanel.setBackground(new Color(255, 127, 127));
    blueHandPanel.setBackground(new Color(173, 216, 230));
    this.add(redHandPanel, BorderLayout.WEST);
    this.add(gridPanel, BorderLayout.CENTER);
    this.add(blueHandPanel, BorderLayout.EAST);
  }

  /**
   * This refreshes the entire grid, including players hands and the entire cells occupied to
   * original.
   */
  public void refresh() {
    playerTurnLabel.setText("Current player: " + this.model.getCurrentPlayer());
    displayPlayerTurn.repaint();

    // Update panel backgrounds based on current turn
    redHandPanel.setBackground(model.getCurrentPlayer() == Player.RED ?
            new Color(255, 127, 127) : Color.GRAY);
    blueHandPanel.setBackground(model.getCurrentPlayer() == Player.BLUE ?
            new Color(173, 216, 230) : Color.GRAY);

    gridPanel.refresh();
    redHandPanel.refresh();
    blueHandPanel.refresh();
    this.repaint();
  }

  @Override
  public void cardClickedListener(Player player, int cardIndex) {
    if (cardIndex == -1) {
      System.out.println("Deselected card " + previousSelectedCard + " in " + player + "'s hand");
      previousSelectedCard = -1;
    } else {
      previousSelectedCard = cardIndex;
      System.out.println("Selected card " + cardIndex + " in " + player + "'s hand");
    }
  }

  @Override
  public void gridCellClickedListener(int row, int col) {
    System.out.println("Clicked grid: row = " + row + ", col = " + col);
  }

  /**
   * Sets the listener for both the grid panel and the hand panels red/blue.
   * This method assigns the given listener to each of the panels, allowing the
   * panels to respond to user interactions or other events as defined in the listener.
   *
   * @param listener The to be set for the panels. This listener
   *                 will handle events such as user input or panel updates.
   */
  public void setListener(ViewListener listener) {
    this.gridPanel.setListener(listener);
    this.redHandPanel.setListener(listener);
    this.blueHandPanel.setListener(listener);
  }

  /**
   * Retrieves the index of the currently selected card based on the current player.
   * If the current player is red, the method checks the selected card
   * index from the red hand panel. If the current player is blue, it
   * checks the selected card index from the blue hand panel.
   *
   * @return The index of the selected card in the current player's hand. If no card
   *         is selected, the method will likely return a negative value (e.g., -1).
   */
  public int getSelectedCardIndex() {
    if (model.getCurrentPlayer() == Player.RED) {
      return redHandPanel.getSelectedCardIndex();
    } else {
      return blueHandPanel.getSelectedCardIndex();
    }
  }
}