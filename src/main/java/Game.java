import org.hexworks.zircon.api.*;
import org.hexworks.zircon.api.component.*;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.resource.BuiltInCP437TilesetResource;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.io.*;
import java.nio.charset.Charset;
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
    //private static String rolledParagraph;
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
    private static final int[] level10Stats = {2500, 2500, 10};
    private static final int[][] campaignStats = {level1Stats, level2Stats, level3Stats, level4Stats, level5Stats, level6Stats, level7Stats, level8Stats, level9Stats, level10Stats};

    private static List<Layer> tekstid = new ArrayList<>(); //
    private static boolean stopLaunchGame = false;
    private static boolean gameRunning = true;
    private static String winOrLose;
    private static List<Integer> easyTops = new ArrayList<>();
    private static List<Integer> mediumTops = new ArrayList<>();
    private static List<Integer> hardTops = new ArrayList<>();
    private static List<Integer> extremeTops = new ArrayList<>();
    private static List<Integer> insaneTops = new ArrayList<>();
    private static List<String> save1 = new ArrayList<>();
    private static List<String> save2 = new ArrayList<>();
    private static List<String> save3 = new ArrayList<>();
    private static boolean boosterStatus = false;
    private static boolean nukeStatus = false;
    private static boolean slowStatus = false;
    private static boolean boosterActive = false;

    public static boolean isBoosterActive() {
        return boosterActive;
    }

    public static void setBoosterActive(boolean boosterActive) {
        Game.boosterActive = boosterActive;
    }

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

    public static int getTotalPoints() {
        return totalPoints;
    }

    public static void setTotalPoints(int totalPoints) {
        Game.totalPoints = totalPoints;
    }

    public static boolean isNukeStatus() {
        return nukeStatus;
    }

    public static boolean isSlowStatus() {
        return slowStatus;
    }

    public static boolean isBoosterStatus() {
        return boosterStatus;
    }

    public static void setBoosterStatus(boolean boosterStatus) {
        Game.boosterStatus = boosterStatus;
    }

    public static void setNukeStatus(boolean nukeStatus) {
        Game.nukeStatus = nukeStatus;
    }

    public static void setSlowStatus(boolean slowStatus) {
        Game.slowStatus = slowStatus;
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
            FileWriter fw = new FileWriter(filename, Charset.forName("UTF-8"), true);
            fw.write(timestamp + " " + winOrLose + " " + difficultyLevel + " " + totalPoints + "\n");
            fw.close();
        } catch (IOException ex) {
            System.out.println("Oih");
        }
    }

    public static void writeSaveState() {
        String filename = "Saves.txt";
        Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String row;
            ArrayList<String[]> rows = new ArrayList<>();
            while ((row = br.readLine()) != null) {
                rows.add(row.split(","));
            }
            try (FileWriter fw = new FileWriter(filename)) {
                String[] save = rows.get(saveClicked - 1);
                save[0] = String.valueOf(saveClicked);
                save[1] = String.valueOf(currentLevel + 1);
                save[2] = String.valueOf(totalPoints);
                save[3] = String.valueOf(getLivesLeft());
                save[4] = Boolean.toString(boosterStatus);
                save[5] = Boolean.toString(nukeStatus);
                save[6] = Boolean.toString(slowStatus);
                save[7] = timestamp.toString();
                for (String[] x : rows) {
                    for (String y : x) {
                        fw.write(y + ",");
                    }
                    fw.write("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadSave(List<String> saveInfo) {
        System.out.println(saveInfo);
        currentLevel = Integer.parseInt(saveInfo.get(0));
        setTotalPoints(Integer.parseInt(saveInfo.get(1)));
        livesLeft = Integer.parseInt(saveInfo.get(2));
        stats = campaignStats[currentLevel - 1];
        boosterStatus = Boolean.valueOf(saveInfo.get(3));
        nukeStatus = Boolean.valueOf(saveInfo.get(4));
        slowStatus = Boolean.valueOf(saveInfo.get(5));
    }

    public static void loadSaveState() throws Exception {
        String rida;
        try (BufferedReader in = new BufferedReader((new FileReader("Saves.txt")));
        ) {
            while ((rida = in.readLine()) != null) {
                String[] line = rida.split(",");
                switch (line[0]) {
                    case "1":
                        for (int i = 1; i < 7; i++) {
                            save1.add(line[i]);
                        }
                        break;
                    case "2":
                        for (int i = 1; i < 7; i++) {
                            save2.add(line[i]);
                        }
                        break;
                    case "3":
                        for (int i = 1; i < 7; i++) {
                            save3.add(line[i]);
                        }
                        break;
                }
            }
        }
    }

    public static void addScoreToTopList(List<Integer> tops, String[] line) {
        int score = Integer.parseInt(line[4]);
        if (tops.size() < 5) {
            tops.add(score);
        } else {
            if (Collections.min(tops) < score) {
                tops.set(tops.indexOf(Collections.min(tops)), score);
            }
        }
        Collections.sort(tops, Collections.reverseOrder());
    }

    public static void scoresFromFile() {
        String rida;
        try (BufferedReader in = new BufferedReader((new FileReader("Leaderboards.txt", Charset.forName("UTF-8"))))) {
            while ((rida = in.readLine()) != null) {
                String[] line = rida.split(" ");
                switch (line[3]) {
                    case "Easy":
                        addScoreToTopList(easyTops, line);
                        break;
                    case "Medium":
                        addScoreToTopList(mediumTops, line);
                        break;
                    case "Hard":
                        addScoreToTopList(hardTops, line);
                        break;
                    case "Insane":
                        addScoreToTopList(insaneTops, line);
                        break;
                    case "Extreme":
                        addScoreToTopList(extremeTops, line);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getScore(List<Integer> list, int index) {
        if (list.size() > index) {
            return Integer.toString(list.get(index));
        }
        return "-";
    }

    public static TextBox buildScoreTextBoxes(List<Integer> tops) {
        return Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(tops, 0))
                .addParagraph("2ND    " + getScore(tops, 1))
                .addParagraph("3RD    " + getScore(tops, 2))
                .addParagraph("4TH    " + getScore(tops, 3))
                .addParagraph("5TH    " + getScore(tops, 4))
                .build();
    }

    public static Panel buildScorePanels(int x, int y, String title) {
        return Components.panel()
                .withPosition(Positions.create(x, y))
                .withSize(15, 15)
                .withTitle(title)
                .wrapWithBox(true)
                .build();
    }

    public static void showScores() {
        final Button backToMenuButtonScores = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        backToMenuButtonScores.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
        scoresFromFile();
        Panel easyPanel = buildScorePanels(19, 8, "Easy");
        TextBox easyBox = buildScoreTextBoxes(easyTops);
        Panel mediumPanel = buildScorePanels(36, 8, "Medium");
        TextBox mediumBox = buildScoreTextBoxes(mediumTops);
        Panel hardPanel = buildScorePanels(53, 8, "Hard");
        TextBox hardBox = buildScoreTextBoxes(hardTops);
        Panel insanePanel = buildScorePanels(28, 24, "Insane");
        TextBox insaneBox = buildScoreTextBoxes(insaneTops);
        Panel extremePanel = buildScorePanels(45, 24, "Extreme");
        TextBox extremeBox = buildScoreTextBoxes(extremeTops);

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
        scoreScreen.addComponent(backToMenuButtonScores);
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

    public static Panel buildSavePanels(int x, int y, String title) {
        return Components.panel()
                .withPosition(Positions.create(x, y))
                .withSize(18, 11)
                .withTitle(title)
                .wrapWithBox(true)
                .build();
    }

    public static TextBox buildSaveTextBoxes(List<String> save) {
        return Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(16)
                .addParagraph("Level: " + save.get(0))
                .addParagraph("Credits: " + save.get(1))
                .addParagraph("Lives: " + save.get(2))
                .build();
    }

    public static void activateChooseSaveButton(Button choose, List<String> save) {
        choose.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                loadSave(save);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameRunning = true;
            stopLaunchGame = false;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
            if (boosterStatus) gameScreen.write("Booster: OK", Positions.create(1, 42)).invoke();
            if (nukeStatus) gameScreen.write("Nuke: OK", Positions.create(1, 41)).invoke();
            if (slowStatus) gameScreen.write("Slow-Mo: OK", Positions.create(1, 43)).invoke();
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
    }

    public static void saveItems() throws Exception {
        Button choose1 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        Button choose2 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        Button choose3 = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        loadSaveState();
        Panel savePanel1 = buildSavePanels(15, 16, "Save 1");
        TextBox saveBox1 = buildSaveTextBoxes(save1);
        Panel savePanel2 = buildSavePanels(33, 16, "Save 2");
        TextBox saveBox2 = buildSaveTextBoxes(save2);
        Panel savePanel3 = buildSavePanels(51, 16, "Save 3");
        TextBox saveBox3 = buildSaveTextBoxes(save3);
        activateChooseSaveButton(choose1, save1);
        activateChooseSaveButton(choose2, save2);
        activateChooseSaveButton(choose3, save3);
        Button backToCampaignMenu = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        backToCampaignMenu.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            campaignLevelScreen.display();
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
        chooseSave.addComponent(backToCampaignMenu);
    }

    public static Panel buildShopPanels(int x, int y, String title) {
        return Components.panel()
                .withPosition(Positions.create(x, y))
                .withSize(27, 8)
                .withTitle(title)
                .wrapWithBox(true)
                .build();
    }

    public static TextBox buildShopTextBoxes(String p1, int cost) {
        return Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(25)
                .addParagraph("Boosts credit gain by 2x")
                .addParagraph("Cost: " + cost)
                .build();
    }

    public static void activateArcadeButtons(Button button, int[] arcadeStats, String difficulty) {
        button.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = arcadeStats;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + difficulty, Positions.create(70, 1)).invoke();
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
    }

    public static void activateShopButtons(Button buyItem, String item, boolean itemStatus) {
        buyItem.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            if (itemStatus) shopScreen.write(item + " already in inventory", Positions.create(30, 40)).invoke();
            else if (totalPoints >= 1000) {
                switch (item) {
                    case "Booster":
                        boosterStatus = true;
                        totalPoints -= 1000;
                        break;
                    case "Nuke":
                        nukeStatus = true;
                        totalPoints -= 2500;
                        break;
                    case "Slow-mo":
                        slowStatus = true;
                        totalPoints -= 1000;
                        break;
                }
                shopScreen.write(item + " bought. Type \"launch" +
                        item.toLowerCase().replace("-", "") +
                        "\" in-game to activate.", Positions.create(10, 40)).invoke();
                shopScreen.write("Credits: " + totalPoints + " ".repeat(10), Positions.create(30, 3)).invoke();

            } else shopScreen.write("Not enough dollar" + " ".repeat(20), Positions.create(30, 40)).invoke();
            return UIEventResponses.preventDefault();
        });
    }

    public static void shopItems() {
        Panel boosterPanel = buildShopPanels(29, 5, "Booster");
        TextBox boosterBox = buildShopTextBoxes("Boosts credit gain by 2x", 1000);
        Panel nukePanel = buildShopPanels(29, 13, "Nuke");
        TextBox nukeBox = buildShopTextBoxes("Everything die", 2500);
        Panel slowPanel = buildShopPanels(29, 21, "Slow-mo");
        TextBox slowBox = buildShopTextBoxes("Slows down words by 2x", 1000);
        Panel livesPanel = buildShopPanels(29, 29, "Lives");
        TextBox livesBox = buildShopTextBoxes("Gain 1 extra life", 5000);

        Button buyBooster = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buyNuke = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buySlow = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buyLives = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();

        activateShopButtons(buyBooster, "Booster", boosterStatus);
        activateShopButtons(buyNuke, "Nuke", nukeStatus);
        activateShopButtons(buySlow, "Slow-mo", slowStatus);

        buyLives.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            if (totalPoints >= 5000) {
                totalPoints -= 5000;
                shopScreen.write("Credits: " + totalPoints + " ".repeat(10), Positions.create(30, 3)).invoke();
                shopScreen.write("1 extra life bought" + " ".repeat(20), Positions.create(30, 40)).invoke();

                livesLeft += 1;
            } else shopScreen.write("Not enough dollar" + " ".repeat(20), Positions.create(30, 40)).invoke();
            return UIEventResponses.preventDefault();
        });
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
            if (!f.exists() || f.length() == 0) {
                Timestamp timestamp = new java.sql.Timestamp(System.currentTimeMillis());
                FileWriter fw = new FileWriter("Saves.txt");
                fw.write("1,1,0,3,false,false,false," + timestamp + "\n"); //booster - nuke - slow-mo
                fw.write("2,1,0,3,false,false,false," + timestamp + "\n");
                fw.write("3,1,0,3,false,false,false," + timestamp + "\n");
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
            levelCompleteScreen.write("Saved in slot 1", Positions.create(33, 12)).invoke();
            saveClicked = 1;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.write("Saved in slot 2", Positions.create(33, 12)).invoke();
            saveClicked = 2;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot3.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.write("Saved in slot 3", Positions.create(33, 12)).invoke();
            saveClicked = 3;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });

        newGameButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = campaignStats[0];
            currentLevel = 1;
            totalPoints = 0;
            gameScreen.addComponent(backButton);
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
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
            shopScreen.write("Credits: " + totalPoints, Positions.create(30, 3)).invoke();
            shopScreen.write(" ".repeat(90) + "xd", Positions.create(10, 40)).invoke();
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
        activateArcadeButtons(easyButton, easyStats, "Easy");
        activateArcadeButtons(medButton, medStats, "Medium");
        activateArcadeButtons(hardButton, hardStats, "Hard");
        activateArcadeButtons(extremeButton, extremeStats, "Extreme");
        activateArcadeButtons(insaneButton, insaneStats, "Insane");

        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> { // BACK nuppu vajutades kustutab teksti layerid ja taastab elud ning algse punktiseisu.
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            clearLastGame();
            gameScreen.removeComponent(backButton);
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });

        next_level.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            currentLevel++;
            stats = campaignStats[currentLevel - 1];
            boosterActive = false;
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
            if (boosterStatus) gameScreen.write("Booster: OK", Positions.create(1, 42)).invoke();
            if (nukeStatus) gameScreen.write("Nuke: OK", Positions.create(1, 41)).invoke();
            if (slowStatus) gameScreen.write("Slow-Mo: OK", Positions.create(1, 43)).invoke();
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
