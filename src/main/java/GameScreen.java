import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

//Üldine mängulaua ekraan
public class GameScreen extends GameVars {
    private final TileGrid tileGrid;
    private final Screen gameScreen;
    private static final Button backButton = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();

    public GameScreen(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.gameScreen = Screens.createScreenFor(tileGrid);
    }

    public static Button getBackButton() {
        return backButton;
    }

    //Ekraani ehitamine (nupp)
    public void build(Screen menuScreen) {
        backButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> { // BACK nuppu vajutades kustutab teksti layerid ja taastab elud ning algse punktiseisu.
            for (Layer b : tekstid) {
                tileGrid.removeLayer(b);
            }
            Game.clearLastGame();
            gameScreen.removeComponent(backButton);
            menuScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getGameScreen() {
        return gameScreen;
    }

    public void display() {
        gameScreen.display();
    }
}
