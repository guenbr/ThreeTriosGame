package cs3500.threetrios.model;

import java.util.List;

import cs3500.threetrios.controller.GameStatusListener;

/**
 * Interface for the game model of the Three Trios, that manages the state and logic of the game 
 * grid. 
 * It adds mutating methods that allow for modifying the game state, such as creating the grid, 
 * playing cards, and starting the game.
 */
public interface GameGridModel extends ReadOnlyTTModel {
  
  /**
   * This creates a grid based on the users desires. (amount of lines and cards wanted).
   */
  void createGrid(List<String> gridLines, List<Card> cards);

  /**
   * This plays the card from the certain players hand to the cell. It ensures
   * that the cell the player wants to play to is a type CardCell.
   * @param card the card in the players hand.
   * @param row the row the CardCell is in.
   * @param col the col the CardCell is in.
   */
  void playCardToCardCell(Card card, int row, int col); // Specific cell that is being played to.

  /**
   * Starts the game of the ThreeTrios game.
   * @param directoryCards the directory of where the card config file is located.
   * @param filenameCards the card file.
   * @param directoryBoard the directory of where the board config file is located.
   * @param filenameBoard the board file.
   */
  void startGame(String directoryCards, String filenameCards,
                 String directoryBoard, String filenameBoard); // Starts

  /**
   * Adds a listener to the model to solve notifications that come up.
   * @param listener to the listener add.
   * @throws IllegalArgumentException if listener equals null
   */
  void addModelStatusListener(GameStatusListener listener);

}