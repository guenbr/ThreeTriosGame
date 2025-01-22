package cs3500.threetrios.main;


import cs3500.threetrios.controller.HumanPlayer;
import cs3500.threetrios.controller.MachinePlayer;
import cs3500.threetrios.controller.ThreeTriosController;
import cs3500.threetrios.controller.ThreeTriosPlayer;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosGridModel;
import cs3500.threetrios.strategy.Corner;
import cs3500.threetrios.strategy.MaxCardFlip;
import cs3500.threetrios.strategy.HardToFlip;
import cs3500.threetrios.view.ThreeTriosGUI;

/**
 * The main method that starts the ThreeTrios game by setting the game model, loading the
 * configuration files, and displaying the game view after it starts.
 */
public final class ThreeTrios {
  /**
   * This function is the main that runs the application.
   *
   * @param args of the users arguments of the user.
   */
  public static void main(String[] args) {
    if (args.length != 2) {
      System.out.println("Player types: human, strategy1, strategy2, strategy3");
      return;
    }
    ThreeTriosController.resetGameOverFlag();
    ThreeTriosGridModel model = new ThreeTriosGridModel();
    model.startGame("docs", "cardsWorksWithAll.config",
            "docs", "board.config");
    ThreeTriosPlayer player1 = createPlayer(args[0], model, Player.RED);
    ThreeTriosPlayer player2 = createPlayer(args[1], model, Player.BLUE);
    ThreeTriosGUI view1 = new ThreeTriosGUI(model);
    ThreeTriosGUI view2 = new ThreeTriosGUI(model);
    new ThreeTriosController(model, player1, view1, Player.RED);
    new ThreeTriosController(model, player2, view2, Player.BLUE);
    view1.setLocationRelativeTo(null);
    view2.setLocation(100, 150);
    view2.setVisible(true);
    view1.setVisible(true);
    if (!player1.isHuman() && !player2.isHuman()) {
      player1.performModelAction(model);
    }
  }

  private static ThreeTriosPlayer createPlayer(String type, ThreeTriosGridModel model,
                                               Player player) {
    switch (type.toLowerCase()) {
      case "human":
        return new HumanPlayer(player);
      case "strategy1":
        return new MachinePlayer(player, model, new Corner());
      case "strategy2":
        return new MachinePlayer(player, model, new MaxCardFlip());
      case "strategy3":
        return new MachinePlayer(player, model, new HardToFlip());
      default:
        throw new IllegalArgumentException("Invalid player type: " + type);
    }
  }
}