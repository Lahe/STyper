import org.hexworks.cobalt.logging.api.Logger;
import org.hexworks.cobalt.logging.api.LoggerFactory;
import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.util.HashMap;

public class Game {
    private static String rolledWord;
    private static int loc;
    private static Layer wordLayer;
    private static HashMap<String, Layer> wordsOnScreen = new HashMap<>();
    private static Logger LOGGER = LoggerFactory.INSTANCE.getLogger(Game.class);

    public static int getLoc() {
        return loc;
    }

    public static HashMap<String, Layer> getWordsOnScreen() {
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

        final Screen menuScreen = Screens.createScreenFor(tileGrid);
        final Button startButton = Components.button().withText("START").withPosition(Positions.create(40, 20)).build();
        menuScreen.addComponent(startButton);

        final Screen gameScreen = Screens.createScreenFor(tileGrid);
        final Button backButton = Components.button().withText("BACK").withPosition(Positions.zero()).build();
        gameScreen.addComponent(backButton);

        menuScreen.display();
        //backButton.disable();
        startButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            LOGGER.info("Starting game!");
            //backButton.enable();
            gameScreen.display();
            return UIEventResponses.preventDefault();
        });

        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            LOGGER.info("Game finished!");
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
        //boolean test = backButton.isEnabled();
        WordSpawner spawner = new WordSpawner();
        TypingSupport support = new TypingSupport();
        WordDrawer drawer = new WordDrawer(gameScreen);
        support.startTypingSupport(gameScreen);
                for (int i = 0; i < 20; i++) {
                    System.out.println(getWordsOnScreen().toString());
                    rolledWord = spawner.rollNext();
                    loc = spawner.getLoc();
                    drawer.drawWords(rolledWord);
                    wordLayer = drawer.getWordLayer();
                    wordsOnScreen.put(rolledWord, wordLayer);
                    ThreadWords threadWords = new ThreadWords(wordLayer);
                    Thread t_threadWords = new Thread(threadWords);
                    t_threadWords.start();
                    Thread.sleep(2000);

    }
    }
}
