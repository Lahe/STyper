import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.graphics.Layer;

public class ThreadWords implements Runnable {
    private Layer wordLayer;
    private static int livesLeft = Game.getLivesLeft();

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
                Thread.sleep(Game.getStats()[1]);
            }
            if (Game.getWordsOnScreen().containsValue(wordLayer)){
                livesLeft -= 1;
                System.out.println(livesLeft);
                Game.setLivesLeft(livesLeft);
                Game.getGameScreen().write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
