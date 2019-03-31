import org.hexworks.cobalt.logging.api.Logger;
import org.hexworks.cobalt.logging.api.LoggerFactory;
import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.ColorTheme;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.util.HashMap;

public class Game {
    private static final TileGrid tileGrid = SwingApplications.startTileGrid(
            AppConfigs.newConfig()
                    .withDefaultTileset(BuiltInCP437TilesetResource
                            .MS_GOTHIC_16X16)
                    .withSize(TypingSupport.getSIZE())
                    .withDebugMode(false)
                    .build());
    private static String rolledWord;
    private static int loc;
    private static Layer wordLayer;
    private static HashMap<String, Layer> wordsOnScreen = new HashMap<>();
    private static int gameLevel = 1;
    private static int livesLeft = 3;
    private static int points = 0;
    private static final Screen menuScreen = Screens.createScreenFor(tileGrid);
    private static final Screen levelScreen = Screens.createScreenFor(tileGrid);
    private static final Button startButton = Components.button().withText("START").withPosition(Positions.create(38, 20)).build();
    private static final Screen gameScreen = Screens.createScreenFor(tileGrid);
    private static final Button backButton = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
    private static final Screen gameOverScreen = Screens.createScreenFor(tileGrid);
    private static final Screen gameFinishedScreen = Screens.createScreenFor(tileGrid);
    private static final Button easyButton = Components.button().withText("Easy").withPosition(Positions.create(38,19)).build();
    private static final Button medButton = Components.button().withText("Medium").withPosition(Positions.create(38,21)).build();
    private static final Button hardButton = Components.button().withText("Hard").withPosition(Positions.create(38,23)).build();
    private static final Button extremeButton = Components.button().withText("Extreme").withPosition(Positions.create(38,25)).build();
    private static int[] stats = new int[3];
    private static final int[] easyStats = {2000,2000,15};
    private static final int[] medStats = {1500,1500,30};
    private static final int[] hardStats = {750,750,60};
    private static final int[] extremeStats = {500,500,90};

    public static int[] getStats() {
        return stats;
    }

    public static void setLivesLeft(int livesLeft) {
        Game.livesLeft = livesLeft;
    }

    public static Screen getGameScreen() {
        return gameScreen;
    }

    public static void setPoints(int points) {
        Game.points = points;
    }

    public static int getPoints() {
        return points;
    }

    public static int getLivesLeft() {
        return livesLeft;
    }

    public static int getLoc() {
        return loc;
    }

    public static HashMap<String, Layer> getWordsOnScreen() {
        return wordsOnScreen;
    }

    public static void launchGame(Screen gameScreen) throws Exception {
        WordSpawner spawner = new WordSpawner();
        WordDrawer drawer = new WordDrawer(gameScreen);
        TypingSupport support = new TypingSupport();
        support.startTypingSupport(gameScreen);
        for (int i = 0; i <= stats[2]; i++) {
            if (livesLeft == 0) {
                gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                gameOverScreen.display();
            }
            //System.out.println(getWordsOnScreen().toString());
            rolledWord = spawner.rollNext();
            loc = spawner.getLoc();
            drawer.drawWords(rolledWord);
            wordLayer = drawer.getWordLayer();
            wordsOnScreen.put(rolledWord, wordLayer);
            ThreadWords threadWords = new ThreadWords(wordLayer);
            Thread t_threadWords = new Thread(threadWords);
            t_threadWords.start();
            livesLeft = ThreadWords.getLivesLeft();

            Thread.sleep(stats[0]);
        }
        boolean gameRunning = true;
        while (gameRunning){
            if (livesLeft == 0) {
                gameRunning = false;
                gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                gameOverScreen.display();
            }
            else if (wordsOnScreen.isEmpty()){
                gameRunning = false;
                gameFinishedScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                gameFinishedScreen.display();
            }
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) throws Exception {
        menuScreen.addComponent(startButton);
        gameOverScreen.write("GAME OVER", Positions.create(38, 20)).invoke();
        gameFinishedScreen.write("Game Finished. Well done!", Positions.create(32, 20)).invoke();
        gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
        gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();

        gameScreen.addComponent(backButton);
        menuScreen.display();
        levelScreen.write("Choose level", Positions.create(36,17)).invoke();
        levelScreen.addComponent(easyButton);
        levelScreen.addComponent(medButton);
        levelScreen.addComponent(hardButton);
        levelScreen.addComponent(extremeButton);

        startButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelScreen.display();
            return UIEventResponses.preventDefault();
        });
        easyButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) ->  {
            stats = easyStats;
            gameScreen.write("Level: Easy", Positions.create(73, 1)).invoke();
            gameScreen.display();
            Runnable runnable = () -> {
                try {
                    launchGame(gameScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });
        medButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) ->  {
            stats = medStats;
            gameScreen.write("Level: Medium", Positions.create(70, 1)).invoke();
            gameScreen.display();
            Runnable runnable = () -> {
                try {
                    launchGame(gameScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });
        hardButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) ->  {
            stats = hardStats;
            gameScreen.write("Level: Hard", Positions.create(73, 1)).invoke();
            gameScreen.display();
            Runnable runnable = () -> {
                try {
                    launchGame(gameScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });
        extremeButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) ->  {
            stats = extremeStats;
            gameScreen.write("Level: Extreme", Positions.create(70, 1)).invoke();
            gameScreen.display();
            Runnable runnable = () -> {
                try {
                    launchGame(gameScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });

        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }
}
