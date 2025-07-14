import model.ScoreCalculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AvarageTest {

  @Test
  public void testAvarage(){
    double result = ScoreCalculator.calculateScore(2, 10);

    assertEquals(20.0, result);
  }
  
}