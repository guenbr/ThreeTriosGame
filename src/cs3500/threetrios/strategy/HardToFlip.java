package cs3500.threetrios.strategy;

import java.util.Objects;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * A simplified strategy that picks cards with the highest attack values and positions
 * with minimal exposure, making them harder to flip.
 */
public class HardToFlip implements StrategicThreeTrios {

  @Override
  public Card optimalCard(ReadOnlyTTModel model, Player player) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);

    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot get optimal card, game over");
    }

    var hand = model.getPlayerHand(player);
    if (hand.isEmpty()) {
      return null;
    }

    // Find card with highest total attack values
    Card bestCard = hand.get(0);
    int highestTotal = getTotalAttack(bestCard);

    for (Card card : hand) {
      int total = getTotalAttack(card);
      if (total > highestTotal) {
        highestTotal = total;
        bestCard = card;
      } else if (total == highestTotal && hand.indexOf(card) < hand.indexOf(bestCard)) {
        // Break ties by choosing card with lower index
        bestCard = card;
      }
    }

    return bestCard;
  }

  @Override
  public void execute(ThreeTriosGridModel model, Player player) {
    Card card = optimalCard(model, player);
    if (card != null) {
      Position pos = optimalPosition(model, card, player);
      if (pos != null) {
        model.playCardToCardCell(card, pos.getRow(), pos.getCol());
      }
    }
  }

  @Override
  public Position optimalPosition(ReadOnlyTTModel model, Card card, Player player) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(card);
    Objects.requireNonNull(player);

    if (model.isGameOver()) {
      throw new IllegalStateException("Game not started, cannot call optimalPos.");
    }

    Position bestPosition = null;
    int leastExposed = Integer.MAX_VALUE;

    // Try each position
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        if (!model.isLegalMove(row, col)) {
          continue;
        }

        int exposedSides = countExposedSides(model, row, col);

        // Update if position has fewer exposed sides or is uppermost-leftmost
        if (exposedSides < leastExposed ||
                (exposedSides == leastExposed && isBetterPosition(row, col, bestPosition))) {
          leastExposed = exposedSides;
          bestPosition = new Position(row, col);
        }
      }
    }

    return bestPosition;
  }

  /**
   * Calculates total attack value of a card.
   */
  private int getTotalAttack(Card card) {
    int total = 0;
    for (Direction dir : Direction.values()) {
      total += card.getAttackValue(dir);
    }
    return total;
  }

  /**
   * Counts how many sides of a position are exposed to potential attacks.
   */
  private int countExposedSides(ReadOnlyTTModel model, int row, int col) {
    int exposed = 0;

    // Check each direction
    if (isValidCardCell(model, row - 1, col)) {
      exposed++; // North
    }
    if (isValidCardCell(model, row + 1, col)) {
      exposed++; // South
    }
    if (isValidCardCell(model, row, col - 1)) {
      exposed++; // West
    }
    if (isValidCardCell(model, row, col + 1)) {
      exposed++; // East
    }

    return exposed;
  }

  /**
   * Checks if a position is a valid card cell.
   */
  private boolean isValidCardCell(ReadOnlyTTModel model, int row, int col) {
    return row >= 0 && row < model.getRows() &&
            col >= 0 && col < model.getCols() &&
            model.getCellType(row, col) == CellType.CARD_CELL;
  }

  /**
   * Helper to determine if a position is better (uppermost-leftmost) than current best.
   */
  private boolean isBetterPosition(int row, int col, Position current) {
    return current == null ||
            row < current.getRow() ||
            (row == current.getRow() && col < current.getCol());
  }
}
