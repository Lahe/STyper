import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.component.Panel;
import org.hexworks.zircon.api.component.TextBox;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public class SaveScreen extends GameVars {
    private final TileGrid tileGrid;
    private final Screen chooseSave;
    private Panel savePanel1;
    private Panel savePanel2;
    private Panel savePanel3;
    private Button backToCampaignMenu;

    public Panel getSavePanel1() {
        return savePanel1;
    }

    public Panel getSavePanel2() {
        return savePanel2;
    }

    public Panel getSavePanel3() {
        return savePanel3;
    }

    public Button getBackToCampaignMenu() {
        return backToCampaignMenu;
    }

    public SaveScreen(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.chooseSave = Screens.createScreenFor(tileGrid);
    }

    public static void loadSave(List<String> saveInfo) {
        currentLevel = Integer.parseInt(saveInfo.get(0));
        totalPoints = Integer.parseInt(saveInfo.get(1));
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

    public static void activateChooseSaveButton(Button choose, List<String> save, Screen gameScreen) {
        choose.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                loadSave(save);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gameRunning = true;
            stopLaunchGame = false;
            gameScreen.addComponent(GameScreen.getBackButton());
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
                    e.printStackTrace();
                }
            };
            Thread t = new Thread(runnable);
            t.start();
            return UIEventResponses.preventDefault();
        });
    }

    public static Panel addContentToPanels(int x, int y, String title, List<String> save, Screen gameScreen) {
        Button choose = Components.button().withText("CHOOSE").withPosition(Positions.create(4, 7)).build();
        Panel savePanel = buildSavePanels(x, y, title);
        TextBox saveBox = buildSaveTextBoxes(save);
        activateChooseSaveButton(choose, save, gameScreen);
        savePanel.addComponent(saveBox);
        savePanel.addComponent(choose);
        return savePanel;
    }

    public void clear(Panel p1, Panel p2, Panel p3, Button b){
        chooseSave.removeComponent(p1);
        chooseSave.removeComponent(p2);
        chooseSave.removeComponent(p3);
        chooseSave.removeComponent(b);
    }

    public void build(Screen gameScreen, Screen campaignLevelScreen) throws Exception {
        backToCampaignMenu = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        loadSaveState();
        savePanel1 = addContentToPanels(15, 16, "Save 1", save1, gameScreen);
        savePanel2 = addContentToPanels(33, 16, "Save 2", save2, gameScreen);
        savePanel3 = addContentToPanels(51, 16, "Save 3", save3, gameScreen);
        chooseSave.addComponent(savePanel1);
        chooseSave.addComponent(savePanel2);
        chooseSave.addComponent(savePanel3);
        chooseSave.addComponent(backToCampaignMenu);
        backToCampaignMenu.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            campaignLevelScreen.display();
            clear(savePanel1,savePanel2,savePanel3,backToCampaignMenu);
            return UIEventResponses.preventDefault();
        });
    }

    public void display() {
        chooseSave.display();
    }
}
