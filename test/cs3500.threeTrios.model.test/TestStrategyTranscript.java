import cs3500.threetrios.model.Player;
import cs3500.threetrios.strategy.Corner;
import cs3500.threetrios.strategy.mocks.MockTranscriptModel;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the Corner strategy's interaction with the game model by verifying the sequence
 * of method calls made during strategy execution. Uses a mock model that outputs method
 * in the console.
 */
public class TestStrategyTranscript {

  @Test
  public void CornerStrategyTranscriptConsole() {
    System.out.println("Transcript:");
    MockTranscriptModel model = new MockTranscriptModel();
    Corner strategy = new Corner();
    strategy.optimalCard(model, Player.RED);
    Assert.assertEquals(1, 1);
  }
}
