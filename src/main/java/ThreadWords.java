import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.graphics.Layer;

public class ThreadWords implements Runnable { // Selles klassis toimub sõnade alla liigutamine ja elude üle arve pidamine.
    private Layer wordLayer;
    private static int livesLeft = Game.getLivesLeft();

    public static int getLivesLeft() {
        return livesLeft;
    }

    public static void setLivesLeft(int livesLeft) {
        ThreadWords.livesLeft = livesLeft;
    }

    ThreadWords(Layer wordLayer) {
        this.wordLayer = wordLayer;
    }

    public void run() {
        try {
            for (int j = 0; j < 22; j++) {
                wordLayer.moveDownBy(2);
                Thread.sleep(Game.getStats()[1]);
            }
            if (Game.getWordsOnScreen().containsValue(wordLayer)) {
                livesLeft -= 1;
                System.out.println(livesLeft);
                Game.setLivesLeft(livesLeft);
                if(livesLeft >= 0) {
                    Game.getGameScreen().write("Lives: " + "♥".repeat(livesLeft) + " ".repeat(3), Positions.create(15, 42)).invoke();
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
