import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static Board board;
    public static GUI gui;

    private final static Die die1 = new Die(), die2 = new Die();
    private ArrayList<Player> playingPlayers;

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
     * - Sets gameover to false
     * - Creates board
     * - Creates players
     * - Deals cards to players
     */
    public void setupGame() {
        gameOver = false;
        createPlayers();
        board = new Board();

        // Set players to their starting positions
        for (Player player : players)
            board.getTile(player.getNewPos()).addPlayer(player);
        gui.redraw();

        // Get number of players
        Integer numPlayers = makeDropDown(new Integer[]{2, 3, 4, 5, 6}, "Number of Players",
                                            "How many people are playing?");

        // Setup the playable players
        playingPlayers = new ArrayList<>();
        ArrayList<Suspects> availablePlayers = new ArrayList<>(Arrays.asList(Suspects.values()));
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
            String name = null;
            while (name == null)
                name = JOptionPane.showInputDialog(gui.window,
                        "Player "+i+" enter your name:", "Player "+i+" Creation",
                        JOptionPane.PLAIN_MESSAGE);
            chosenPlayer.setName(name);

            Game.print(chosenPlayer.getName()+" ("+chosenPlayer+") created!");
        }

        dealCards(playingPlayers);
    }

    /**
     * Play the game
     */
    public void playGame() {
        int currentPlayer = 0;
        while (!gameOver) {
            Player player = players.get(currentPlayer % playingPlayers.size());
            player.takeTurn(board);

            if (player.hasLost()){
                playingPlayers.remove(player);
            } else currentPlayer++;

            if(playingPlayers.size() == 0) {
                System.out.println("All players have lost, game is over.");
                gameOver = true;
            }
        }
    }

    /**
     * Creates cards, chooses a person, weapon and room at random to be the murder items
     * shuffles the remaining cards and deals them out to the players

     * @param players the list of players playing the game to be dealt cards to
     */
    public void dealCards(List<Player> players){
        int numPlayers = players.size();

        //Create ArrayLists of each card type
        ArrayList<Card<Suspects>> playerCards = new ArrayList<>();
        ArrayList<Card<Weapons>> weaponCards = new ArrayList<>();
        ArrayList<Card<Rooms>> roomCards = new ArrayList<>();

        for(Suspects p : Suspects.values()){ playerCards.add(new Card<>(p)); }
        for(Weapons w : Weapons.values()){ weaponCards.add(new Card<>(w)); }
        for(Rooms r : Rooms.values()){ roomCards.add(new Card<>(r)); }

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
     * Roll the dice and get the result
     * @return the total number rolled on the dice
     */
    public static int rollDice() {
        int total = die1.roll() + die2.roll();
        gui.redraw();
        return total;
    }

    /**
     * Print a message to the GUI console
     * @param text the message to print
     */
    public static void print(String text) {
        gui.addToConsole(text);
        gui.consolePanel.redraw();
    }

    public static void main(String[] args) throws IOException {
        gui = new GUI();
        Game game = new Game();
        game.setupGame();
        //game.playGame();
    }



    // OLD METHODS TO BE DELETED

    /**
     * 'Clear' the output of the console by printing a bunch of newline characters
     */
    public static void clearOutput() {
        System.out.print(String.join("", Collections.nCopies(30, "\n")));
    }

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
