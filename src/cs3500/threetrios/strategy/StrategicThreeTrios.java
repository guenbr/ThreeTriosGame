package cs3500.threetrios.strategy;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;

/**
 * Strategy interface for choosing moves in ThreeTrios.
 * Each strategy implementation follows the specification for each
 * strategy defined. Determines the best card and position on grid to play.
 */
public interface StrategicThreeTrios {

  /**
   * Determines where to play the selected card.
   * @param model The current game state.
   * @param card The card to be played.
   * @param player current turn.
   * @return The position to play the card.
   * @throws IllegalArgumentException if any arguments is null.
   */
  Position optimalPosition(ReadOnlyTTModel model, Card card, Player player);

  /**
   * Determines the card and position for the next move.
   * @param model The current game state.
   * @param player current turn.
   * @return The selected card to play.
   * @throws IllegalArgumentException if any arguments is null.
   */
  Card optimalCard(ReadOnlyTTModel model, Player player);

  /**
   * This executes the move on the model and plays a card to
   * the card cell grid based on the strategy.
   * @param model the current model to make a move.
   * @param player which player is making the move.
   */
  void execute(ThreeTriosGridModel model, Player player);
}

