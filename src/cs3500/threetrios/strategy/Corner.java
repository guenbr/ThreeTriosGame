package cs3500.threetrios.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.CellType;
import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * Corner strategy prioritizes corner positions (only have two exposed attack values)
 * and selects cards based on their exposed attack values for the specific corner position.
 */

public class Corner implements StrategicThreeTrios {

  @Override
  public Position optimalPosition(ReadOnlyTTModel model, Card card, Player player) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(card);
    Objects.requireNonNull(player);
    if (model.isGameOver()) {
      throw new IllegalStateException("Game not started, cannot call optimalPos.");
    }
    Position corner = optimalCorner(model);
    if (corner != null) {
      return corner;
    }

    return findUppermostLeftmostPosition(model);
  }

  /**
   * break ties by choosing the move with the uppermost-leftmost
   * coordinate for the position and then choose the best card for
   * that position with an index closest to 0 in the hand.
   */
  private Position findUppermostLeftmostPosition(ReadOnlyTTModel model) {
    List<Position> legalMoves = model.getLegalMoves();
    if (!legalMoves.isEmpty()) {
      Position optimalPosition = legalMoves.get(0);
      for (Position pos : legalMoves) {
        if (pos.getRow() < optimalPosition.getRow() ||
                (pos.getRow() == optimalPosition.getRow() &&
                        pos.getCol() < optimalPosition.getCol())) {
          optimalPosition = pos;
        }
      }
      return optimalPosition;
    }
    return null;
  }

  @Override
  public Card optimalCard(ReadOnlyTTModel model, Player player) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    // This ensures that there must be cards in a hand.
    if (model.isGameOver()) {
      throw new IllegalStateException("Cannot get optimal card, game over");
    }

    List<Card> hand = model.getPlayerHand(player);
    Position optimalCorner = optimalCorner(model);
    // This means tie; chose card 0 in the hand
    if (optimalCorner == null) {
      return hand.get(0);
    }

    List<Direction> exposedDirections = getExposedDirections(model, optimalCorner);
    return optimalCardForExposedDirection(hand, exposedDirections);
  }

  /**
   * Finds the best available corner position, from uppermost-leftmost.
   */
  private Position optimalCorner(ReadOnlyTTModel model) {
    Position optimalCorner = null;
    for (int row = 0; row < model.getRows(); row++) {
      for (int col = 0; col < model.getCols(); col++) {
        if (isCorner(model, row, col)) {
          Position currentPos = new Position(row, col);
          if (optimalCorner == null || row < optimalCorner.getRow() ||
                  (row == optimalCorner.getRow() && col < optimalCorner.getCol())) {
            optimalCorner = currentPos;
          }
        }
      }
    }

    return optimalCorner;
  }

  /**
   * A corner is any position where exactly 2 sides are exposed for attack.
   */
  private boolean isCorner(ReadOnlyTTModel model, int row, int col) {
    Objects.requireNonNull(model);
    if (!model.isLegalMove(row, col)) {
      return false;
    }

    int exposedSides = 0;

    // North
    if (isHoleOrEdge(model, row - 1, col)) {
      exposedSides++;
    }
    // South
    if (isHoleOrEdge(model, row + 1, col)) {
      exposedSides++;
    }
    // West
    if (isHoleOrEdge(model, row, col - 1)) {
      exposedSides++;
    }
    // East
    if (isHoleOrEdge(model, row, col + 1)) {
      exposedSides++;
    }

    // Corner defined as having only two exposed attack values.
    return exposedSides == 2;
  }

  /**
   * Checks if a position is either off the grid (edge) or a hole.
   */
  private boolean isHoleOrEdge(ReadOnlyTTModel model, int row, int col) {
    // Check if position is off grid (edge)
    if (row < 0 || row >= model.getRows() || col < 0 || col >= model.getCols()) {
      return true;
    }
    // Check if position is a hole
    return model.getCellType(row, col) == CellType.HOLE;
  }

  /**
   * Gets the list of directions that are exposed for a position.
   */
  private List<Direction> getExposedDirections(ReadOnlyTTModel model, Position position) {
    List<Direction> exposedDirections = new ArrayList<>();
    int row = position.getRow();
    int col = position.getCol();

    if (isHoleOrEdge(model, row - 1, col)) {
      exposedDirections.add(Direction.NORTH);
    }
    if (isHoleOrEdge(model, row + 1, col)) {
      exposedDirections.add(Direction.SOUTH);
    }
    if (isHoleOrEdge(model, row, col - 1)) {
      exposedDirections.add(Direction.WEST);
    }
    if (isHoleOrEdge(model, row, col + 1)) {
      exposedDirections.add(Direction.EAST);
    }

    return exposedDirections;
  }

  /**
   * Selects the best card based on attack values in the exposed directions.
   */
  private Card optimalCardForExposedDirection(List<Card> hand, List<Direction> exposedDirections) {
    Card optimalCard = hand.get(0);
    int bestScore = calculateExposedAttackValue(optimalCard, exposedDirections);
    int bestIndex = 0;

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      int score = calculateExposedAttackValue(card, exposedDirections);
      if (score > bestScore || (score == bestScore && i < bestIndex)) {
        bestScore = score;
        optimalCard = card;
        bestIndex = i;
      }
    }

    return optimalCard;
  }

  /**
   * Calculates sum of attack value for a card from only on its
   * values that are exposed.
   */
  private int calculateExposedAttackValue(Card card, List<Direction> exposedDirections) {
    int score = 0;
    for (Direction dir : exposedDirections) {
      score += card.getAttackValue(dir);
    }
    return score;
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