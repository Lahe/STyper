import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

//Campaign mode valikute ekraan
public class CampaignLevels extends GameVars {
    private final TileGrid tileGrid;
    private final Screen campaignLevelScreen;

    public CampaignLevels(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.campaignLevelScreen = Screens.createScreenFor(tileGrid);
    }

    // Ekraani loomine (nuppude paigutamine ja nuppudele reageerimine.)
    public void build(Screen gameScreen, Screen menuScreen) {
        Button continueButton = Components.button().withText("CONTINUE").withPosition(Positions.create(37, 21)).build();
        Button newGameButton = Components.button().withText("NEW GAME").withPosition(Positions.create(37, 23)).build();
        Button backToMenuButton2 = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        campaignLevelScreen.addComponent(continueButton);
        campaignLevelScreen.addComponent(newGameButton);
        campaignLevelScreen.addComponent(backToMenuButton2);
        SaveScreen saveScreen = new SaveScreen(tileGrid);
        continueButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            try {
                if (saveScreen.getSavePanel1() != null) {
                    save1.clear();
                    save2.clear();
                    save3.clear();
                    saveScreen.clear(saveScreen.getSavePanel1(), saveScreen.getSavePanel2(), saveScreen.getSavePanel3(), saveScreen.getBackToCampaignMenu());
                }
                saveScreen.build(gameScreen, campaignLevelScreen);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            saveScreen.display();
            return UIEventResponses.preventDefault();
        });
        newGameButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = campaignStats[0];
            currentLevel = 1;
            totalPoints = 0;
            gameScreen.addComponent(GameScreen.getBackButton());
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + currentLevel, Positions.create(73, 1)).invoke();
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
        backToMenuButton2.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getCampaignLevelScreen() {
        return campaignLevelScreen;
    }

    public void display() {
        campaignLevelScreen.display();
    }
}
