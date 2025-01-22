package cs3500.threetrios.view;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.FontMetrics;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;

/**
 * This GridPanel class is a graphical representation of the game grid that listens for
 * interactions of the users.
 */
public class GridPanel extends JPanel implements GridPanelGUI {

  private final ReadOnlyTTModel model;

  private ViewListener listener;

  /**
   * Constructs a new model, sets the preferred size of the panel and the background color.
   * Also adds a mouse listener to handle grid cell clicks.
   * @param model the read-only model representing the game state of the game.
   */
  public GridPanel(ReadOnlyTTModel model) {
    this.model = model;
    this.setPreferredSize(new Dimension(800, 600));
    this.setBackground(Color.WHITE);
    this.addMouseListener(new GridPanel.GridMouseListener());
  }

  private class GridMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      int cellWidth = getWidth() / model.getCols();
      int cellHeight = getHeight() / model.getRows();
      int col = e.getX() / cellWidth;
      int row = e.getY() / cellHeight;
      handleGridClick(row, col);
    }
  }

  @Override
  public void handleGridClick(int row, int col) {
    if (listener != null) {
      listener.gridCellClickedListener(row, col);
    }
  }

  @Override
  public void refresh() {
    repaint();
  }

  protected void setListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    int cellWidth = getWidth() / model.getCols();
    int cellHeight = getHeight() / model.getRows();
    Font font = new Font(Font.MONOSPACED, Font.BOLD, Math.min(cellWidth, cellHeight) / 4);
    g2d.setFont(font);
    FontMetrics fm = g2d.getFontMetrics();

    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        int xCord = col * cellWidth;
        int yCord = row * cellHeight;
        Cell cell = model.getCell(row, col);

        if (cell instanceof CardCell) {
          if (cell.isOccupied()) {
            if (cell.getOwner() == Player.RED) {
              g2d.setColor(new Color(255, 127, 127));
            } else if (cell.getOwner() == Player.BLUE) {
              g2d.setColor(new Color(173, 216, 230));
            }
          } else {
            g2d.setColor(Color.YELLOW);
          }
        } else {
          g2d.setColor(Color.LIGHT_GRAY);
        }
        g2d.fillRect(xCord, yCord, cellWidth, cellHeight);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(xCord, yCord, cellWidth, cellHeight);
        if (cell instanceof CardCell && cell.isOccupied()) {
          g2d.setColor(Color.BLACK);
          Card card = cell.getCard();
          String north = String.valueOf(card.getAttackValue(Direction.NORTH));
          g2d.drawString(north,
                  xCord + cellWidth / 2 - fm.stringWidth(north) / 2,
                  yCord + fm.getHeight());
          String south = String.valueOf(card.getAttackValue(Direction.SOUTH));
          g2d.drawString(south,
                  xCord + cellWidth / 2 - fm.stringWidth(south) / 2,
                  yCord + cellHeight - fm.getDescent());
          String west = String.valueOf(card.getAttackValue(Direction.WEST));
          g2d.drawString(west,
                  xCord + 5,
                  yCord + cellHeight / 2 + fm.getAscent() / 2);
          String east = String.valueOf(card.getAttackValue(Direction.EAST));
          g2d.drawString(east,
                  xCord + cellWidth - fm.stringWidth(east) - 5,
                  yCord + cellHeight / 2 + fm.getAscent() / 2);
        }
      }
    }
  }
}