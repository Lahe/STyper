import org.hexworks.zircon.api.AppConfigs;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.SwingApplications;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.screen.Screen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.HashMap;

public class Game extends GameVars { // Selles klassis luuakse mängu graafiline liides, luuakse get- ja set-meetodid ning käivitatakse mäng.
    private static final TileGrid tileGrid = SwingApplications.startTileGrid(AppConfigs.newConfig()
            .withDefaultTileset(BuiltInCP437TilesetResource.MS_GOTHIC_16X16)
            .withSize(TypingSupport.getSIZE())
            .withDebugMode(false)
            .build());
    private static Menu menu = new Menu(tileGrid);
    private static Shop shop = new Shop(tileGrid);
    private static Scores scores = new Scores(tileGrid);
    private static ArcadeLevels arcadeLevels = new ArcadeLevels(tileGrid);
    private static CampaignLevels campaignLevels = new CampaignLevels(tileGrid);
    private static GameFinishedScreen gameFinishedScreen = new GameFinishedScreen(tileGrid);
    private static GameOverScreen gameOverScreen = new GameOverScreen(tileGrid);
    private static GameScreen gameScreen = new GameScreen(tileGrid);
    private static LevelCompleteScreen levelCompleteScreen = new LevelCompleteScreen(tileGrid);

    private static Screen mutableMenu = menu.getMenuScreen();
    private static Screen mutableShop = shop.getShopScreen();
    private static Screen mutableArcadeLevels = arcadeLevels.getLevelScreen();
    private static Screen mutableCampaignLevels = campaignLevels.getCampaignLevelScreen();
    private static Screen mutableGameFinishedScreen = gameFinishedScreen.getGameFinishedScreen();
    private static Screen mutableGameOverScreen = gameOverScreen.getGameOverScreen();
    private static Screen mutableGameScreen = gameScreen.getGameScreen();
    private static Screen mutableLevelCompleteScreen = levelCompleteScreen.getLevelCompleteScreen();

    public static Screen getMutableGameScreen() {
        return mutableGameScreen;
    }
    //private static String rolledParagraph;

    public static void writeResultToFile() {
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String difficultyLevel;
        if (stats == easyStats) {
            difficultyLevel = "Easy";
        } else if (stats == medStats) {
            difficultyLevel = "Medium";
        } else if (stats == hardStats) {
            difficultyLevel = "Hard";
        } else if (stats == extremeStats) {
            difficultyLevel = "Extreme";
        } else {
            difficultyLevel = "Insane";
        }
        try {
            String filename = "Leaderboards.txt";
            FileWriter fw = new FileWriter(filename, Charset.forName("UTF-8"), true);
            fw.write(timestamp + " " + winOrLose + " " + difficultyLevel + " " + points + "\n");
            fw.close();
        } catch (IOException ex) {
            System.out.println("Oih");
        }
    }

    public static void clearLastGame() {
        TypingSupport.setStartOrStopSupport(false);
        stopLaunchGame = true;
        tileGrid.clear();
        livesLeft = 3;
        points = 0;
        wordsOnScreen = new HashMap<>();
        gameRunning = false;
        mutableGameScreen.write("Points:         ", Positions.create(60, 42)).invoke();
    }

    public static void checkIfGameOver() {
        if (livesLeft == 0) {
            if (gameStyle.equals("ARCADE")) {
                mutableGameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                winOrLose = "LOST";
                writeResultToFile();
            }
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            clearLastGame();
            mutableGameOverScreen.display();
        }
    }

    public static void launchGame(Screen gameScreen) throws Exception { // Loob igale sõnale ThreadWords'i isendi ja kuvab sõnu ekraanile.
        WordSpawner spawner = new WordSpawner();
        WordDrawer drawer = new WordDrawer(gameScreen);
        TypingSupport support = new TypingSupport();
        support.startTypingSupport(gameScreen);
        for (int i = 0; i <= stats[2]; i++) {
            checkIfGameOver();
            //System.out.println(getWordsOnScreen().toString());
            rolledWord = spawner.rollNext();
            loc = spawner.getLoc();
            /*
            rolledParagraph = spawner.rollNextPara(i);
            if (currentLevel == 10) {
                drawer.drawParagraph(rolledParagraph);
                wordLayer = drawer.getParaLayer();
                wordsOnScreen.put(rolledWord, wordLayer);
            }
            */
            drawer.drawWords(rolledWord);
            wordLayer = drawer.getWordLayer();
            wordsOnScreen.put(rolledWord, wordLayer);

            tekstid.add(wordLayer);
            ThreadWords threadWords = new ThreadWords(wordLayer);
            Thread t_threadWords = new Thread(threadWords);
            t_threadWords.start();
            livesLeft = ThreadWords.getLivesLeft();
            Thread.sleep(stats[0]);
            if (stopLaunchGame) {
                TypingSupport.setStartOrStopSupport(false);
                break;
            }
        }

        while (gameRunning) { // Kontrollitakse, kas mäng on läbi. Võidu või kaotuse korral kuvatakse punktide arv.
            if (livesLeft == 0) {
                gameRunning = false;
                if (gameStyle.equals("ARCADE")) {
                    mutableGameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                    winOrLose = "LOST";
                    writeResultToFile();
                    mutableGameOverScreen.display();
                } else {
                    clearLastGame();
                    mutableGameOverScreen.display();
                }

            } else if (wordsOnScreen.isEmpty()) {
                gameRunning = false;
                if (gameStyle.equals("ARCADE")) {
                    mutableGameFinishedScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                    winOrLose = "WIN";
                    writeResultToFile();
                    clearLastGame();
                    mutableGameFinishedScreen.display();
                } else if (gameStyle.equals("CAMPAIGN") && currentLevel == 9) {
                    clearLastGame();
                    mutableGameFinishedScreen.display();
                } else {
                    clearLastGame();
                    mutableLevelCompleteScreen.display();
                }
            }
            Thread.sleep(500);
        }
    }


    public static void main(String[] args) throws Exception {
        try {
            File f = new File("Saves.txt");
            File f2 = new File("Leaderboards.txt");
            if (!f.exists() || f.length() == 0) {
                Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
                FileWriter fw = new FileWriter("Saves.txt");
                fw.write("1,1,0,3,false,false,false," + timestamp + "\n"); //booster - nuke - slow-mo
                fw.write("2,1,0,3,false,false,false," + timestamp + "\n");
                fw.write("3,1,0,3,false,false,false," + timestamp + "\n");
                fw.close();
            }
            if (!f2.exists()) {
                f2.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        menu.build(mutableCampaignLevels, mutableArcadeLevels, scores);
        shop.build(mutableShop, mutableLevelCompleteScreen);
        arcadeLevels.build(mutableGameScreen, mutableMenu);
        campaignLevels.build(mutableGameScreen, mutableMenu);
        gameFinishedScreen.build(mutableGameScreen, mutableMenu);
        gameOverScreen.build(mutableGameScreen, mutableMenu);
        gameScreen.build(mutableMenu);
        levelCompleteScreen.build(mutableGameScreen, mutableShop);

        menu.display();
    }
}
