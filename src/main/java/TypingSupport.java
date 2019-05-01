import com.sun.management.GarbageCollectionNotificationInfo;
import org.hexworks.zircon.api.Positions;
import org.hexworks.zircon.api.Sizes;
import org.hexworks.zircon.api.UIEventResponses;
import org.hexworks.zircon.api.data.Position;
import org.hexworks.zircon.api.data.Size;
import org.hexworks.zircon.api.grid.TileGrid;
import org.hexworks.zircon.api.uievent.KeyCode;
import org.hexworks.zircon.api.uievent.KeyboardEventType;

import java.util.ArrayList;

public class TypingSupport { // See klass tegeleb kasutaja poolt sisestatud tekstiga. Kontrollib, kas kirjutatud sõna klapib ekraanil olevaga.
    private static final int TERMINAL_WIDTH = 85;
    private static final int TERMINAL_HEIGHT = 45;
    private static final Size SIZE = Sizes.create(TERMINAL_WIDTH, TERMINAL_HEIGHT);
    private static final int TYPER_WIDTH = 35;
    private static final int TYPER_HEIGHT = 43;
    private static StringBuilder sb = new StringBuilder();
    private static int points = Game.getPoints();
    private static int totalPoints = Game.getTotalPoints();
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
            //System.out.println("SIIN");
            int point_multiplier = 1;
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
                        if (Game.isBoosterActive()) {
                            points += sb.length() * 10 * 2;
                            totalPoints += sb.length() * 10 * 2;
                        }
                        else {
                            points += sb.length() * 10;
                            totalPoints += sb.length() * 10;
                        }
                        Game.setPoints(points);
                        Game.setTotalPoints(totalPoints);
                        if (Game.getGameStyle().equals("ARCADE")) {
                            Game.getGameScreen().write("Points: " + points, Positions.create(60, 42)).invoke();
                        } else {
                            Game.getGameScreen().write("Credits: " + totalPoints, Positions.create(60, 42)).invoke();
                        }
                        tileGrid.removeLayer(Game.getWordsOnScreen().get(sb.toString()));
                        Game.getWordsOnScreen().remove(sb.toString());
                    }
                    if (sb.toString().equals("launchbooster")) {
                        Game.getGameScreen().write("✓".repeat(12), Positions.create(1, 42)).invoke();
                        if (Game.isBoosterStatus()) {
                            Game.setBoosterStatus(false);
                            Game.setBoosterActive(true);
                        }
                    }
                    if (sb.toString().equals("launchnuke")) {
                        Game.getGameScreen().write("✓".repeat(12), Positions.create(1, 41)).invoke();
                        if (Game.isNukeStatus()) {
                            int nukePts = 0;
                            ArrayList<String> wordsToRemove = new ArrayList<>();
                            for (String word : Game.getWordsOnScreen().keySet()) {
                                nukePts += word.length() * 10;
                                tileGrid.removeLayer(Game.getWordsOnScreen().get(word));
                                wordsToRemove.add(word);
                            }
                            for (String s: wordsToRemove) {
                                Game.getWordsOnScreen().remove(s);
                            }
                            System.out.println(nukePts);
                            totalPoints += nukePts;
                            Game.setNukeStatus(false);
                        }
                    }
                    if (sb.toString().equals("launchslowmo")) {
                        Game.getGameScreen().write("✓".repeat(12), Positions.create(1, 43)).invoke();
                        if (Game.isSlowStatus()) {
                            int curSpeed = Game.getStats()[1];
                            Game.getStats()[1] = curSpeed * 2;
                            Game.getStats()[0] = curSpeed * 2;
                            Game.setSlowStatus(false);
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
                if (startOrStopSupport == false) {
                    return null;
                }
                return UIEventResponses.processed();
            });
        }
    }
}
