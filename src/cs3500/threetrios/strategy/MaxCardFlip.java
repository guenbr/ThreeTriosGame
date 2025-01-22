package cs3500.threetrios.strategy;

import java.util.List;
import java.util.Objects;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * This class provides a strategy for selecting the optimal card and position based on maximizing
 * the number of flips in the game. The strategy checks all cards in the player's hand and their
 * potential placements on the game board to maximize the number of flips. If multiple positions or
 * cards result in the same number of flips, the one with the higher priority is chosen.
 */
public class MaxCardFlip implements StrategicThreeTrios {

  @Override
  public Card optimalCard(ReadOnlyTTModel model, Player player) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);

    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot get optimal card, game over");
    }

    List<Card> hand = model.getPlayerHand(player);
    if (hand.isEmpty()) {
      return null;
    }

    Card bestCard = hand.get(0);
    Position bestPosition = null;
    int maxFlips = -1;

    // Try each position and card combination to find highest flips
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        if (!model.isLegalMove(row, col)) {
          continue;
        }

        for (Card card : hand) {
          int flips = model.getPotentialFlips(card, row, col);
          if (flips > maxFlips ||
                  (flips == maxFlips && isBetterPosition(row, col, bestPosition))) {
            maxFlips = flips;
            bestCard = card;
            bestPosition = new Position(row, col);
          }
        }
      }
    }

    return bestCard;
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
    int maxFlips = -1;

    // Check all positions for most flips
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        if (!model.isLegalMove(row, col)) {
          continue;
        }

        int flips = model.getPotentialFlips(card, row, col);
        if (flips > maxFlips ||
                (flips == maxFlips && isBetterPosition(row, col, bestPosition))) {
          maxFlips = flips;
          bestPosition = new Position(row, col);
        }
      }
    }

    // If no best position found, return first legal position
    if (bestPosition == null) {
      for (int row = 0; row < model.getRows(); row++) {
        for (int col = 0; col < model.getCols(); col++) {
          if (model.isLegalMove(row, col)) {
            return new Position(row, col);
          }
        }
      }
    }

    return bestPosition;
  }

  /**
   * Helper to determine if a position is better (uppermost-leftmost) than the current best.
   */
  private boolean isBetterPosition(int row, int col, Position current) {
    return current == null ||
            row < current.getRow() ||
            (row == current.getRow() && col < current.getCol());
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
}