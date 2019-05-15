import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

public class GameFinishedScreen extends GameVars {
    private final TileGrid tileGrid;
    private final Screen gameFinishedScreen;


    public GameFinishedScreen(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.gameFinishedScreen = Screens.createScreenFor(tileGrid);
    }
    // Ehitab ekraani (tekst, nupud)
    public void build(Screen gameScreen, Screen menuScreen) {
        gameFinishedScreen.write("Game Finished. Well done!", Positions.create(31, 20)).invoke();
        Button playAgainButtonFinishedScreen = Components.button().withText("PLAY AGAIN").withPosition(Positions.create(37, 23)).build();
        gameFinishedScreen.addComponent(playAgainButtonFinishedScreen);
        playAgainButtonFinishedScreen.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            Game.clearLastGame();
            gameScreen.removeComponent(GameScreen.getBackButton());
            wordsOnScreen.clear();
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getGameFinishedScreen() {
        return gameFinishedScreen;
    }

    public void display() {
        gameFinishedScreen.display();
    }
}
