import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEventType;

import java.util.ArrayList;

public class TypingSupport extends GameVars { // See klass tegeleb kasutaja poolt sisestatud tekstiga. Kontrollib, kas kirjutatud sõna klapib ekraanil olevaga.
    private static final int TERMINAL_WIDTH = 85;
    private static final int TERMINAL_HEIGHT = 45;
    private static final Size SIZE = Sizes.create(TERMINAL_WIDTH, TERMINAL_HEIGHT);
    private static final int TYPER_WIDTH = 35;
    private static final int TYPER_HEIGHT = 43;
    private static StringBuilder sb = new StringBuilder();
    private static boolean startOrStopSupport = true;

    public static void setStartOrStopSupport(boolean startOrStopSupport) {
        TypingSupport.startOrStopSupport = startOrStopSupport;
    }

    public static Size getSIZE() {
        return SIZE;
    }

    public void startTypingSupport(TileGrid tileGrid) {
        if (startOrStopSupport) {
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
                    if (wordsOnScreen.containsKey(sb.toString())) { // Kui sõna klapib, siis suurendatakse punktisummat ja eemaldatakse sõna ekraanilt
                        if (boosterActive) {
                            points += sb.length() * 10 * 2;
                            totalPoints += sb.length() * 10 * 2;
                        } else {
                            points += sb.length() * 10;
                            totalPoints += sb.length() * 10;
                        }
                        if (gameStyle.equals("ARCADE")) {
                            Game.getMutableGameScreen().write("Points: " + points, Positions.create(60, 42)).invoke();
                        } else {
                            Game.getMutableGameScreen().write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
                        }
                        tileGrid.removeLayer(wordsOnScreen.get(sb.toString()));
                        wordsOnScreen.remove(sb.toString());
                    }
                    if (sb.toString().equals("launchbooster")) {
                        Game.getMutableGameScreen().write("✓".repeat(12), Positions.create(1, 42)).invoke();
                        if (boosterActive) {
                            boosterStatus = false;
                            boosterActive = true;
                        }
                    }
                    if (sb.toString().equals("launchnuke")) {
                        Game.getMutableGameScreen().write("✓".repeat(12), Positions.create(1, 41)).invoke();
                        if (nukeStatus) {
                            int nukePts = 0;
                            ArrayList<String> wordsToRemove = new ArrayList<>();
                            for (String word : wordsOnScreen.keySet()) {
                                nukePts += word.length() * 10;
                                tileGrid.removeLayer(wordsOnScreen.get(word));
                                wordsToRemove.add(word);
                            }
                            for (String s : wordsToRemove) {
                                wordsOnScreen.remove(s);
                            }
                            totalPoints += nukePts;
                            Game.getMutableGameScreen().write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
                            nukeStatus = false;
                        }
                    }
                    if (sb.toString().equals("launchslowmo")) {
                        Game.getMutableGameScreen().write("✓".repeat(12), Positions.create(1, 43)).invoke();
                        if (slowStatus) {
                            int curSpeed = stats[1];
                            stats[1] = curSpeed * 2;
                            stats[0] = curSpeed * 2;
                            slowStatus = false;
                        }
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
                if (!startOrStopSupport) {
                    return null;
                }
                return UIEventResponses.processed();
            });
        }
    }
}
