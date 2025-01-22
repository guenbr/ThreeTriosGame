package cs3500.threetrios.view;

import cs3500.threetrios.model.CardCell;
import cs3500.threetrios.model.Cell;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.GameGridModel;
import cs3500.threetrios.model.HoleCell;
import cs3500.threetrios.model.Player;

import java.io.IOException;
import java.util.List;

/**
 * Provides a textual representation of the game state for the Three Trios game that is being
 * played. Will show a human-readable format so the user can see what is happening visually.
 */
public class ThreeTriosTextView implements ThreeTriosView {

  private GameGridModel model;
  private Appendable out;

  /**
   * Constructor that reads in the grid of the game.
   * @param model model of the game that is being played.
   * @param out is what the output is when the game is being rendered.
   * @throws IllegalArgumentException when the model is null as well as the appendable.
   */
  public ThreeTriosTextView(GameGridModel model, Appendable out) {
    if (model == null || out == null) {
      throw new IllegalArgumentException("Model and Appendable cannot be null");
    }
    this.model = model;
    this.out = out;
  }

  @Override
  public void render() throws IOException {
    if (out == null || model == null) {
      throw new IOException("Rendering failed, appendable or model is null");
    } else {
      try {
        out.append(this.toString());
      } catch (IOException e) {
        throw new IOException("Failed to render game state", e);
      }
    }
  }

  @Override
  public String toString() {
    if (model == null) {
      throw new IllegalStateException("Model is not set");
    }

    Player currentPlayer = model.getCurrentPlayer();
    if (currentPlayer == null || currentPlayer == Player.NULL_PLAYER) {
      throw new IllegalStateException("Invalid player");
    }

    StringBuilder sb = new StringBuilder();

    sb.append("Player: ").append(currentPlayer.getPlayer()).append("\n");

    int numRows = model.getRows();
    int numCols = model.getCols();

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numCols; col++) {
        Cell cell = model.getCell(row, col);
        if (cell instanceof HoleCell) {
          sb.append(' ');
        } else if (cell instanceof CardCell) {
          if (cell.isOccupied()) {
            // Get the owner's initial ('R' or 'B') from the cell
            char ownerInitial;
            if (cell.getOwner() == Player.RED) {
              ownerInitial = 'R';
            } else {
              ownerInitial = 'B';
            }
            sb.append(ownerInitial);
          } else {
            sb.append('_');
          }
        } else {
          sb.append(' ');
        }
      }
      sb.append("\n");
    }

    sb.append("Hand:\n");
    List<Card> hand = model.getPlayerHand(currentPlayer);
    for (Card card : hand) {
      sb.append(card.getName()).append(" ");
      sb.append(attackValueToString(card.getAttackValue(Direction.NORTH))).append(" ");
      sb.append(attackValueToString(card.getAttackValue(Direction.SOUTH))).append(" ");
      sb.append(attackValueToString(card.getAttackValue(Direction.EAST))).append(" ");
      sb.append(attackValueToString(card.getAttackValue(Direction.WEST))).append("\n");
    }

    return sb.toString();
  }

  /**
   * Converts an integer attack value to its string representation.
   * If the attack value is 10, it is represented as A.
   * All other attack values 1-9 are just returned to their actual string value.
   * @param attackValue the integer attack value to convert.
   * @return a string representing the attack value.
   */
  private String attackValueToString(int attackValue) {
    if (attackValue == 10) {
      return "A";
    } else {
      return String.valueOf(attackValue);
    }
  }
}