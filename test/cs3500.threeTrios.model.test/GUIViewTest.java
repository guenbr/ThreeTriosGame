//import org.junit.Assert;
//import org.junit.Test;
//
//import java.util.List;
//
//import cs3500.threetrios.model.hw05.Card;
//import cs3500.threetrios.model.hw05.Player;
//import cs3500.threetrios.model.hw05.ThreeTriosGridModel;
//import cs3500.threetrios.view.hw06.ThreeTriosGUI;
//
///**
// * Tests for the Three Trios GUI view implementation.
// * Verifies that the view correctly displays game state changes,
// * updates after moves, and maintains synchronization with the model.
// */
//public class GUIViewTest {
//
//  /**
//   * Helper method to create and initialize a new game model.
//   * @return initialized ThreeTriosGridModel
//   */
//  private ThreeTriosGridModel createInitializedModel() {
//    ThreeTriosGridModel model = new ThreeTriosGridModel();
//    model.startGame("docs", "cardsWorksWithAll.config",
//            "docs", "board.config");
//    return model;
//  }
//
//  @Test
//  public void testInitialGameState() {
//    ThreeTriosGridModel model = createInitializedModel();
//    ThreeTriosGUI view = new ThreeTriosGUI(model);
//
//    // Verify initial state
//    Assert.assertFalse("Game should not be over initially", model.isGameOver());
//    Assert.assertEquals("Initial player should be RED", Player.RED, model.getCurrentPlayer());
//    Assert.assertEquals("Red player should have full hand",
//            model.getInitialHandSize(), model.getPlayerHand(Player.RED).size());
//    Assert.assertEquals("Blue player should have full hand",
//            model.getInitialHandSize(), model.getPlayerHand(Player.BLUE).size());
//  }
//
//  @Test
//  public void testRedTurnGameState() {
//    ThreeTriosGridModel model = createInitializedModel();
//    ThreeTriosGUI view = new ThreeTriosGUI(model);
//
//    // Play red's move
//    List<Card> redHand = model.getPlayerHand(Player.RED);
//    int initialHandSize = redHand.size();
//    model.playCardToCardCell(redHand.get(0), 0, 0);
//    view.refresh();
//
//    // Verify state after red's move
//    Assert.assertEquals("Red's hand should decrease by 1",
//            initialHandSize - 1, model.getPlayerHand(Player.RED).size());
//    Assert.assertEquals("Current player should be BLUE",
//            Player.BLUE, model.getCurrentPlayer());
//    Assert.assertTrue("Cell (0,0) should be occupied",
//            model.getCell(0, 0).isOccupied());
//    Assert.assertEquals("Cell (0,0) should be owned by RED",
//            Player.RED, model.getCell(0, 0).getOwner());
//  }
//
//  @Test
//  public void testBlueTurnGameState() {
//    ThreeTriosGridModel model = createInitializedModel();
//    ThreeTriosGUI view = new ThreeTriosGUI(model);
//
//    // Play moves for both players
//    List<Card> redHand = model.getPlayerHand(Player.RED);
//    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
//    int initialBlueHandSize = blueHand.size();
//
//    model.playCardToCardCell(redHand.get(0), 0, 0);
//    model.playCardToCardCell(blueHand.get(0), 1, 1);
//    view.refresh();
//
//    // Verify state after both moves
//    Assert.assertEquals("Blue's hand should decrease by 1",
//            initialBlueHandSize - 1, model.getPlayerHand(Player.BLUE).size());
//    Assert.assertEquals("Current player should be RED",
//            Player.RED, model.getCurrentPlayer());
//    Assert.assertTrue("Cell (1,1) should be occupied",
//            model.getCell(1, 1).isOccupied());
//    Assert.assertEquals("Cell (1,1) should be owned by BLUE",
//            Player.BLUE, model.getCell(1, 1).getOwner());
//  }
//
//  @Test
//  public void testHandSizeDecreasesAfterMoves() {
//    ThreeTriosGridModel model = createInitializedModel();
//    ThreeTriosGUI view = new ThreeTriosGUI(model);
//
//    List<Card> redHand = model.getPlayerHand(Player.RED);
//    List<Card> blueHand = model.getPlayerHand(Player.BLUE);
//    int initialRedHandSize = redHand.size();
//    int initialBlueHandSize = blueHand.size();
//
//    // Play multiple moves
//    model.playCardToCardCell(redHand.get(0), 0, 0);
//    model.playCardToCardCell(blueHand.get(0), 1, 1);
//    model.playCardToCardCell(redHand.get(0), 2, 2);
//    view.refresh();
//
//    // Verify hand sizes
//    Assert.assertEquals("Red's hand should decrease by 2",
//            initialRedHandSize - 2, model.getPlayerHand(Player.RED).size());
//    Assert.assertEquals("Blue's hand should decrease by 1",
//            initialBlueHandSize - 1, model.getPlayerHand(Player.BLUE).size());
//  }
//}