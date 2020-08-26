import javax.swing.*;
import java.io.*;
import java.util.*;

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
    public static Scanner input = new Scanner(System.in);
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
     * Shows the initial instructions.
     * Ask if the user just wants to play or view the instructions
     */
    public void showMenu() {
        System.out.println("Note: The board was designed using the 'Consolas' font and may not display properly in other fonts.");
        System.out.println();

        String response = chooseFromArray(new String[]{"Instructions", "Play"}, "Welcome to Cluedo!");

        if (response.equals("Instructions"))
            try {
                showInstructions();
            } catch (InvalidFileException e) {
                System.out.println("Instructions not found");
                showMenu();
            }
        else if (response.equals("Play")) playGame();
        else {
            System.out.println("Invalid response, try again!");
            showMenu();
        }
    }

    /**
     * Show the instructions
     */
    public void showInstructions() throws InvalidFileException {
        File instructions = new File("Assets/Instructions.txt");
        try {
            Scanner scanner = new Scanner(instructions);
            while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
        }catch(FileNotFoundException e){ throw new InvalidFileException("Assets/Instructions.txt"); }
        System.out.println();
        showMenu();
    }

    /**
     * Create the Weapoms, starting them in random rooms
     */
    public void createWeapons() {
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
    public void createPlayers() {
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
    public Position getStartingPosition(Suspects suspect){
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
    public void setupGame() throws IOException {
        gameOver = false;
        createPlayers();
        createWeapons();

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
            // Choose character
            Suspects[] options = availablePlayers.toArray(new Suspects[]{});
            Suspects chosen = makeDropDown(options, "Player "+i+" Creation",
                                            "Player "+i+" choose your character:");
            Player chosenPlayer = playerMap.get(chosen);
            chosenPlayer.setHasLost(false);
            availablePlayers.remove(chosen);
            playingPlayers.add(chosenPlayer);

            // Get name
            String name = "";
            while (name.length() == 0 || name.length() > 10 || takenNames.contains(name.toLowerCase())) {
                name = JOptionPane.showInputDialog(gui.window,
                        "Player " + i + " enter your name:" +
                                (name.length() > 10 ? "(Max 10 characters)" : (
                                takenNames.contains(name.toLowerCase()) ? "\n(That name has already been taken)" : "")),
                        "Player " + i + " Creation",
                        JOptionPane.PLAIN_MESSAGE);
                name = name==null ? "" : name.trim();
            }
            takenNames.add(name.toLowerCase());
            chosenPlayer.setName(name);

            Game.print(chosenPlayer.getName()+" ("+chosenPlayer+") created!");
        }

        //Parse the extra info for the cards
        parseExtraCardInfo();

        dealCards(playingPlayers);
    }

    /**
     * Parse the extra information about each card
     * @return
     */
    private void parseExtraCardInfo() throws IOException {
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
    public void playGame() {
        int playerIndex = 0;
        while (!gameOver) {
            currentPlayer = playingPlayers.get(playerIndex);
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
    public void dealCards(List<Player> players) {
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

    public static Collection<Player> getActivePlayers() {
        return Collections.unmodifiableCollection(playingPlayers);
    }

    /**
     * Restart the game
     * todo implement this
     */
    public static void restart() {

    }

    public static void main(String[] args) throws IOException, InvalidFileException {
        board = new Board();
        gui = new GUI();
        Game game = new Game();
        game.setupGame();
        game.playGame();
    }



    // OLD METHODS TO BE DELETED

    /**
     * Get the user to choose an option from an array of options of a given type
     *
     * @param options the array of options
     * @param text the text at the top of the list of options, e.g. "Choose a weapon:"
     * @param <T> the type of the individual options
     * @return the option that was chosen
     */
    public static <T> T chooseFromArray(T[] options, String text) {
        // Display available options
        System.out.println(text+" (Enter a number 1-"+options.length+")");
        for (int i = 0; i < options.length; i++) {
            System.out.println(i+1 + ". "+options[i]);
        }

        // Get input
        Scanner inputStr = new Scanner(input.nextLine());
        int index = 0;

        // Error check input
        while (index < 1 || index > options.length) {
            while (!inputStr.hasNextInt()) {
                System.out.println("Please enter a number 1-"+options.length);
                inputStr = new Scanner(input.nextLine());
            }
            index = inputStr.nextInt();
        }

        return options[index-1];
    }
}
