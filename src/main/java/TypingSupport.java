import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.color.ANSITileColor;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.graphics.Layer;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEventType;

public class TypingSupport extends Game {
    private static final int TERMINAL_WIDTH = 85;
    private static final int TERMINAL_HEIGHT = 45;
    private static final Size SIZE = Sizes.create(TERMINAL_WIDTH, TERMINAL_HEIGHT);
    private static final int TYPER_WIDTH = 35;
    private static final int TYPER_HEIGHT = 43;
    private static StringBuilder sb = new StringBuilder();

    public static StringBuilder getSb() {
        return sb;
    }

    public static Size getSIZE() {
        return SIZE;
    }

    public void startTypingSupport(TileGrid tileGrid) {
        tileGrid.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> {
            final Position pos = tileGrid.cursorPosition();
            tileGrid.putCursorAt(pos.withY(TYPER_HEIGHT).withX(TYPER_WIDTH));

            if (event.getCode().equals(KeyCode.ESCAPE)) {
                System.exit(0);
            }
            else if (event.getCode().equals(KeyCode.BACKSPACE)) {
                tileGrid.putCursorAt(pos.withY(TYPER_HEIGHT).withX(TYPER_WIDTH + sb.length() - 1));
                tileGrid.putCharacter(' ');
                sb.setLength(Math.max(sb.length() - 1, 0));
                System.out.println(sb.toString());

            } else if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.SPACE)) {
                //System.out.println(getRolledWord());
                if (getWordsOnScreen().containsKey(sb.toString())) {
                    tileGrid.removeLayer(getWordsOnScreen().get(sb.toString()));
                    getWordsOnScreen().remove(sb.toString());
                    //System.out.println("xd");
                }
                for (int i = 0; i < TERMINAL_WIDTH; i++) {
                    tileGrid.putCharacter(' ');
                }
                sb.setLength(0);
            } else {
                sb.append(event.getKey());
                System.out.println(sb.toString());
                for (int i = 0; i < sb.length(); i++) {
                    tileGrid.putCharacter(sb.toString().charAt(i));
                }
            }
            return UIEventResponses.processed();
        });
    }
}
