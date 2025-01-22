package cs3500.threetrios.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;

/**
 * This class represents the hand of a player in the game, displaying the player's cards and
 * allowing interaction through clicking.
 */
public class PlayerHandPanel extends JPanel implements PlayerHandPanelGUI {
  private final ReadOnlyTTModel model;

  private final Player player;

  private int selectedCard = -1;

  private ViewListener listener;

  /**
   * Constructs a new model for a given player and model, sets the preferred
   * size of the panel, and adds a mouse listener to handle card clicks.
   *
   * @param model the read-only model representing the game state.
   * @param player the player whose hand is displayed in this panel.
   */
  public PlayerHandPanel(ReadOnlyTTModel model, Player player) {
    this.model = model;
    this.player = player;
    setPreferredSize(new Dimension(200, 600));
    addMouseListener(new PlayerHandMouseListener());
  }

  private class PlayerHandMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      if (model.getCurrentPlayer() == player) {
        List<Card> hand = model.getPlayerHand(player);
        if (!hand.isEmpty()) {  // Only handle clicks if hand is not empty
          int cardHeight = getHeight() / hand.size();
          int clickedIndex = e.getY() / cardHeight;
          handleCardClick(clickedIndex);
        }
      }
    }
  }

  protected void setListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void handleCardClick(int clickedIndex) {
    List<Card> hand = model.getPlayerHand(player);

    if (clickedIndex >= 0 && clickedIndex < hand.size()) {
      if (selectedCard == clickedIndex) {
        selectedCard = -1;
      } else {
        if (selectedCard != -1) {
          if (listener != null) {
            listener.cardClickedListener(player, -1);
          }
        }
        selectedCard = clickedIndex;
      }
      if (listener != null) {
        listener.cardClickedListener(player, selectedCard);
      }
      refresh();
    }
  }

  @Override
  public void refresh() {
    repaint();
  }

  @Override
  public int getSelectedCardIndex() {
    return selectedCard;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    List<Card> hand = model.getPlayerHand(player);

    // Set background color based on player
    if (player == Player.RED) {
      setBackground(new Color(255, 127, 127));
    } else {
      setBackground(new Color(173, 216, 230));
    }

    // Don't attempt to paint cards if hand is empty
    if (hand.isEmpty()) {
      return;
    }

    int cardHeight = getHeight() / Math.max(1, hand.size());
    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      int y = i * cardHeight;

      // Keep magenta highlight for selected card regardless of turn
      if (i == selectedCard) {
        g2d.setColor(Color.MAGENTA);
      } else {
        g2d.setColor(getBackground());
      }

      g2d.fillRect(0, y, getWidth(), cardHeight);
      g2d.setColor(Color.BLACK);
      g2d.drawRect(0, y, getWidth(), cardHeight);
      drawCardValues(g2d, card, y, getWidth(), cardHeight);
    }
  }

  private void drawCardValues(Graphics2D g2d, Card card, int y, int width, int height) {
    g2d.setColor(Color.BLACK);
    Font font = new Font(Font.MONOSPACED, Font.BOLD, 50);
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();
    int centerX =  width / 2;
    int centerY = y + height / 2;
    String north = String.valueOf(card.getAttackValue(Direction.NORTH));
    g2d.drawString(north, centerX - fm.stringWidth(north) / 2, y + fm.getHeight());

    String south = String.valueOf(card.getAttackValue(Direction.SOUTH));
    g2d.drawString(south, centerX - fm.stringWidth(south) / 2,
            y + height - fm.getDescent());

    String west = String.valueOf(card.getAttackValue(Direction.WEST));
    g2d.drawString(west,  5, centerY + fm.getAscent() / 2);

    String east = String.valueOf(card.getAttackValue(Direction.EAST));
    g2d.drawString(east,  width - fm.stringWidth(east) - 5,
            centerY + fm.getAscent() / 2);
  }
}
