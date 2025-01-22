package cs3500.threetrios.view;

import java.io.IOException;

/**
 * Interface for the view, will show you how battle phases are played out regards to the game
 * model.
 */
public interface ThreeTriosView {

  /**
   * Renders the current state of the game to the specified output.
   * @throws IOException if an IO error occurs while writing to the output.
   * @throws IllegalStateException if the game model is not set or the game has not been started.
   */
  void render() throws IOException; // Renders in the model (rendering game states).

  /**
   * Generates a string representation of the current game state (with attack values of the
   * current players/active players hand in respect to their hand).
   * @return a string representing the current state of the Three Trios game.
   * @throws IllegalStateException if the game model is not set or the current player is invalid.
   */
  String toString(); // This is what the players/users will see during game.


}
