import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.virtual.DefaultVirtualTerminal;
import org.w3c.dom.Text;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;


public class Game {
    public TextGraphics tg;
    public Screen screen;
    public Terminal terminal;

    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics tg = screen.newTextGraphics();
        screen.startScreen();
        String words[] = {"Love", "Hate", "Truth", "Happy", "Pressure", "Burp", "Vampire"
                , "Surf", "Believe", "Slime", "Dream", "Religion", "Rhythm", "Disco", "Honey",
                "Star", "Armies", "Zombie", "Heart", "Break", "Docks", "Multiply", "Mace", "Moustache"
                , "Wizards", "Sports", "Blind", "Riddle", "Business", "School", "Blood", "Promenade",
                "Vault", "Spray", "Eternal", "Dress", "Abstain", "Controls", "Circuit", "Forever",
                "Dangerous", "Skunk", "House", "Wives", "Flashlight", "Console", "Awesome", "Scared",
                "Hormones", "Promise", "Angel", "Baggage", "Duck", "Destroy", "Tissue", "Ketchup",
                "Picture", "Basket", "Basketball", "Fan", "Dough", "Thief", "Female", "Family", "Normal",
                "Wireless", "Empty", "Cycles", "Banana", "Eggplant", "Samba", "Jumble", "Flush", "Beach",
                "Driver", "Queen", "Mommy", "Fade", "Kitten", "Spring", "Interest", "Debts", "Horse",
                "Tomorrow", "Discount", "Faithful", "Midnight", "Epic", "Calendar", "Roses", "Funeral",
                "Badminton", "Spirit", "Water", "Pizza", "Science", "Cabinet", "Apple", "Television",
                "Profile", "Wine", "Sedan", "Luxury", "Firewall", "Computer", "Tablet", "Giraffe", "Ring",
                "Shut", "Vision", "World", "War", "High", "School", "Fantasy", "Warfare", "Incognito",
                "Loading", "Penguins", "Fright", "Night", "Diary", "Mega", "Submit", "Hold", "Statue",
                "Boat", "Mobile", "Invisible", "Visible", "Ground", "Space", "Dragon", "Spade", "Clover",
                "Senior", "Junior", "Sophomore", "Freshman", "Twilight", "Dawn", "Eclipse", "Moon",
                "Hair", "Spray", "Camp", "Jazz", "Rock", "Eggs", "Hustle"};
        String word = new WordSpawner(words).getWord().toLowerCase();

        boolean keeprunning = true;
        StringBuilder sb = new StringBuilder();
        while (keeprunning) {
            KeyStroke keyPressed = terminal.pollInput(); // pollinput is non-blocking
            if (keyPressed != null) {
                System.out.println(keyPressed);
                switch (keyPressed.getKeyType()) {
                    case Escape:
                        keeprunning = false;
                        break;
                    case Character:
                        if (keyPressed.getCharacter().equals(' ')) {
                            if (sb.toString().equals(word)){
                                sb = new StringBuilder();
                                screen.refresh();
                            }
                            tg.putString(0, 23, " ".repeat(80));
                            screen.refresh();
                        }
                        else {
                            sb.append(keyPressed.getCharacter());
                            tg.putString(30, 23, sb.toString());
                            screen.refresh();
                            break;
                        }
                    case Enter:
                        tg.putString(0, 23, " ".repeat(80));
                        screen.refresh();
                        sb = new StringBuilder();
                        break;
                    case Backspace:
                        sb.setLength(Math.max(sb.length() - 1, 0));
                        tg.putString(30 + sb.length(), 23, " ");
                        screen.refresh();
                        break;
                    case ArrowUp:
                        int i = 0;
                        try {
                            word = new WordSpawner(words).getWord().toLowerCase();
                            new ThreadWords(i,word,terminal,screen,tg).start();
                        } catch(Exception err){
                            err.printStackTrace();
                        }

                        break;
                    default:
                        System.out.println("default-branch");
                }
            }
        }

        screen.refresh();
        screen.stopScreen();
    }
}
