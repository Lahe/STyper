import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.builder.component.LabelBuilder;
import org.hexworks.zircon.api.builder.component.TextBoxBuilder;
import org.hexworks.zircon.api.component.*;
import org.hexworks.zircon.api.graphics.BoxType;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.resource.ColorThemeResource;
import org.hexworks.zircon.api.resource.TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.tileset.Tileset;
import org.hexworks.zircon.api.uievent.ComponentEventType;
import org.hexworks.zircon.internal.component.renderer.NoOpComponentRenderer;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final Screen scoreScreen = Screens.createScreenFor(tileGrid);
    private static final Button scoreButton = Components.button().withText("SCORES").withPosition(Positions.create(38, 23)).build();
    private static final Button startButton = Components.button().withText("START").withPosition(Positions.create(38, 20)).build();
    private static final Button playAgainButton = Components.button().withText("PLAY AGAIN").withPosition(Positions.create(37, 23)).build();
    private static final Button playAgainButtonFinishedScreen = Components.button().withText("PLAY AGAIN").withPosition(Positions.create(37, 23)).build();
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
    private static boolean gameRunning = true;
    private static String winOrLose;
    public static List<Integer> easyTops = new ArrayList<>();
    public static List<Integer> mediumTops = new ArrayList<>();
    public static List<Integer> hardTops = new ArrayList<>();
    public static List<Integer> extremeTops = new ArrayList<>();
    public static List<Integer> insaneTops = new ArrayList<>();

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

    public static void writeResultToFile() {
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        String difficultyLevel = "";
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
            String filename = "Game_results.txt";
            FileWriter fw = new FileWriter(filename, true);
            fw.write(timestamp + " " + winOrLose + " " + difficultyLevel + " " + getPoints() + "\n");
            fw.close();
        } catch (IOException ex) {
            System.out.println("Oih");
        }
    }
    public static void scoresFromFile(){
        String rida;
        try(
                BufferedReader in = new BufferedReader((new FileReader("Game_results.txt")));
        ){
            while((rida = in.readLine()) != null) {
                String[] line = rida.split(" ");
                if (line[3].equals("Easy")) {
                    int score = Integer.parseInt(line[4]);
                    if(easyTops.size() < 5){
                        easyTops.add(score);
                    }
                    else{
                        if(Collections.min(easyTops) < score){
                            easyTops.set(easyTops.indexOf(Collections.min(easyTops)), score);
                        }
                    }
                }
                else if(line[3].equals("Medium")){
                    int score = Integer.parseInt(line[4]);
                    if(mediumTops.size() < 5){
                        mediumTops.add(score);
                    }
                    else{
                        if(Collections.min(mediumTops) < score){
                            mediumTops.set(mediumTops.indexOf(Collections.min(mediumTops)), score);
                        }
                    }
                }
                else if(line[3].equals("Hard")){
                    int score = Integer.parseInt(line[4]);
                    if(hardTops.size() < 5){
                        hardTops.add(score);
                    }
                    else{
                        if(Collections.min(hardTops) < score){
                            hardTops.set(hardTops.indexOf(Collections.min(hardTops)), score);
                        }
                    }
                }
                else if(line[3].equals("Insane")){
                    int score = Integer.parseInt(line[4]);
                    if(insaneTops.size() < 5){
                        insaneTops.add(score);
                    }
                    else{
                        if(Collections.min(insaneTops) < score){
                            insaneTops.set(insaneTops.indexOf(Collections.min(insaneTops)), score);
                        }
                    }
                }
                else if(line[3].equals("Extreme")){
                    int score = Integer.parseInt(line[4]);
                    if(extremeTops.size() < 5){
                        extremeTops.add(score);
                    }
                    else{
                        if(Collections.min(extremeTops) < score){
                            extremeTops.set(extremeTops.indexOf(Collections.min(extremeTops)), score);
                        }
                    }
                }
            }
            Collections.sort(easyTops, Collections.reverseOrder());
            Collections.sort(mediumTops, Collections.reverseOrder());
            Collections.sort(hardTops, Collections.reverseOrder());
            Collections.sort(insaneTops, Collections.reverseOrder());
            Collections.sort(extremeTops, Collections.reverseOrder());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getScore(List<Integer> list, int index){
        if(list.size() > index){
            return Integer.toString(list.get(index));
        }
        return "-";
    }

    public static void showScores(){
        scoresFromFile();
        Panel easyPanel = Components.panel()
                .withPosition(Positions.create(19, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(13, 15)
                .withTitle("Easy")
                .wrapWithBox(true)
                .build();

        TextBox easyBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(11)
                .addParagraph("RANK SCORE")
                .addParagraph("1ST  " + getScore(easyTops, 0))
                .addParagraph("2ND  " + getScore(easyTops, 1))
                .addParagraph("3RD  " + getScore(easyTops, 2))
                .addParagraph("4TH  " + getScore(easyTops, 3))
                .addParagraph("5TH  " + getScore(easyTops, 4))
                .build();

        Panel mediumPanel = Components.panel()
                .withPosition(Positions.create(36, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(13, 15)
                .withTitle("Medium")
                .wrapWithBox(true)
                .build();

        TextBox mediumBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(11)
                .addParagraph("RANK SCORE")
                .addParagraph("1ST  " + getScore(mediumTops, 0))
                .addParagraph("2ND  " + getScore(mediumTops, 1))
                .addParagraph("3RD  " + getScore(mediumTops, 2))
                .addParagraph("4TH  " + getScore(mediumTops, 3))
                .addParagraph("5TH  " + getScore(mediumTops, 4))
                .build();

        Panel hardPanel = Components.panel()
                .withPosition(Positions.create(53, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(13, 15)
                .withTitle("Hard")
                .wrapWithBox(true)
                .build();

        TextBox hardBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(11)
                .addParagraph("RANK SCORE")
                .addParagraph("1ST  " + getScore(hardTops, 0))
                .addParagraph("2ND  " + getScore(hardTops, 1))
                .addParagraph("3RD  " + getScore(hardTops, 2))
                .addParagraph("4TH  " + getScore(hardTops, 3))
                .addParagraph("5TH  " + getScore(hardTops, 4))
                .build();

        Panel insanePanel = Components.panel()
                .withPosition(Positions.create(28, 24)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(13, 15)
                .withTitle("Insane")
                .wrapWithBox(true)
                .build();
        // insanePanel.applyColorTheme(ColorThemeResource.AMIGA_OS.getTheme()); Teema lisamine
        TextBox insaneBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(11)
                .addParagraph("RANK SCORE")
                .addParagraph("1ST  " + getScore(insaneTops, 0))
                .addParagraph("2ND  " + getScore(insaneTops, 1))
                .addParagraph("3RD  " + getScore(insaneTops, 2))
                .addParagraph("4TH  " + getScore(insaneTops, 3))
                .addParagraph("5TH  " + getScore(insaneTops, 4))
                .build();

        Panel extremePanel = Components.panel()
                .withPosition(Positions.create(45, 24)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(13, 15)
                .withTitle("Extreme")
                .wrapWithBox(true)
                .build();

        TextBox extremeBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(11)
                .addParagraph("RANK SCORE")
                .addParagraph("1ST  " + getScore(extremeTops, 0))
                .addParagraph("2ND  " + getScore(extremeTops, 1))
                .addParagraph("3RD  " + getScore(extremeTops, 2))
                .addParagraph("4TH  " + getScore(extremeTops, 3))
                .addParagraph("5TH  " + getScore(extremeTops, 4))
                .build();

        easyPanel.addComponent(easyBox);
        mediumPanel.addComponent(mediumBox);
        hardPanel.addComponent(hardBox);
        insanePanel.addComponent(insaneBox);
        extremePanel.addComponent(extremeBox);

        scoreScreen.addComponent(easyPanel);
        scoreScreen.addComponent(mediumPanel);
        scoreScreen.addComponent(hardPanel);
        scoreScreen.addComponent(insanePanel);
        scoreScreen.addComponent(extremePanel);
        scoreScreen.display();
    }

    public static void clearLastGame() {
        TypingSupport.setStartOrStopSupport(false);
        stopLaunchGame = true;
        tileGrid.clear();
        livesLeft = 3;
        setLivesLeft(3);
        setPoints(0);
        TypingSupport.setPoints(0);
        ThreadWords.setLivesLeft(3);
        wordsOnScreen = new HashMap<>();
        gameRunning = false;
    }

    public static void checkIfGameOver() {
        if (livesLeft == 0) {
            gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
            winOrLose = "LOST";
            writeResultToFile();
            clearLastGame();
            gameOverScreen.display();
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
            drawer.drawWords(rolledWord);
            wordLayer = drawer.getWordLayer();
            wordsOnScreen.put(rolledWord, wordLayer);
            tekstid.add(wordLayer);
            ThreadWords threadWords = new ThreadWords(wordLayer);
            Thread t_threadWords = new Thread(threadWords);
            threadid.add(t_threadWords); //
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
                gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                winOrLose = "LOST";
                writeResultToFile();
                gameOverScreen.display();
            } else if (wordsOnScreen.isEmpty()) {
                gameRunning = false;
                gameFinishedScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                winOrLose = "WIN";
                writeResultToFile();
                clearLastGame();
                gameFinishedScreen.display();
            }
            Thread.sleep(500);
        }
    }

    public static void main(String[] args) throws Exception {
        menuScreen.addComponent(startButton);
        menuScreen.addComponent(scoreButton);
        gameOverScreen.write("GAME OVER", Positions.create(38, 20)).invoke();
        gameFinishedScreen.write("Game Finished. Well done!", Positions.create(32, 20)).invoke();
        gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
        gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();

        menuScreen.display();
        levelScreen.write("Choose level", Positions.create(36, 17)).invoke();
        levelScreen.addComponent(easyButton);
        levelScreen.addComponent(medButton);
        levelScreen.addComponent(hardButton);
        levelScreen.addComponent(extremeButton);
        levelScreen.addComponent(insaneButton);
        gameOverScreen.addComponent(playAgainButton);
        gameFinishedScreen.addComponent(playAgainButtonFinishedScreen);
        for (int i = 33; i < 51; i++) {
            levelScreen.write("═", Positions.create(i, 16)).invoke();
            levelScreen.write("═", Positions.create(i, 18)).invoke();
            levelScreen.write("═", Positions.create(i, 28)).invoke();
        }
        levelScreen.write("╔", Positions.create(33, 16)).invoke();
        for (int i = 16; i < 29; i++) {
            levelScreen.write("║", Positions.create(33, i)).invoke();
            levelScreen.write("║", Positions.create(50, i)).invoke();
        }
        levelScreen.write("╬", Positions.create(33, 16)).invoke();
        levelScreen.write("╬", Positions.create(50, 16)).invoke();
        levelScreen.write("╬", Positions.create(33, 18)).invoke();
        levelScreen.write("╬", Positions.create(50, 18)).invoke();
        levelScreen.write("╬", Positions.create(33, 28)).invoke();
        levelScreen.write("╬", Positions.create(50, 28)).invoke();

        startButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelScreen.display();
            return UIEventResponses.preventDefault();
        });

        scoreButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            showScores();
            return UIEventResponses.preventDefault();
        });

        playAgainButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            clearLastGame();
            gameScreen.removeComponent(backButton);
            wordsOnScreen.clear();
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });

        playAgainButtonFinishedScreen.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            clearLastGame();
            gameScreen.removeComponent(backButton);
            wordsOnScreen.clear();
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });

        easyButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = easyStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
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
            gameRunning = true;
            stopLaunchGame = false;
            stats = medStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
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
            gameRunning = true;
            stopLaunchGame = false;
            stats = hardStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
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
            gameRunning = true;
            stopLaunchGame = false;
            stats = extremeStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
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
            gameRunning = true;
            stopLaunchGame = false;
            stats = insaneStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
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
            for (Thread a : threadid) {
                a.stop();
            }
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            clearLastGame();
            gameScreen.removeComponent(backButton);
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }
}
