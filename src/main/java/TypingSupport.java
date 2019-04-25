import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEventType;

public class TypingSupport { // See klass tegeleb kasutaja poolt sisestatud tekstiga. Kontrollib, kas kirjutatud sõna klapib ekraanil olevaga.
    private static final int TERMINAL_WIDTH = 85;
    private static final int TERMINAL_HEIGHT = 45;
    private static final Size SIZE = Sizes.create(TERMINAL_WIDTH, TERMINAL_HEIGHT);
    private static final int TYPER_WIDTH = 35;
    private static final int TYPER_HEIGHT = 43;
    private static StringBuilder sb = new StringBuilder();
    private static int points = Game.getPoints();
    private static boolean startOrStopSupport = true;

    public static void setStartOrStopSupport(boolean startOrStopSupport) {
        TypingSupport.startOrStopSupport = startOrStopSupport;
    }

    public static void setPoints(int points) {
        TypingSupport.points = points;
    }

    public static Size getSIZE() {
        return SIZE;
    }

    public void startTypingSupport(TileGrid tileGrid) {
        if (startOrStopSupport) {
            System.out.println("SIIN");
            tileGrid.onKeyboardEvent(KeyboardEventType.KEY_PRESSED, (event, phase) -> {
                final Position pos = tileGrid.cursorPosition();
                tileGrid.putCursorAt(pos.withY(TYPER_HEIGHT).withX(TYPER_WIDTH));

                if (event.getCode().equals(KeyCode.ESCAPE)) { // ESCAPE vajutades mäng sulgub.
                    System.exit(0);
                } else if (event.getCode().equals(KeyCode.BACKSPACE)) { // Võimaldab mängijal kirjutatud teksti kustutada.
                    tileGrid.putCursorAt(pos.withY(TYPER_HEIGHT).withX(TYPER_WIDTH + sb.length() - 1));
                    tileGrid.putCharacter(' ');
                    sb.setLength(Math.max(sb.length() - 1, 0));
                    //System.out.println(sb.toString());

                } else if (event.getCode().equals(KeyCode.ENTER) || event.getCode().equals(KeyCode.SPACE)) { // Kas kasutaja vajutab sõna kinnitamiseks SPACE või ENTER.
                    if (Game.getWordsOnScreen().containsKey(sb.toString())) { // Kui sõna klapib, siis suurendatakse punktisummat ja eemaldatakse sõna ekraanilt
                        points += sb.length() * 10;
                        Game.setPoints(points);
                        System.out.println(Game.getPoints());
                        if (Game.getGameStyle().equals("ARCADE"))
                        Game.getGameScreen().write("Points: " + points, Positions.create(60, 42)).invoke();
                        else Game.getGameScreen().write("Credits: " + points, Positions.create(60, 42)).invoke();
                        tileGrid.removeLayer(Game.getWordsOnScreen().get(sb.toString()));
                        Game.getWordsOnScreen().remove(sb.toString());
                    }
                    for (int i = 0; i < TERMINAL_WIDTH; i++) {  // Tehakse kirjutamise lahter tühjaks.
                        tileGrid.putCharacter(' ');
                    }
                    sb.setLength(0);
                } else { // Teiste klaviatuuri nuppude vajutamisel lisatakse vajutatud trükimärk StringBuilderisse.
                    sb.append(event.getKey());
                    //System.out.println(sb.toString());
                    for (int i = 0; i < sb.length(); i++) {
                        tileGrid.putCharacter(sb.toString().charAt(i));
                    }
                }
                if (startOrStopSupport == false) {
                    return null;
                }
                return UIEventResponses.processed();
            });
        }
    }
}
