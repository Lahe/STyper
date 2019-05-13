import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.graphics.Layer;

public class ThreadWords extends GameVars implements Runnable { // Selles klassis toimub sõnade alla liigutamine ja elude üle arve pidamine.
    private Layer wordLayer;

    public static int getLivesLeft() {
        return livesLeft;
    }

    ThreadWords(Layer wordLayer) {
        this.wordLayer = wordLayer;
    }

    public void run() {
        try {
            for (int j = 0; j < 22; j++) {
                wordLayer.moveDownBy(2);
                Thread.sleep(GameVars.stats[1]);
            }
            if (wordsOnScreen.containsValue(wordLayer)) {
                livesLeft -= 1;
                System.out.println(livesLeft);
                if (livesLeft >= 0) {
                    Game.getMutableGameScreen().write("Lives: " + "♥".repeat(livesLeft) + " ".repeat(3), Positions.create(15, 42)).invoke();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
