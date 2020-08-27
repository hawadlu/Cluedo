import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Overall class to run game
 * -Initializes players, card and board
 * -Loops through players to run game
 * -Checks if game is finished
 */
public class Game {
    public static Suspects murderer;
    public static Rooms murderRoom;
    public static Weapons murderWeapon;
    public static boolean gameOver;
    public static List<Player> players;
    public static Map<Suspects, Player> playerMap;
    public static List<Weapon> weapons;
    public static Map<Weapons, Weapon> weaponMap;
    public static Board board;
    public static GUI gui;
    public static Player currentPlayer;
    public static HashMap<String, String> extraInfo = new HashMap<>();

    private final static Die die1 = new Die(), die2 = new Die();
    private static ArrayList<Player> playingPlayers;

    public enum Suspects {
        SCARLET,
        PLUM,
        WHITE,
        PEACOCK,
        GREEN,
        MUSTARD
    }

    public enum Rooms{
        KITCHEN,
        BALLROOM,
        STUDY,
        BILLIARD_ROOM,
        CONSERVATORY,
        DINING_ROOM,
        HALL,
        LIBRARY,
        LOUNGE
    }

    public enum Weapons{
        CANDLESTICK,
        DAGGER,
        LEAD_PIPE,
        REVOLVER,
        ROPE,
        SPANNER
    }

    /**
     * Create the Weapons, starting them in random rooms
     */
    private static void createWeapons() {
        weapons = new ArrayList<>();
        weaponMap = new HashMap<>();

        for (Weapons weapon : Weapons.values()) {

            Rooms room = Rooms.values()[(int) (Math.random() * 8)];

            try {
                Weapon newWeapon = new Weapon(weapon, room);
                weapons.add(newWeapon);
                weaponMap.put(weapon, newWeapon);
            } catch (InvalidFileException e) {
                System.out.println("No image available for "+weapon);
            }
        }
    }

    /**
     * Create the players, setting them all as NPC's to start with
     */
    private static void createPlayers() {
        players = new ArrayList<>();
        playerMap = new HashMap<>();

        for (Suspects suspect : Suspects.values()) {
            Position startPos = getStartingPosition(suspect);
            try {
                Player newPlayer = new Player(suspect, startPos);
                newPlayer.setHasLost(true);
                players.add(newPlayer);
                playerMap.put(suspect, newPlayer);
            } catch (InvalidFileException e) {
                System.out.println("No image available for "+suspect);
            }
        }
    }

    /**
     * Gets the default starting position of a suspect
     *
     * @param suspect the suspect enum to find the starting position of
     * @return a position object, containing the starting position coordinates
     */
    private static Position getStartingPosition(Suspects suspect){
        switch (suspect) {
            case WHITE: return new Position(9, 0);
            case GREEN: return new Position(14, 0);
            case PEACOCK: return new Position(23, 6);
            case PLUM: return new Position(23, 19);
            case SCARLET: return new Position(7, 24);
            case MUSTARD: return new Position(0, 17);
            default: return null;
        }
    }

    /**
     * Create a dropdown to get the player to choose one of the provided options
     * @param options the options the player can choose from
     * @param title the title on the popup box
     * @param message the message above the dropdown
     * @param <T> the type of object of the options
     * @return the option that was chosen by the player
     */
    @SuppressWarnings("unchecked")
    public static <T> T makeDropDown(T[] options, String title, String message) {
        T chosen = null;
        while (chosen == null)
            chosen = (T) JOptionPane.showInputDialog(gui.window, message, title,
                    JOptionPane.PLAIN_MESSAGE, null, options, 2);
        return chosen;
    }

    /**
     * Setup the game to be ready to be played
     * - Sets game over to false
     * - Creates board
     * - Creates players
     * - Deals cards to players
     */
    public static void setupGame() throws IOException {
        gameOver = false;
        createPlayers();
        createWeapons();
        currentPlayer = null;

        // Set Weapons to their starting positions
        for (Weapon weapon : weapons)
            board.getRoom(weapon.getRoom()).addWeapon(weapon);
        gui.redraw();

        // Set players to their starting positions
        for (Player player : players)
            board.getTile(player.getPos()).addPlayer(player);
        gui.redraw();

        // Get number of players
        Integer numPlayers = makeDropDown(new Integer[]{2, 3, 4, 5, 6}, "Number of Players",
                                            "How many people are playing?");

        // Setup the playable players
        playingPlayers = new ArrayList<>();
        ArrayList<Suspects> availablePlayers = new ArrayList<>(Arrays.asList(Suspects.values()));
        ArrayList<String> takenNames = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            // Setup the character creation panel
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

            // Setup the radio buttons for character selection
            ButtonGroup radioButtons = new ButtonGroup();
            JLabel textTitle = new JLabel();
            textTitle.setText("Player "+i+": Who do you want to play as?");
            panel.add(textTitle);
            for (Suspects suspect : availablePlayers) {
                JRadioButton radio = new JRadioButton((""+suspect));
                radio.addActionListener(e -> Game.currentPlayer = playerMap.get(suspect));
                radioButtons.add(radio);
                panel.add(radio);
            }

            // Setup name input box
            JTextPane textMessage = new JTextPane();
            textMessage.setEditable(false);
            panel.add(textMessage);
            textMessage.setBackground(new Color(0,0,0,0));

            // Get the users response
            String name = "";
            while (currentPlayer == null ||
                    name.length() == 0 || name.length() > 10 ||
                    takenNames.contains(name.toLowerCase())) {

                textMessage.setText("Enter your name:" +
                                    (name.length() > 10 ? "\n(Max 10 characters)" :
                                    (takenNames.contains(name.toLowerCase()) ?
                                    "\n(That name has already been taken)" : "")));

                name = JOptionPane.showInputDialog(gui.window, panel,
                            "Player " + i + " Creation", JOptionPane.PLAIN_MESSAGE);

                name = name==null ? "" : name.trim();
            }

            // Setup the player accordingly
            currentPlayer.setHasLost(false);
            availablePlayers.remove(currentPlayer.getSuspect());
            playingPlayers.add(currentPlayer);
            takenNames.add(name.toLowerCase());
            currentPlayer.setName(name);

            Game.print(currentPlayer.getName()+" ("+currentPlayer+") created!");
            currentPlayer = null;
        }

        //Parse the extra info for the cards
        parseExtraCardInfo();

        dealCards(playingPlayers);
    }

    /**
     * Parse the extra information about each card
     * @return
     */
    private static void parseExtraCardInfo() throws IOException {
        File infoFile = new File("Assets/Cards/Info.txt");
        FileReader fileReader = new FileReader(infoFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String line;
        String title = null;
        String state = "read info";

        while((line = bufferedReader.readLine()) != null) {
            if (state.equals("read info")) {
                if (line.equals("NEW ITEM")) state = "read title";
                else {
                    extraInfo.put(title, line);
                }
            } else if (state.equals("read title")) {
                title = line;
                state = "read info";
            }
        }
    }

    /**
     * Play the game
     */
    public static void playGame() {
        int playerIndex = 0;
        while (!gameOver) {
            currentPlayer = playingPlayers.get(playerIndex);
            currentPlayer.hideHand();
            JOptionPane.showMessageDialog(gui.window, currentPlayer+"'s turn");
            currentPlayer.showHand();

            try { currentPlayer.takeTurn(board);
            } catch (InvalidFileException e) { e.printStackTrace(); }

            if (currentPlayer.hasLost()) playingPlayers.remove(playerIndex);
            else playerIndex++;

            if(playingPlayers.size() == 0) {
                System.out.println("All players have lost, game is over.");
                gameOver = true;
            } else playerIndex = playerIndex % playingPlayers.size();
        }
    }

    /**
     * Creates cards, chooses a person, weapon and room at random to be the murder items
     * shuffles the remaining cards and deals them out to the players

     * @param players the list of players playing the game to be dealt cards to
     */
    private static void dealCards(List<Player> players) {
        int numPlayers = players.size();

        //Create ArrayLists of each card type
        ArrayList<Card<Suspects>> playerCards = new ArrayList<>();
        ArrayList<Card<Weapons>> weaponCards = new ArrayList<>();
        ArrayList<Card<Rooms>> roomCards = new ArrayList<>();

        try {
            for (Suspects s : Suspects.values()) playerCards.add(new Card<>(s, extraInfo));
            for (Weapons w : Weapons.values()) weaponCards.add(new Card<>(w, extraInfo));
            for (Rooms r : Rooms.values()) roomCards.add(new Card<>(r, extraInfo));
        } catch(InvalidFileException e) { System.out.println(e.toString()); }

        //Shuffling
        Collections.shuffle(playerCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(roomCards);

        //Add to accuse
        murderer = playerCards.get(0).getEnum();     playerCards.remove(0);
        murderRoom = roomCards.get(0).getEnum();       roomCards.remove(0);
        murderWeapon = weaponCards.get(0).getEnum();     weaponCards.remove(0);

        //Add rest to big list
        ArrayList<Card<?>> remainingCards = new ArrayList<>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(roomCards);
        remainingCards.addAll(weaponCards);

        //Shuffle all the cards
        Collections.shuffle(remainingCards);

        //Divide Among players
        int currentPlayer = 0;
        for(Card<?> c : remainingCards){
            players.get(currentPlayer).addToHand(c);
            currentPlayer ++;
            currentPlayer %= numPlayers;
        }
    }

    /**
     * Roll the dice, get the result
     * @return the total number rolled on the dice
     */
    public static int rollDice() {
        return die1.roll() + die2.roll();
    }

    /**
     * Get the dice
     * @return an array of the 2 die
     */
    public static Die[] getDice() {
        return new Die[]{die1, die2};
    }

    /**
     * Print a message to the GUI console
     * @param text the message to print
     */
    public static void print(String text) {
        gui.addToConsole(text);
        gui.consolePanel.redraw();
    }

    /**
     * Get a list of players that are currently playing the game and haven't lost
     * @return unmodifiable Collection of active players
     */
    public static Collection<Player> getActivePlayers() {
        return Collections.unmodifiableCollection(playingPlayers);
    }

    public static void main(String[] args) throws IOException, InvalidFileException {
        board = new Board();
        gui = new GUI();
        setupGame();
        playGame();
    }
}
