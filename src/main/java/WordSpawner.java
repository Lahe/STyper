import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class WordSpawner { // See klass tegeleb failist sõnade lugemisega ja nende sõnade seast suvalise valimisega.
    public static ArrayList<String> readWords(String filename) throws Exception { // Failist sõnade lugemine.
        File file = new File(filename);
        ArrayList<String> words = new ArrayList<>();
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNextLine()) {
                String rida = sc.nextLine();
                words.add(rida);
            }
        }
        return words;
    }

    private static ArrayList<String> words;
    private Random wordRoller = new Random();
    private int rollWord = wordRoller.nextInt(9895);
    private Random rand = new Random();

    static {
        try {
            words = readWords("src/main/simple2.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getLoc() {
        return rand.nextInt(65) + 5;
    }

    public String rollNext() throws Exception {
        rollWord = wordRoller.nextInt(9895);
        return words.get(rollWord).toLowerCase();
    }
}
