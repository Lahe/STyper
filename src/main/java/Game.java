import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.builder.component.ButtonBuilder;
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
import java.util.*;

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
    private static int points;
    private static int totalPoints;
    private static final Screen menuScreen = Screens.createScreenFor(tileGrid);
    private static final Screen levelScreen = Screens.createScreenFor(tileGrid);
    private static final Screen scoreScreen = Screens.createScreenFor(tileGrid);
    private static final Screen shopScreen = Screens.createScreenFor(tileGrid);
    private static final Screen campaignLevelScreen = Screens.createScreenFor(tileGrid);
    private static final Screen savesScreen = Screens.createScreenFor(tileGrid);
    private static final Screen levelCompleteScreen = Screens.createScreenFor(tileGrid);
    private static final Screen chooseSave = Screens.createScreenFor(tileGrid);
    private static final Button shopButton = Components.button().withText("SHOP").withPosition(Positions.create(20, 30)).build();
    private static final Button campaignButton = Components.button().withText("CAMPAIGN").withPosition(Positions.create(38, 17)).build();
    private static final Button scoreButton = Components.button().withText("SCORES").withPosition(Positions.create(38, 23)).build();
    private static final Button arcadeButton = Components.button().withText("ARCADE MODE").withPosition(Positions.create(38, 20)).build();
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
    private static final Button continueButton = Components.button().withText("CONTINUE").withPosition(Positions.create(38, 21)).build();
    private static final Button newGameButton = Components.button().withText("NEW GAME").withPosition(Positions.create(38, 23)).build();
    private static final Button backToMenuButton = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
    private static final Button backToMenuButton2 = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
    private static final Button backToMenuButton3 = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
    private static final Button backToMenuButton4 = Components.button().withText("QUIT").withPosition(Positions.offset1x1()).build();
    private static final Button backToLevelCompleteScreen = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
    private static final Button next_level = Components.button().withText("NEXT LEVEL").withPosition(Positions.create(60, 30)).build();
    private static final Button saveSlot1 = Components.button().withText("SAVE IN SLOT 1").withPosition(Positions.create(33, 16)).build();
    private static final Button saveSlot2 = Components.button().withText("SAVE IN SLOT 2").withPosition(Positions.create(33, 20)).build();
    private static final Button saveSlot3 = Components.button().withText("SAVE IN SLOT 3").withPosition(Positions.create(33, 24)).build();
    private static int saveClicked;
    private static int[] stats = new int[3];
    private static final int[] easyStats = {2000, 2000, 15};
    private static final int[] medStats = {1300, 1300, 30};
    private static final int[] hardStats = {1000, 1000, 60};
    private static final int[] extremeStats = {750, 750, 60};
    private static final int[] insaneStats = {500, 500, 60};

    private static int currentLevel;
    private static final int[] level1Stats = {2000, 2000, 5};
    private static final int[] level2Stats = {2000, 2000, 10};
    private static final int[] level3Stats = {1500, 1500, 15};
    private static final int[] level4Stats = {1500, 1500, 20};
    private static final int[] level5Stats = {1500, 1500, 25};
    private static final int[] level6Stats = {1000, 1000, 30};
    private static final int[] level7Stats = {1000, 1000, 35};
    private static final int[] level8Stats = {1000, 1000, 40};
    private static final int[] level9Stats = {750, 750, 55};
    private static final int[][] campaignStats = {level1Stats, level2Stats, level3Stats, level4Stats, level5Stats, level6Stats, level7Stats, level8Stats, level9Stats};

    private static List<Layer> tekstid = new ArrayList<>(); //
    private static boolean stopLaunchGame = false;
    private static boolean gameRunning = true;
    private static String winOrLose;
    public static List<Integer> easyTops = new ArrayList<>();
    public static List<Integer> mediumTops = new ArrayList<>();
    public static List<Integer> hardTops = new ArrayList<>();
    public static List<Integer> extremeTops = new ArrayList<>();
    public static List<Integer> insaneTops = new ArrayList<>();
    public static List<String> save1 = new ArrayList<>();
    public static List<String> save2 = new ArrayList<>();
    public static List<String> save3 = new ArrayList<>();


    private static String gameStyle = "";


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

    public static String getGameStyle() {
        return gameStyle;
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
            String filename = "Leaderboards.txt";
            FileWriter fw = new FileWriter(filename, true);
            fw.write(timestamp + " " + winOrLose + " " + difficultyLevel + " " + getPoints() + "\n");
            fw.close();
        } catch (IOException ex) {
            System.out.println("Oih");
        }
    }

    public static void writeSaveState() {
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        try {
            String filename = "Saves.txt";
            FileWriter fw = new FileWriter(filename, true);
            fw.write(saveClicked + " " + currentLevel + " " + getPoints() + " " + getLivesLeft() + " " + timestamp + "\n");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSave1() {
        System.out.println(save1);
        currentLevel = Integer.parseInt(save1.get(0));
        setPoints(Integer.parseInt(save1.get(1)));
        livesLeft = Integer.parseInt(save1.get(2));
        stats = campaignStats[currentLevel - 1];
    }

    public static void loadSave2() {
        currentLevel = Integer.parseInt(save2.get(0));
        setPoints(Integer.parseInt(save2.get(1)));
        livesLeft = Integer.parseInt(save2.get(2));
        stats = campaignStats[currentLevel - 1];
    }

    public static void loadSave3() {
        currentLevel = Integer.parseInt(save3.get(0));
        setPoints(Integer.parseInt(save3.get(1)));
        livesLeft = Integer.parseInt(save3.get(2));
        stats = campaignStats[currentLevel - 1];

    }

    public static void loadSaveState() throws Exception {
        String rida;
        try (BufferedReader in = new BufferedReader((new FileReader("Saves.txt")));
        ) {
            while ((rida = in.readLine()) != null) {
                String[] line = rida.split(" ");
                if (line[0].equals("1")) {
                    save1.add(line[1]);
                    save1.add(line[2]);
                    save1.add(line[3]);
                }
                if (line[0].equals("2")) {
                    save2.add(line[1]);
                    save2.add(line[2]);
                    save2.add(line[3]);
                }
                if (line[0].equals("3")) {
                    save3.add(line[1]);
                    save3.add(line[2]);
                    save3.add(line[3]);
                }
            }
        }
    }

    public static void scoresFromFile() {
        String rida;
        try (
                BufferedReader in = new BufferedReader((new FileReader("Leaderboards.txt")));
        ) {
            while ((rida = in.readLine()) != null) {
                String[] line = rida.split(" ");
                if (line[3].equals("Easy")) {
                    int score = Integer.parseInt(line[4]);
                    if (easyTops.size() < 5) {
                        easyTops.add(score);
                    } else {
                        if (Collections.min(easyTops) < score) {
                            easyTops.set(easyTops.indexOf(Collections.min(easyTops)), score);
                        }
                    }
                } else if (line[3].equals("Medium")) {
                    int score = Integer.parseInt(line[4]);
                    if (mediumTops.size() < 5) {
                        mediumTops.add(score);
                    } else {
                        if (Collections.min(mediumTops) < score) {
                            mediumTops.set(mediumTops.indexOf(Collections.min(mediumTops)), score);
                        }
                    }
                } else if (line[3].equals("Hard")) {
                    int score = Integer.parseInt(line[4]);
                    if (hardTops.size() < 5) {
                        hardTops.add(score);
                    } else {
                        if (Collections.min(hardTops) < score) {
                            hardTops.set(hardTops.indexOf(Collections.min(hardTops)), score);
                        }
                    }
                } else if (line[3].equals("Insane")) {
                    int score = Integer.parseInt(line[4]);
                    if (insaneTops.size() < 5) {
                        insaneTops.add(score);
                    } else {
                        if (Collections.min(insaneTops) < score) {
                            insaneTops.set(insaneTops.indexOf(Collections.min(insaneTops)), score);
                        }
                    }
                } else if (line[3].equals("Extreme")) {
                    int score = Integer.parseInt(line[4]);
                    if (extremeTops.size() < 5) {
                        extremeTops.add(score);
                    } else {
                        if (Collections.min(extremeTops) < score) {
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

    public static String getScore(List<Integer> list, int index) {
        if (list.size() > index) {
            return Integer.toString(list.get(index));
        }
        return "-";
    }

    public static void showScores() {
        scoresFromFile();
        Panel easyPanel = Components.panel()
                .withPosition(Positions.create(19, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(15, 15)
                .withTitle("Easy")
                .wrapWithBox(true)
                .build();

        TextBox easyBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(easyTops, 0))
                .addParagraph("2ND    " + getScore(easyTops, 1))
                .addParagraph("3RD    " + getScore(easyTops, 2))
                .addParagraph("4TH    " + getScore(easyTops, 3))
                .addParagraph("5TH    " + getScore(easyTops, 4))
                .build();

        Panel mediumPanel = Components.panel()
                .withPosition(Positions.create(36, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(15, 15)
                .withTitle("Medium")
                .wrapWithBox(true)
                .build();

        TextBox mediumBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(mediumTops, 0))
                .addParagraph("2ND    " + getScore(mediumTops, 1))
                .addParagraph("3RD    " + getScore(mediumTops, 2))
                .addParagraph("4TH    " + getScore(mediumTops, 3))
                .addParagraph("5TH    " + getScore(mediumTops, 4))
                .build();

        Panel hardPanel = Components.panel()
                .withPosition(Positions.create(53, 8)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(15, 15)
                .withTitle("Hard")
                .wrapWithBox(true)
                .build();

        TextBox hardBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(hardTops, 0))
                .addParagraph("2ND    " + getScore(hardTops, 1))
                .addParagraph("3RD    " + getScore(hardTops, 2))
                .addParagraph("4TH    " + getScore(hardTops, 3))
                .addParagraph("5TH    " + getScore(hardTops, 4))
                .build();

        Panel insanePanel = Components.panel()
                .withPosition(Positions.create(28, 24)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(15, 15)
                .withTitle("Insane")
                .wrapWithBox(true)
                .build();
        // insanePanel.applyColorTheme(ColorThemeResource.AMIGA_OS.getTheme()); Teema lisamine
        TextBox insaneBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(insaneTops, 0))
                .addParagraph("2ND    " + getScore(insaneTops, 1))
                .addParagraph("3RD    " + getScore(insaneTops, 2))
                .addParagraph("4TH    " + getScore(insaneTops, 3))
                .addParagraph("5TH    " + getScore(insaneTops, 4))
                .build();

        Panel extremePanel = Components.panel()
                .withPosition(Positions.create(45, 24)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(15, 15)
                .withTitle("Extreme")
                .wrapWithBox(true)
                .build();

        TextBox extremeBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(extremeTops, 0))
                .addParagraph("2ND    " + getScore(extremeTops, 1))
                .addParagraph("3RD    " + getScore(extremeTops, 2))
                .addParagraph("4TH    " + getScore(extremeTops, 3))
                .addParagraph("5TH    " + getScore(extremeTops, 4))
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
        scoreScreen.addComponent(backToMenuButton3);
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
            if (gameStyle.equals("ARCADE")) {
                gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                winOrLose = "LOST";
                writeResultToFile();
                clearLastGame();
                gameOverScreen.display();
            } else {
                clearLastGame();
                levelCompleteScreen.display();
            }
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
                    gameOverScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                    winOrLose = "LOST";
                    writeResultToFile();
                    gameOverScreen.display();
                } else {
                    gameRunning = false;
                    clearLastGame();
                    levelCompleteScreen.display();
                }

            } else if (wordsOnScreen.isEmpty()) {
                if (gameStyle.equals("ARCADE")) {
                    gameRunning = false;
                    gameFinishedScreen.write("Points: " + points, Positions.create(38, 21)).invoke();
                    winOrLose = "WIN";
                    writeResultToFile();
                    clearLastGame();
                    gameFinishedScreen.display();
                } else {
                    gameRunning = false;
                    clearLastGame();
                    levelCompleteScreen.display();
                }
            }
            Thread.sleep(500);
        }
    }

    public static void saveItems() throws Exception {
        Button choose1 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        Button choose2 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        Button choose3 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        loadSaveState();
        Panel savePanel1 = Components.panel()
                .withPosition(Positions.create(13, 16))
                .withSize(18, 11)
                .withTitle("Save 1")
                .wrapWithBox(true)
                .build();
        TextBox saveBox1 = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(16)
                .addParagraph("Level: " + save1.get(0))
                .addParagraph("Credits: " + save1.get(1))
                .addParagraph("Lives: " + save1.get(2))
                .build();

        Panel savePanel2 = Components.panel()
                .withPosition(Positions.create(34, 16))
                .withSize(18, 11)
                .withTitle("Save 2")
                .wrapWithBox(true)
                .build();
        TextBox saveBox2 = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(16)
                .addParagraph("Level: " + save2.get(0))
                .addParagraph("Credits: " + save2.get(1))
                .addParagraph("Lives: " + save2.get(2))
                .build();

        Panel savePanel3 = Components.panel()
                .withPosition(Positions.create(55, 16))
                .withSize(18, 11)
                .withTitle("Save 3")
                .wrapWithBox(true)
                .build();
        TextBox saveBox3 = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(16)
                .addParagraph("Level: " + save3.get(0))
                .addParagraph("Credits: " + save3.get(1))
                .addParagraph("Lives: " + save3.get(2))
                .build();
        choose1.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                loadSave1();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameRunning = true;
            stopLaunchGame = false;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
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
        choose2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                loadSave2();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameRunning = true;
            stopLaunchGame = false;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
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
        choose3.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                loadSave3();
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameRunning = true;
            stopLaunchGame = false;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: 1", Positions.create(73, 1)).invoke();
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

        savePanel1.addComponent(saveBox1);
        savePanel2.addComponent(saveBox2);
        savePanel3.addComponent(saveBox3);
        savePanel1.addComponent(choose1);
        savePanel2.addComponent(choose2);
        savePanel3.addComponent(choose3);
        chooseSave.addComponent(savePanel1);
        chooseSave.addComponent(savePanel2);
        chooseSave.addComponent(savePanel3);
    }


    public static void shopItems() {
        Panel boosterPanel = Components.panel()
                .withPosition(Positions.create(30, 5)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(26, 8)
                .withTitle("Booster")
                .wrapWithBox(true)
                .build();

        Button buyBooster = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        buyBooster.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            //lisab boosteritesse +1
            return UIEventResponses.preventDefault();
        });

        TextBox boosterBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(24)
                .addParagraph("Boosts point gain by 2x")
                .addParagraph("Cost: 1000")
                .build();

        Panel nukePanel = Components.panel()
                .withPosition(Positions.create(30, 13)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(26, 8)
                .withTitle("Nuke")
                .wrapWithBox(true)
                .build();
        Button buyNuke = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        buyNuke.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            //lisab nukedesse +1
            return UIEventResponses.preventDefault();
        });

        TextBox nukeBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(24)
                .addParagraph("Everything die")
                .addParagraph("Cost: 2500")
                .build();

        Panel slowPanel = Components.panel()
                .withPosition(Positions.create(30, 21)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(26, 8)
                .withTitle("Slow-mo")
                .wrapWithBox(true)
                .build();
        Button buySlow = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        buySlow.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            //lisab slowmodesse +1
            return UIEventResponses.preventDefault();
        });

        TextBox slowBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(24)
                .addParagraph("Slows down words")
                .addParagraph("Cost: 1000")
                .build();

        Panel livesPanel = Components.panel()
                .withPosition(Positions.create(30, 29)) // X 36 KESKMISE JAOKS 4 on vahe
                .withSize(26, 8)
                .withTitle("Lives")
                .wrapWithBox(true)
                .build();
        Button buyLives = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        buyLives.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            //lisab eludesse +1
            return UIEventResponses.preventDefault();
        });

        TextBox livesBox = Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(24)
                .addParagraph("Gain 1 extra life")
                .addParagraph("Cost: 5000")
                .build();

        boosterPanel.addComponent(boosterBox);
        boosterPanel.addComponent(buyBooster);
        nukePanel.addComponent(nukeBox);
        nukePanel.addComponent(buyNuke);
        slowPanel.addComponent(slowBox);
        slowPanel.addComponent(buySlow);
        livesPanel.addComponent(livesBox);
        livesPanel.addComponent(buyLives);

        shopScreen.addComponent(boosterPanel);
        shopScreen.addComponent(nukePanel);
        shopScreen.addComponent(slowPanel);
        shopScreen.addComponent(livesPanel);
        shopScreen.addComponent(backToLevelCompleteScreen);
    }

    public static void main(String[] args) throws Exception {
        try {
            File f = new File("Saves.txt");
            if (!f.exists()) {
                Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
                FileWriter fw = new FileWriter("Saves.txt");
                fw.write("1 1 0 3" + timestamp + "\n");
                fw.write("2 1 0 3" + timestamp + "\n");
                fw.write("3 1 0 3" + timestamp + "\n");
                fw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        menuScreen.addComponent(campaignButton);
        menuScreen.addComponent(arcadeButton);
        menuScreen.addComponent(scoreButton);
        gameOverScreen.write("GAME OVER", Positions.create(38, 20)).invoke();
        gameFinishedScreen.write("Game Finished. Well done!", Positions.create(32, 20)).invoke();

        menuScreen.display();
        gameOverScreen.addComponent(playAgainButton);
        gameFinishedScreen.addComponent(playAgainButtonFinishedScreen);
        levelScreen.write("Arcade mode", Positions.create(36, 10)).invoke();
        levelScreen.write("Choose level", Positions.create(36, 17)).invoke();
        levelScreen.addComponent(easyButton);
        levelScreen.addComponent(medButton);
        levelScreen.addComponent(hardButton);
        levelScreen.addComponent(extremeButton);
        levelScreen.addComponent(insaneButton);
        levelScreen.addComponent(backToMenuButton);
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

        campaignLevelScreen.addComponent(shopButton);
        campaignLevelScreen.addComponent(continueButton);
        campaignLevelScreen.addComponent(newGameButton);
        campaignLevelScreen.addComponent(backToMenuButton2);

        levelCompleteScreen.write("LEVEL COMPLETED", Positions.create(33, 10)).invoke();
        levelCompleteScreen.addComponent(next_level);
        levelCompleteScreen.addComponent(saveSlot1);
        levelCompleteScreen.addComponent(saveSlot2);
        levelCompleteScreen.addComponent(saveSlot3);
        levelCompleteScreen.addComponent(shopButton);

        saveItems();
        continueButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            chooseSave.display();
            return UIEventResponses.preventDefault();
        });

        saveSlot1.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            saveClicked = 1;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            saveClicked = 2;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            saveClicked = 3;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });


        newGameButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = campaignStats[0];
            currentLevel = 1;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
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

        campaignButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameStyle = "CAMPAIGN";
            campaignLevelScreen.display();
            return UIEventResponses.preventDefault();
        });

        arcadeButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameStyle = "ARCADE";
            levelScreen.display();
            return UIEventResponses.preventDefault();
        });

        shopItems();
        shopButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            shopScreen.display();
            return UIEventResponses.preventDefault();
        });

        showScores();
        scoreButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            scoreScreen.display();
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
            t.start();
            return UIEventResponses.preventDefault();
        });

        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> { // BACK nuppu vajutades kustutab teksti layerid ja taastab elud ning algse punktiseisu.
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            clearLastGame();
            gameScreen.removeComponent(backButton);
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });

        next_level.onComponentEvent(ComponentEventType.ACTIVATED, (event)-> {
            gameRunning = true;
            stopLaunchGame = false;
            currentLevel++;
            stats = campaignStats[currentLevel-1];
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
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

        backToMenuButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
        backToMenuButton2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
        backToMenuButton3.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
        backToLevelCompleteScreen.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.display();
            return UIEventResponses.preventDefault();
        });
        backToMenuButton4.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }
}
