import java.util.Random;

public class WordSpawner {
    String[] words;
    Random a;
    int roll;
    String word;
    private String text;

    public String getWord() {
        return word;
    }


    public void matchWord(String typedText){
        if(typedText.equals(this.word)){

        }
    }

    public WordSpawner(String[] words) {
        this.words = words;
        this.a = new Random();
        this.roll = a.nextInt(149);
        this.word = words[roll];
    }
    public WordSpawner(String text){
        this.text = text;
    }
}
