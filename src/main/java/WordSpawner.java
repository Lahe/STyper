import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

// See klass tegeleb failist sõnade lugemisega ja nende sõnade seast suvalise valimisega.
public class WordSpawner {
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
    //private static ArrayList<String> paragraphs;
    private Random wordRoller = new Random();
    private int rollWord = wordRoller.nextInt(9895);
    //private int rollPara = wordRoller.nextInt(770);
    private Random rand = new Random();

    static {
        try {
            words = readWords("simple2.txt");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int getLoc() {
        return rand.nextInt(65) + 5;
    }

    public String rollNext() throws Exception {
        rollWord = wordRoller.nextInt(9895);
        return words.get(rollWord).toLowerCase();
    }

    //Paragraphs - unfinished
    /*
    public String rollNextPara(int i) {
        rollPara = wordRoller.nextInt(770);
        return paragraphs.get(rollPara).toLowerCase().trim().split(" ")[i];
    }
    public static ArrayList<String> readParagraphs(String filename) throws Exception {
        ArrayList<String> paragraphs = new ArrayList<>();
        File file = new File(filename);
        try (Scanner sc = new Scanner(file, "UTF-8")) {
            while (sc.hasNextLine()) {
                String rida = sc.nextLine();
                paragraphs.add(rida.split("\\t")[1]);
            }
        }
        return paragraphs;
    }
    static {
        try {
            paragraphs = readParagraphs("paragraphs.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}
