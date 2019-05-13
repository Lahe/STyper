import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

public class ArcadeLevels extends GameVars {
    private final TileGrid tileGrid;
    private final Screen levelScreen;


    public ArcadeLevels(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.levelScreen = Screens.createScreenFor(tileGrid);
    }

    public static void activateArcadeButtons(Button button, int[] arcadeStats, String difficulty, Screen gameScreen) {
        button.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            gameRunning = true;
            stopLaunchGame = false;
            stats = arcadeStats;
            gameScreen.addComponent(GameScreen.getBackButton());
            gameScreen.write("Lives: " + "♥".repeat(livesLeft), Positions.create(15, 42)).invoke();
            gameScreen.write("Points: " + points, Positions.create(60, 42)).invoke();
            gameScreen.write("Level: " + difficulty, Positions.create(70, 1)).invoke();
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

    public void build(Screen gameScreen, Screen menuScreen) {
        final Button easyButton = Components.button().withText("Easy").withPosition(Positions.create(38, 19)).build();
        final Button medButton = Components.button().withText("Medium").withPosition(Positions.create(38, 21)).build();
        final Button hardButton = Components.button().withText("Hard").withPosition(Positions.create(38, 23)).build();
        final Button extremeButton = Components.button().withText("Extreme").withPosition(Positions.create(38, 25)).build();
        final Button insaneButton = Components.button().withText("Insane").withPosition(Positions.create(38, 27)).build();
        final Button backToMenuButton = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
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
        activateArcadeButtons(easyButton, easyStats, "Easy", gameScreen);
        activateArcadeButtons(medButton, medStats, "Medium", gameScreen);
        activateArcadeButtons(hardButton, hardStats, "Hard", gameScreen);
        activateArcadeButtons(extremeButton, extremeStats, "Extreme", gameScreen);
        activateArcadeButtons(insaneButton, insaneStats, "Insane", gameScreen);
        backToMenuButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getLevelScreen() {
        return levelScreen;
    }

    public void display() {
        levelScreen.display();
    }
}
