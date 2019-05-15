import org.hexworks.zircon.api.Components;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Screens;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.component.Button;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.screen.Screen;
import org.hexworks.zircon.api.uievent.ComponentEventType;

public class Menu extends GameVars {
    private final Screen menuScreen;
    private final TileGrid tileGrid;

    public Menu(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.menuScreen = Screens.createScreenFor(tileGrid);
    }

    //Ekraani ehitamine (tekst, nupud)
    public void build(Screen campaignLevelScreen, Screen levelScreen, Scores scoreScreen) {
        menuScreen.write(" S-Typer", Positions.create(38, 12)).invoke();
        final Button campaignButton = Components.button().withText("CAMPAIGN").withPosition(Positions.create(37, 17)).build();
        final Button scoreButton = Components.button().withText("SCORES").withPosition(Positions.create(38, 23)).build();
        final Button arcadeButton = Components.button().withText("ARCADE MODE").withPosition(Positions.create(36, 20)).build();
        menuScreen.addComponent(campaignButton);
        menuScreen.addComponent(arcadeButton);
        menuScreen.addComponent(scoreButton);
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
        scoreButton.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            scoreScreen.build(menuScreen);
            scoreScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public Screen getMenuScreen() {
        return menuScreen;
    }

    public void display() {
        menuScreen.display();
    }
}
