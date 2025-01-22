package cs3500.threetrios.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.Position;
import cs3500.threetrios.model.ReadOnlyTTModel;
import cs3500.threetrios.model.ThreeTriosGridModel;
import cs3500.threetrios.strategy.StrategicThreeTrios;
import cs3500.threetrios.view.ViewListener;

/**
 * Represents a machine player in the game.
 * This player makes decisions based on the provided strategy
 * and automatically moves during its turn.
 */
public class MachinePlayer implements ThreeTriosPlayer {
  private final Player playerType;
  private final ThreeTriosGridModel model;
  private final StrategicThreeTrios strategy;
  private final List<ViewListener> listeners;

  /**
   * Constructs a MachinePlayer with the specified player type, game model, and strategy.
   * @param playerType the type of player like red or blue
   * @param model the read-only model representing the current state of the game
   * @param strategy the strategy used by this machine player to make optimal moves
   */
  public MachinePlayer(Player playerType, ReadOnlyTTModel model, StrategicThreeTrios strategy) {
    Objects.requireNonNull(playerType);
    Objects.requireNonNull(model);
    Objects.requireNonNull(strategy);
    this.playerType = playerType;
    this.model = (ThreeTriosGridModel) model;
    this.strategy = strategy;
    this.listeners = new ArrayList<>();
  }

  @Override
  public void addPlayerActionListener(ViewListener listener) {
    Objects.requireNonNull(listener);
    listeners.add(listener);
  }

  @Override
  public void performModelAction(ReadOnlyTTModel model) {
    Objects.requireNonNull(model);
    if (model.getCurrentPlayer() != playerType || model.isGameOver()) {
      return;
    }
    Card card = strategy.optimalCard(model, playerType);
    if (card != null) {
      Position pos = strategy.optimalPosition(model, card, playerType);
      if (pos != null) {
        try {
          this.model.playCardToCardCell(card, pos.getRow(), pos.getCol());
          for (ViewListener listener : listeners) {
            listener.gridCellClickedListener(pos.getRow(), pos.getCol());
          }
        } catch (IllegalArgumentException | IllegalStateException e) {
          System.err.println("Machine player failed to make move: " + e.getMessage());
        }
      }
    }
  }

  @Override
  public Player getTurn() {
    return playerType;
  }

  @Override
  public boolean isHuman() {
    return false;
  }
}