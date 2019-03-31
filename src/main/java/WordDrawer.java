import org.hexworks.zircon.api.CharacterTileStrings;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.builder.graphics.LayerBuilder;
import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.graphics.CharacterTileString;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;


public class WordDrawer { // See klass tegeleb ekraanile sõnade kuvamisega.
    private TileGrid tileGrid;
    private Layer wordLayer;

    public Layer getWordLayer() {
        return wordLayer;
    }

    public WordDrawer(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
    }

    public void drawWords(String rolledWord) {
        wordLayer = new LayerBuilder().withOffset(Positions.create(Game.getLoc(), 0))
                .withSize(Sizes.create(rolledWord.length(), 1))
                .build();
        CharacterTileString tcs = CharacterTileStrings.newBuilder()
                .withForegroundColor(ANSITileColor.WHITE)
                .withText(rolledWord)
                .build();
        wordLayer.draw(tcs, Positions.zero());
        tileGrid.pushLayer(wordLayer);

    }
}
