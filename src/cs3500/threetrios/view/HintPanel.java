package cs3500.threetrios.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.ReadOnlyTTModel;

/**
 * A transparent panel drawn over the grid that shows hint numbers indicating how many cards
 * would be flipped if the selected card is placed on a specific cell.
 */
public class HintPanel extends JPanel {
  private final ReadOnlyTTModel model;
  private Card selectedCard;

  /**
   * Hints on card model.
   * @param model model of the game.
   *
   */
  public HintPanel(ReadOnlyTTModel model) {
    this.model = model;
    setOpaque(false);
  }

  /**
   * Update the selected card for which hints should be displayed.
   */
  public void updateHints(Card card) {
    this.selectedCard = card;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (selectedCard == null || !isVisible()) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int cellWidth = getWidth() / model.getCols();
    int cellHeight = getHeight() / model.getRows();

    // Only draw hint numbers where legal moves exist
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        if (model.isLegalMove(row, col)) {
          int flips = model.getPotentialFlips(selectedCard, row, col);
          if (flips > 0) {
            drawHintNumber(g2d, row, col, flips, cellWidth, cellHeight);
          }
        }
      }
    }
  }

  private void drawHintNumber(Graphics2D g2d, int row, int col, int flips,
                              int cellWidth, int cellHeight) {
    int x = col * cellWidth;
    int y = row * cellHeight;

    // Draw hint in bottom-left corner of cell
    int hintSize = Math.min(cellWidth, cellHeight) / 3;
    int hintX = x + 5;
    int hintY = y + cellHeight - hintSize - 5;

    // Draw circle background
    g2d.setColor(new Color(0, 0, 0, 180));
    g2d.fillOval(hintX, hintY, hintSize, hintSize);

    // Draw number
    g2d.setColor(Color.WHITE);
    g2d.setFont(new Font("Arial", Font.BOLD, hintSize * 2 / 3));
    FontMetrics fm = g2d.getFontMetrics();
    String number = String.valueOf(flips);
    int textX = hintX + (hintSize - fm.stringWidth(number)) / 2;
    int textY = hintY + ((hintSize + fm.getAscent()) / 2);
    g2d.drawString(number, textX, textY);
  }

  /**
   * refreshes game.
   */
  public void refresh() {
    repaint();
  }
}
