package cs3500.threetrios.controller;

import java.util.ArrayList;
import java.util.List;

import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.view.ViewListener;

/**
 * Represents a human player in the game.
 * This class manages interactions and actions performed by human.
 */
public class HumanPlayer implements ThreeTriosPlayer {
  private final Player playerType;
  private final List<ViewListener> listeners;

  /**
   * Constructs a human player with the specified player type.
   * @param playerType the type of player like red or blue.
   */
  public HumanPlayer(Player playerType) {
    this.playerType = playerType;
    this.listeners = new ArrayList<>();
  }

  @Override
  public void addPlayerActionListener(ViewListener listener) {
    if (listener != null) {
      listeners.add(listener);
    }
  }

  @Override
  public void performModelAction(ReadOnlyTTModel model) {
    // Human players don't automatically perform actions
    // Wait for listener
  }

  @Override
  public Player getTurn() {
    return playerType;
  }

  @Override
  public boolean isHuman() {
    return true;
  }
}