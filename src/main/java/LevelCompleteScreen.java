import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

//Leveli lõpetamise ekraan
public class LevelCompleteScreen extends GameVars {
    private final TileGrid tileGrid;
    private final Screen levelCompleteScreen;


    public LevelCompleteScreen(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.levelCompleteScreen = Screens.createScreenFor(tileGrid);
    }

    // Save kirjutamine faili
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
                save[3] = String.valueOf(livesLeft);
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

    // Leveli lõpetamise screeni ehitamine (tekst, nupud), nuppude aktiveerimine
    public void build(Screen gameScreen, Screen shopScreen) {
        Button shopButton = Components.button().withText("SHOP").withPosition(Positions.create(21, 30)).build();
        Button next_level = Components.button().withText("NEXT LEVEL").withPosition(Positions.create(54, 30)).build();
        Button saveSlot1 = Components.button().withText("SAVE IN SLOT 1").withPosition(Positions.create(33, 16)).build();
        Button saveSlot2 = Components.button().withText("SAVE IN SLOT 2").withPosition(Positions.create(33, 20)).build();
        Button saveSlot3 = Components.button().withText("SAVE IN SLOT 3").withPosition(Positions.create(33, 24)).build();
        levelCompleteScreen.write("LEVEL COMPLETED!", Positions.create(33, 10)).invoke();
        levelCompleteScreen.addComponent(next_level);
        levelCompleteScreen.addComponent(saveSlot1);
        levelCompleteScreen.addComponent(saveSlot2);
        levelCompleteScreen.addComponent(saveSlot3);
        levelCompleteScreen.addComponent(shopButton);
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
                    Game.launchGame(gameScreen);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });
        shopButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            shopScreen.display();
            shopScreen.write("Credits: " + totalPoints, Positions.create(30, 3)).invoke();
            shopScreen.write(" ".repeat(90) + "xd", Positions.create(10, 40)).invoke();
            return UIEventResponses.preventDefault();
        });
        saveSlot1.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.write("Saved in slot 1!", Positions.create(33, 12)).invoke();
            saveClicked = 1;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.write("Saved in slot 2!", Positions.create(33, 12)).invoke();
            saveClicked = 2;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
        saveSlot3.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.write("Saved in slot 3!", Positions.create(33, 12)).invoke();
            saveClicked = 3;
            writeSaveState();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getLevelCompleteScreen() {
        return levelCompleteScreen;
    }

    public void display() {
        levelCompleteScreen.display();
    }
}
