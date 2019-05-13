import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

public class GameOverScreen extends GameVars {
    private final TileGrid tileGrid;
    private final Screen gameOverScreen;


    public GameOverScreen(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.gameOverScreen = Screens.createScreenFor(tileGrid);
    }

    public void build(Screen gameScreen, Screen menuScreen) {
        final Button playAgainButton = Components.button().withText("PLAY AGAIN").withPosition(Positions.create(37, 23)).build();
        gameOverScreen.write("GAME OVER", Positions.create(38, 20)).invoke();
        gameOverScreen.addComponent(playAgainButton);
        playAgainButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            Game.clearLastGame();
            gameScreen.removeComponent(GameScreen.getBackButton());
            wordsOnScreen.clear();
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });

    }

    public Screen getGameOverScreen() {
        return gameOverScreen;
    }

    public void display() {
        gameOverScreen.display();
    }
}
