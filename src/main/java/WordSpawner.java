import java.util.Random;

public class WordSpawner {
    String[] words;
    Random a;
    int roll;
    String word;

    public String getWord() {
        return word;
    }

    public WordSpawner(String[] words) {
        this.words = words;
        this.a = new Random();
        this.roll = a.nextInt(149);
        this.word = words[roll];
    }
}
