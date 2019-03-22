import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;

import java.util.HashMap;

public class Game {
    private static String rolledWord;
    private static int loc;
    private static Layer wordLayer;
    private static HashMap<String,Layer> wordsOnScreen = new HashMap<>();


    public static int getLoc() {
        return loc;
    }

    public static HashMap<String,Layer> getWordsOnScreen() {
        return wordsOnScreen;
    }

    public static void main(String[] args) throws Exception {
        final TileGrid tileGrid = SwingApplications.startTileGrid(
                AppConfigs.newConfig()
                        .withDefaultTileset(BuiltInCP437TilesetResource
                                .MS_GOTHIC_16X16)
                        .withSize(TypingSupport.getSIZE())
                        .withDebugMode(false)
                        .build());
        WordSpawner spawner = new WordSpawner();
        TypingSupport support = new TypingSupport();

        WordDrawer drawer = new WordDrawer(tileGrid);
        support.startTypingSupport(tileGrid);
        for (int i = 0; i < 20; i++) {
            System.out.println(getWordsOnScreen().toString());
            rolledWord = spawner.rollNext();
            loc = spawner.getLoc();
            drawer.drawWords(rolledWord);
            wordLayer = drawer.getWordLayer();
            wordsOnScreen.put(rolledWord,wordLayer);
            ThreadWords threadWords = new ThreadWords(wordLayer);
            Thread t_threadWords = new Thread(threadWords);
            t_threadWords.start();
            Thread.sleep(2000);
        }
    }
}
