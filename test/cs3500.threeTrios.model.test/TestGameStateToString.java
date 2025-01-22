import cs3500.threetrios.model.Direction;
import cs3500.threetrios.model.Card;
import cs3500.threetrios.model.Player;
import cs3500.threetrios.model.ThreeTriosGridModel;
import cs3500.threetrios.view.ThreeTriosTextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * The purpose of this class is to test the validity of the
 * ThreeTriosTextView implementation. This ensures what the text view
 * output looks like by testing what it looks like initially by the grid
 * state of cardCell and holes. This class also checks the game state
 * of the render after playing a card and seeing if the POV of the
 * game changes to the next player.
 */
public class TestGameStateToString {
  private ThreeTriosGridModel model;
  private Appendable out;
  private ThreeTriosTextView view;

  @Before
  public void setUp() {
    model = new ThreeTriosGridModel();
    out = new StringBuilder();
    view = new ThreeTriosTextView(model, out);
  }

  @Test
  public void testRenderInitialGame() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    try {
      view.render();
      StringBuilder expectedOutput = new StringBuilder();
      expectedOutput.append("Player: RED\n");
      expectedOutput.append("_  \n");
      expectedOutput.append("___\n");
      expectedOutput.append("  _\n");
      expectedOutput.append("Hand:\n");

      List<Card> redHand = model.getPlayerHand(Player.RED);
      for (Card card : redHand) {
        expectedOutput.append(card.getName()).append(" ");

        int northAttack = card.getAttackValue(Direction.NORTH);
        String northStr;
        if (northAttack == 10) {
          northStr = "A";
        } else {
          northStr = String.valueOf(northAttack);
        }
        expectedOutput.append(northStr).append(" ");

        int southAttack = card.getAttackValue(Direction.SOUTH);
        String southStr;
        if (southAttack == 10) {
          southStr = "A";
        } else {
          southStr = String.valueOf(southAttack);
        }
        expectedOutput.append(southStr).append(" ");

        int eastAttack = card.getAttackValue(Direction.EAST);
        String eastStr;
        if (eastAttack == 10) {
          eastStr = "A";
        } else {
          eastStr = String.valueOf(eastAttack);
        }
        expectedOutput.append(eastStr).append(" ");

        int westAttack = card.getAttackValue(Direction.WEST);
        String westStr;
        if (westAttack == 10) {
          westStr = "A";
        } else {
          westStr = String.valueOf(westAttack);
        }
        expectedOutput.append(westStr).append("\n");
      }

      Assert.assertEquals(expectedOutput.toString(), out.toString());
    } catch (IOException e) {
      fail("IOException should not occur");
    }
  }

  @Test
  public void testRenderBeforeGameStarts() throws IOException {
    Assert.assertThrows("Render game before game started",
            IllegalStateException.class, () -> view.render());
  }


  @Test
  public void testRenderAfterMove() {
    model.startGame("docs", "docs/cardsWorksWithAll.config",
            "docs", "docs/board.config");
    List<Card> redHand = model.getPlayerHand(Player.RED);
    Card cardToPlay = redHand.get(0);
    model.playCardToCardCell(cardToPlay, 0, 0); // RED plays at (0,0)

    try {
      // Reset the Appendable
      out = new StringBuilder();
      view = new ThreeTriosTextView(model, out);
      view.render();

      StringBuilder expectedOutput = new StringBuilder();
      expectedOutput.append("Player: BLUE\n");
      expectedOutput.append("R  \n");
      expectedOutput.append("___\n");
      expectedOutput.append("  _\n");
      expectedOutput.append("Hand:\n");
      List<Card> blueHand = model.getPlayerHand(Player.BLUE);
      for (Card card : blueHand) {
        expectedOutput.append(card.getName()).append(" ");

        int northAttack = card.getAttackValue(Direction.NORTH);
        String northStr;
        if (northAttack == 10) {
          northStr = "A";
        } else {
          northStr = String.valueOf(northAttack);
        }
        expectedOutput.append(northStr).append(" ");

        int southAttack = card.getAttackValue(Direction.SOUTH);
        String southStr;
        if (southAttack == 10) {
          southStr = "A";
        } else {
          southStr = String.valueOf(southAttack);
        }
        expectedOutput.append(southStr).append(" ");

        int eastAttack = card.getAttackValue(Direction.EAST);
        String eastStr;
        if (eastAttack == 10) {
          eastStr = "A";
        } else {
          eastStr = String.valueOf(eastAttack);
        }
        expectedOutput.append(eastStr).append(" ");

        int westAttack = card.getAttackValue(Direction.WEST);
        String westStr;
        if (westAttack == 10) {
          westStr = "A";
        } else {
          westStr = String.valueOf(westAttack);
        }
        expectedOutput.append(westStr).append("\n");
      }
      Assert.assertEquals(expectedOutput.toString(), out.toString());
    } catch (IOException e) {
      fail("IOException should not occur");
    }
  }

}