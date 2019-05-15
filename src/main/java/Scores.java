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

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

//Skooride kuvamise ekraan
public class Scores extends GameVars {
    private final TileGrid tileGrid;
    private final Screen scoreScreen;

    public Scores(TileGrid tileGrid) {
        this.tileGrid = tileGrid;
        this.scoreScreen = Screens.createScreenFor(tileGrid);
    }

    // Skoori tekstikastide loomine
    public static TextBox buildScoreTextBoxes(List<Integer> tops) {
        return Components.textBox()
                .withPosition(Positions.create(0, 1))
                .withContentWidth(13)
                .addParagraph("RANK | SCORE")
                .addParagraph("1ST    " + getScore(tops, 0))
                .addParagraph("2ND    " + getScore(tops, 1))
                .addParagraph("3RD    " + getScore(tops, 2))
                .addParagraph("4TH    " + getScore(tops, 3))
                .addParagraph("5TH    " + getScore(tops, 4))
                .build();
    }

    // Skoori paneelide loomine
    public static Panel buildScorePanels(int x, int y, String title) {
        return Components.panel()
                .withPosition(Positions.create(x, y))
                .withSize(15, 15)
                .withTitle(title)
                .wrapWithBox(true)
                .build();
    }

    // Teksti paigutamine paneeli
    public static Panel addContentToPanels(int x, int y, String title, List<Integer> tops) {
        Panel panel = buildScorePanels(x, y, title);
        TextBox box = buildScoreTextBoxes(tops);
        panel.addComponent(box);
        return panel;
    }

    // Score'ide ekraani ehitamine (tekst, nupud, paneelid)
    public void build(Screen menuScreen) {
        final Button backToMenuButtonScores = Components.button().withText("BACK").withPosition(Positions.offset1x1()).build();
        scoresFromFile();
        Panel easyPanel = addContentToPanels(19, 8, "Easy", easyTops);
        Panel mediumPanel = addContentToPanels(36, 8, "Medium", mediumTops);
        Panel hardPanel = addContentToPanels(53, 8, "Hard", hardTops);
        Panel insanePanel = addContentToPanels(28, 24, "Insane", insaneTops);
        Panel extremePanel = addContentToPanels(45, 24, "Extreme", extremeTops);

        backToMenuButtonScores.onComponentEvent(ComponentEventType.ACTIVATED, (event) -> {
            menuScreen.display();
            scoreScreen.removeComponent(easyPanel);
            scoreScreen.removeComponent(mediumPanel);
            scoreScreen.removeComponent(hardPanel);
            scoreScreen.removeComponent(insanePanel);
            scoreScreen.removeComponent(extremePanel);
            scoreScreen.removeComponent(backToMenuButtonScores);
            easyTops.clear();
            mediumTops.clear();
            hardTops.clear();
            insaneTops.clear();
            extremeTops.clear();
            return UIEventResponses.preventDefault();
        });

        scoreScreen.addComponent(easyPanel);
        scoreScreen.addComponent(mediumPanel);
        scoreScreen.addComponent(hardPanel);
        scoreScreen.addComponent(insanePanel);
        scoreScreen.addComponent(extremePanel);
        scoreScreen.addComponent(backToMenuButtonScores);
    }

    // Sobiva skoori lisamine skooritabelisse
    public static void addScoreToTopList(List<Integer> tops, String[] line) {
        int score = Integer.parseInt(line[4]);
        if (tops.size() < 5) {
            tops.add(score);
        } else {
            if (Collections.min(tops) < score) {
                tops.set(tops.indexOf(Collections.min(tops)), score);
            }
        }
        Collections.sort(tops, Collections.reverseOrder());
    }

    // Skooride lugemine failist ja tabelisse lisamine
    public static void scoresFromFile() {
        String rida;
        try (BufferedReader in = new BufferedReader((new FileReader("Leaderboards.txt", Charset.forName("UTF-8"))))) {
            while ((rida = in.readLine()) != null) {
                String[] line = rida.split(" ");
                switch (line[3]) {
                    case "Easy":
                        addScoreToTopList(easyTops, line);
                        break;
                    case "Medium":
                        addScoreToTopList(mediumTops, line);
                        break;
                    case "Hard":
                        addScoreToTopList(hardTops, line);
                        break;
                    case "Insane":
                        addScoreToTopList(insaneTops, line);
                        break;
                    case "Extreme":
                        addScoreToTopList(extremeTops, line);
                        break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Sobiva skoori kätte saamine listist. Kui puudub, siis "-".
    public static String getScore(List<Integer> list, int index) {
        if (list.size() > index) {
            return Integer.toString(list.get(index));
        }
        return "-";
    }

    public void display() {
        scoreScreen.display();
    }
}
