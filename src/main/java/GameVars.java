import org.hexworks.zircon.api.graphics.Layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameVars {
    static String rolledWord;
    static int loc;
    static Layer wordLayer;
    static HashMap<String, Layer> wordsOnScreen = new HashMap<>();
    static int livesLeft = 3;
    static int points;
    static int totalPoints;
    static int saveClicked;
    static int[] stats = new int[3];
    static final int[] easyStats = {2000, 2000, 15};
    static final int[] medStats = {1300, 1300, 30};
    static final int[] hardStats = {1000, 1000, 60};
    static final int[] extremeStats = {750, 750, 60};
    static final int[] insaneStats = {500, 500, 60};
    static int currentLevel;
    private static final int[] level1Stats = {2000, 2000, 5};
    private static final int[] level2Stats = {2000, 2000, 10};
    private static final int[] level3Stats = {1500, 1500, 15};
    private static final int[] level4Stats = {1500, 1500, 20};
    private static final int[] level5Stats = {1500, 1500, 25};
    private static final int[] level6Stats = {1000, 1000, 30};
    private static final int[] level7Stats = {1000, 1000, 35};
    private static final int[] level8Stats = {1000, 1000, 40};
    private static final int[] level9Stats = {750, 750, 55};
    private static final int[] level10Stats = {2500, 2500, 10};
    static final int[][] campaignStats = {level1Stats, level2Stats, level3Stats, level4Stats, level5Stats, level6Stats, level7Stats, level8Stats, level9Stats, level10Stats};
    static List<Layer> tekstid = new ArrayList<>(); //
    static boolean stopLaunchGame = false;
    static boolean gameRunning = true;
    static String winOrLose;
    static List<Integer> easyTops = new ArrayList<>();
    static List<Integer> mediumTops = new ArrayList<>();
    static List<Integer> hardTops = new ArrayList<>();
    static List<Integer> extremeTops = new ArrayList<>();
    static List<Integer> insaneTops = new ArrayList<>();
    static List<String> save1 = new ArrayList<>();
    static List<String> save2 = new ArrayList<>();
    static List<String> save3 = new ArrayList<>();
    static boolean boosterStatus = false;
    static boolean nukeStatus = false;
    static boolean slowStatus = false;
    static boolean boosterActive = false;
    static String gameStyle = "";
}
