import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Random;

public class ThreadWords extends Thread {
    public Terminal terminal;
    public Screen screen;
    public TextGraphics tg;
    int id;
    String word;
    Random roll;
    int loc;

    public ThreadWords(int i,String word, Terminal terminal, Screen screen, TextGraphics tg) throws Exception {
        this.id = i;
        this.terminal = terminal;
        this.screen = screen;
        this.tg = tg;
        this.word = word;
        this.roll = new Random();
        this.loc = roll.nextInt(60)+5;

    }


    public void run() {
        try {
            for (int i = 0; i < 24; i++) {
                tg.putString(loc, i, word);
                tg.putString(loc, i - 1, " ".repeat(word.length()));
                screen.refresh();
                Thread.sleep(300);
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
