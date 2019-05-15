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

//Poe ekraan
public class Shop extends GameVars {
    private final Screen shopScreen;
    private final TileGrid tileGrid;

    public Shop(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.shopScreen = Screens.createScreenFor(tileGrid);
    }

    // Poe paneelide loomine
    public static Panel buildShopPanels(int x, int y, String title) {
        return Components.panel()
                .withPosition(Positions.create(x, y))
                .withSize(27, 8)
                .withTitle(title)
                .wrapWithBox(true)
                .build();
    }

    // Tekstikastide loomine
    public static TextBox buildShopTextBoxes(String p1, int cost) {
        return Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(25)
                .addParagraph(p1)
                .addParagraph("Cost: " + cost)
                .build();
    }

    // Poes tehtud valikutele reageerimine
    public static void activateShopButtons(Button buyItem, String item, boolean itemStatus, Screen shopScreen) {
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

    public Screen getShopScreen() {
        return shopScreen;
    }

    // Poe ekraani ehitamine (nupud, tekst)
    public void build(Screen shopScreen, Screen levelCompleteScreen) {
        Panel boosterPanel = buildShopPanels(29, 5, "Booster");
        TextBox boosterBox = buildShopTextBoxes("Boosts credit gain by 2x", 1000);
        Panel nukePanel = buildShopPanels(29, 13, "Nuke");
        TextBox nukeBox = buildShopTextBoxes("Everything die", 2500);
        Panel slowPanel = buildShopPanels(29, 21, "Slow-mo");
        TextBox slowBox = buildShopTextBoxes("Slows down words by 2x", 1000);
        Panel livesPanel = buildShopPanels(29, 29, "Lives");
        TextBox livesBox = buildShopTextBoxes("Gain 1 extra life", 5000);

        Button backToLevelCompleteScreen = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        Button buyBooster = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buyNuke = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buySlow = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();
        Button buyLives = Components.button().withText("BUY").withPosition(Positions.create(10, 5)).build();

        activateShopButtons(buyBooster, "Booster", boosterStatus, shopScreen);
        activateShopButtons(buyNuke, "Nuke", nukeStatus, shopScreen);
        activateShopButtons(buySlow, "Slow-mo", slowStatus, shopScreen);

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
        backToLevelCompleteScreen.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            levelCompleteScreen.display();
            return UIEventResponses.preventDefault();
        });
    }

    public void display() {
        shopScreen.display();
    }
}
