package cs3500.threetrios.controller;

import java.util.List;
import java.util.Objects;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.GameGridModel;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.view.ThreeTriosGUI;
import cs3500.threetrios.view.ViewListener;

import javax.swing.JOptionPane;

/**
 * Controller for the TT that controls between the game model, view, and players.
 * This controller handles user actions, communicates with the model to update the
 * game state, and notifies the view to reflect changes, refresh.
 */
public class ThreeTriosController implements ViewListener, GameStatusListener {
  private final GameGridModel model;
  private final ThreeTriosPlayer player;
  private final ThreeTriosGUI view;
  private final Player playerType;
  private static boolean gameOverMessageShown = false; // Add this static field

  /**
   * Constructs a three trios model to represent state and rules of the game.
   * @param model the game model
   * @param player the player, either the human version or the computer version
   * @param view the view gui of the game for the user
   * @param playerType the type of player, blue or red
   */
  public ThreeTriosController(GameGridModel model, ThreeTriosPlayer player,
                              ThreeTriosGUI view, Player playerType) {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    Objects.requireNonNull(view);
    Objects.requireNonNull(playerType);
    this.model = model;
    this.player = player;
    this.view = view;
    this.playerType = playerType;

    this.view.setListener(this);
    view.setTitle(playerType.toString() + "'s View");
    this.model.addModelStatusListener(this);
  }

  @Override
  public void gridCellClickedListener(int row, int col) {
    if (model.getCurrentPlayer() != this.playerType) {
      JOptionPane.showMessageDialog(view,
              "It is " + model.getCurrentPlayer() + "'s turn",
              "Wrong Turn",
              JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (!player.isHuman()) {
      return;
    }
    int selectedCardIndex = view.getSelectedCardIndex();
    if (selectedCardIndex == -1) {
      JOptionPane.showMessageDialog(view,
              "Please select a card first",
              "No Card Selected",
              JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      List<Card> hand = model.getPlayerHand(playerType);
      if (selectedCardIndex < 0 || selectedCardIndex >= hand.size()) {
        return;
      }
      Card selectedCard = hand.get(selectedCardIndex);
      if (!model.isLegalMove(row, col)) {
        notifyInvalidMove("Cannot play to hole cell");
        return;
      }
      model.playCardToCardCell(selectedCard, row, col);
      view.refresh();
    } catch (IllegalArgumentException | IllegalStateException e) {
      notifyInvalidMove(e.getMessage());
    }
  }

  @Override
  public void cardClickedListener(Player player, int cardIndex) {
    Objects.requireNonNull(player);
    if (model.getCurrentPlayer() != this.playerType) {
      JOptionPane.showMessageDialog(view,
              "It is " + model.getCurrentPlayer() + "'s turn",
              "Wrong Turn",
              JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (player != this.playerType) {
      JOptionPane.showMessageDialog(view,
              "Player " + this.playerType + " cannot select from " + player + "'s hand",
              "Invalid Selection",
              JOptionPane.WARNING_MESSAGE);
      return;
    }
    view.refresh();
  }

  @Override
  public void notifyTurnChange(Player currentPlayer) {
    String titleText;
    if (model.isGameOver()) {
      titleText = playerType + "'s View - Game Over";
    } else {
      if (currentPlayer == playerType) {
        titleText = playerType + "'s View - Your Turn";
      } else {
        titleText = playerType + "'s View - Waiting for " + currentPlayer;
      }
    }
    view.setTitle(titleText);
    view.refresh();
    if (currentPlayer == playerType && !player.isHuman()) {
      player.performModelAction(model);
      view.refresh();
    }
  }

  @Override
  public void notifyGameOver(Player winner, int score) {
    view.setTitle(playerType + "'s View - Game Over");
    view.refresh();
    if (playerType == Player.RED && !gameOverMessageShown) {
      gameOverMessageShown = true;
      String message;
      if (winner == Player.NULL_PLAYER) {
        message = String.format("Game Over - Tie game! Both players scored %d!", score);
      } else {
        message = String.format("Game Over - %s wins with score %d!", winner, score);
      }
      JOptionPane.showMessageDialog(view, message);
    }
  }

  /**
   * Rests the game over flag so that the game over message is shown to user again when needed.
   * Called to essentially restart the game.
   */
  public static void resetGameOverFlag() {
    gameOverMessageShown = false;
  }

  /**
   * This is the pop-up to mark invalid moves.
   * @param message the specific error message prompted.
   */
  public void notifyInvalidMove(String message) {
    JOptionPane.showMessageDialog(view, message, "Invalid Move",
            JOptionPane.ERROR_MESSAGE);
  }
}
