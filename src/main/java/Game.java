

import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.application.Application;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.color.TileColor;
import org.hexworks.zircon.api.component.ComponentAlignment;
import org.hexworks.zircon.api.component.Label;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.data.Tile;
import org.hexworks.zircon.api.graphics.CharacterTileString;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.resource.TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game {
    private static String rolledWord;
    private static int loc;
    private static Layer wordLayer;
    private static HashMap<String,Layer> wordsOnScreen = new HashMap<>();
    private static final TileGrid tileGrid = SwingApplications.startTileGrid(
            AppConfigs.newConfig()
                    .withDefaultTileset(BuiltInCP437TilesetResource
                            .MS_GOTHIC_16X16)
                    .withSize(TypingSupport.getSIZE())
                    .withDebugMode(false)
                    .build());

    public static int getLoc() {
        return loc;
    }

    public static HashMap<String,Layer> getWordsOnScreen() {
        return wordsOnScreen;
    }

    public static String getRolledWord() {
        return rolledWord;
    }

    public static Layer getWordLayer() {
        return wordLayer;
    }


    public static void main(String[] args) throws Exception {
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
