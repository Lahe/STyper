import java.util.Random;

public class WordSpawner {
    private String[] words = {"Love", "Hate", "Truth", "Happy", "Pressure", "Burp", "Vampire"
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
    private Random wordRoller = new Random();
    private int rollWord = wordRoller.nextInt(149);
    private Random rand = new Random();

    public int getLoc() {
        return rand.nextInt(70) + 5;
    }

    public String rollNext() {
        rollWord = wordRoller.nextInt(149);
        return  words[rollWord].toLowerCase();
    }
}
