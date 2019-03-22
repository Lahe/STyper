import org.hexworks.zircon.api.graphics.Layer;

public class ThreadWords implements Runnable {
    private Layer wordLayer;

    ThreadWords(Layer wordLayer){
        this.wordLayer = wordLayer;
    }
    public void run() {
        try {
            for (int j = 0; j < 25; j++) {
                wordLayer.moveDownBy(2);
                Thread.sleep(1000);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
