import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Game { // Selles klassis luuakse mängu graafiline liides, luuakse get- ja set-meetodid ning käivitatakse mäng.
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
    private static final Button easyButton = Components.button().withText("Easy").withPosition(Positions.create(38, 19)).build();
    private static final Button medButton = Components.button().withText("Medium").withPosition(Positions.create(38, 21)).build();
    private static final Button hardButton = Components.button().withText("Hard").withPosition(Positions.create(38, 23)).build();
    private static final Button extremeButton = Components.button().withText("Extreme").withPosition(Positions.create(38, 25)).build();
    private static final Button insaneButton = Components.button().withText("Insane").withPosition(Positions.create(38, 27)).build();
    private static int[] stats = new int[3];
    private static final int[] easyStats = {2000, 2000, 15};
    private static final int[] medStats = {1300, 1300, 30};
    private static final int[] hardStats = {1000, 1000, 60};
    private static final int[] extremeStats = {750, 750, 60};
    private static final int[] insaneStats = {500, 500, 60};
    private static List<Thread> threadid = new ArrayList<>(); //
    private static List<Layer> tekstid = new ArrayList<>(); //
    private static boolean stopLaunchGame = false;

    public static TileGrid getTileGrid() {
        return tileGrid;
    }

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

    public static void launchGame(Screen gameScreen) throws Exception { // Loob igale sõnale ThreadWords'i isendi ja kuvab sõnu ekraanile.
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
            tekstid.add(wordLayer);
            ThreadWords threadWords = new ThreadWords(wordLayer);
            Thread t_threadWords = new Thread(threadWords);
            /////////////////////d
            threadid.add(t_threadWords); //
            t_threadWords.start();
            livesLeft = ThreadWords.getLivesLeft();
            Thread.sleep(stats[0]);
            if (stopLaunchGame) {
                TypingSupport.setStartOrStopSupport(false);
                break;
            }
        }
        boolean gameRunning = true;
        while (gameRunning) { // Kontrollitakse, kas mäng on läbi. Võidu või kaotuse korral kuvatakse punktide arv.
            if (livesLeft == 0) {
                gameRunning = false;
                gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                gameOverScreen.display();
            } else if (wordsOnScreen.isEmpty()) {
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
        levelScreen.write("Choose level", Positions.create(36, 17)).invoke();
        levelScreen.addComponent(easyButton);
        levelScreen.addComponent(medButton);
        levelScreen.addComponent(hardButton);
        levelScreen.addComponent(extremeButton);
        levelScreen.addComponent(insaneButton);

        startButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelScreen.display();
            return UIEventResponses.preventDefault();
        });
        easyButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            stopLaunchGame = false;
            stats = easyStats;
            gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
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
            threadid.add(t);
            t.start();
            return UIEventResponses.preventDefault();
        });
        medButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            stopLaunchGame = false;
            stats = medStats;
            gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
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
            threadid.add(t);
            t.start();
            return UIEventResponses.preventDefault();
        });
        hardButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            stopLaunchGame = false;
            stats = hardStats;
            gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
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
            threadid.add(t);
            t.start();
            return UIEventResponses.preventDefault();
        });
        extremeButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            stopLaunchGame = false;
            stats = extremeStats;
            gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
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
            threadid.add(t);
            t.start();
            return UIEventResponses.preventDefault();
        });
        insaneButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            stopLaunchGame = false;
            stats = insaneStats;
            gameScreen.write("Lives: " + livesLeft, Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: Insane", Positions.create(70, 1)).invoke();
            gameScreen.display();
            Runnable runnable = () -> {
                try {
                    launchGame(gameScreen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            threadid.add(t);
            t.start();
            return UIEventResponses.preventDefault();
        });

        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> { // BACK nuppu vajutades, peatab threadid, kustutab teksti layerid ja taastab elud ning algse punktiseisu.
            System.out.println("THEDE ON :" + threadid.size());
            for (Thread a : threadid) {
                a.stop();
            }
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            TypingSupport.setStartOrStopSupport(false);
            stopLaunchGame = true;
            tileGrid.clear();
            livesLeft = 3;
            setLivesLeft(3);
            setPoints(0);
            TypingSupport.setPoints(0);
            ThreadWords.setLivesLeft(3);
            wordsOnScreen = new HashMap<>();
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }
}
