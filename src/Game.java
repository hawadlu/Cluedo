import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Overall class to run game
 * -Initializes players, card and board
 * -Loops through players to run game
 * -Checks if game is finished
 */
public class Game {
    public static Players accusePlayer;
    public static Rooms accuseRoom;
    public static Weapons accuseWeapon;
    public static Scanner input = new Scanner(System.in);
    public static boolean gameOver = false;

    Board board;

    public enum Players {
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
        BILLARD_ROOM,
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
    public void showMenu() throws FileNotFoundException {
        String response = chooseFromArray(new String[]{"Instructions", "Play"}, "Welcome to Cluedo!");

        if (response.equals("Instructions")) showInstructions();
        else if (response.equals("Play")) playGame();
        else {
            System.out.println("Invalid response, try again!");
            showMenu();
        }
    }

    /**
     * Show the instructions
     */
    public void showInstructions() throws FileNotFoundException {
        File instructions = new File("Assets/Instructions.txt");
        Scanner scanner = new Scanner(instructions);
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
        System.out.println();
        showMenu();
    }

    /**
     * Get the number of players from the user
     *
     * @return the number of players playing the game
     */
    public int getNumPlayers() {
        System.out.println("How many players are playing? (2-6 Players only)");
        Scanner inputStr = new Scanner(input.nextLine());
        int num = 0;

        // Error check input
        while (num < 2 || num > 6) {
            while (!inputStr.hasNextInt()) {
                System.out.println("Please enter a number 2-6");
                inputStr = new Scanner(input.nextLine());
            }
            num = inputStr.nextInt();
        }

        return num;
    }

    /**
     * Create the players that will be playing the game
     *
     * @param numPlayers the number of players that will be playing the game
     * @return an arraylist of players
     */
    public ArrayList<Player> createPlayers(int numPlayers) {
        ArrayList<Player> players = new ArrayList<>();
        // Make a list of available players so that chosen players can be removed from the list
        ArrayList<Players> availablePlayers = new ArrayList<>(Arrays.asList(Players.values()));

        // Make player objects for each player that wants to play
        for (int i = 0; i < numPlayers; i++) {
            Players player = chooseFromArray(availablePlayers.toArray(new Players[]{}), "Player "+(i+1)+" choose your character:");
            availablePlayers.remove(player);

            // Catch choosing a player that doesnt have a starting position set
            try {
                Position startPos = getStartingPosition(player);
                players.add(new Player(player, startPos));
            }
            catch (InvalidPlayerException e) {
                System.out.println(e.toString());
                i--;
            }
        }
        return players;
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

    /**
     * Gets the default starting position of a player
     *
     * @param player the player enum to find the position of
     * @return a position object, containing the starting position coordinates
     * @throws InvalidPlayerException if the provided player does not have a starting position
     */
    public Position getStartingPosition(Players player) throws InvalidPlayerException {
        switch (player) {
            case WHITE: return new Position(9, 0);
            case GREEN: return new Position(14, 0);
            case PEACOCK: return new Position(23, 6);
            case PLUM: return new Position(23, 19);
            case SCARLET: return new Position(7, 24);
            case MUSTARD: return new Position(0, 17);
        }
        throw new InvalidPlayerException(player.toString());
    }

    /**
     * Play the game
     */
    public void playGame() {
        gameOver = false;
        int numPlayers = getNumPlayers();
        ArrayList<Player> players = createPlayers(numPlayers);
        dealCards(players, numPlayers);

        //Create the board
        board = new Board(players);

        int currentPlayer = 0;
        while (!gameOver) {
            Player player = players.get(currentPlayer%numPlayers);
            try {
                System.out.println(player.getName() + "'s Turn");
                player.takeTurn(board);
            } catch (InvalidActionException e) {System.out.println("CONNOR FIX THA THING");}

            currentPlayer++;
        }
    }

    /**
     * Creates cards, chooses a person, weapon and room at random to be the murder items
     * shuffles the remaining cards and deals them out to the players

     * @param players the list of players playing the game to be dealt cards to
     * @param numPlayers the number of players playing the game
     */
    public void dealCards(List<Player> players, int numPlayers){
        //Create ArrayLists of each card type
        ArrayList<Card<Players>> playerCards = new ArrayList<>();
        ArrayList<Card<Weapons>> weaponCards = new ArrayList<>();
        ArrayList<Card<Rooms>> roomCards = new ArrayList<>();

        for(Players p : Players.values()){ playerCards.add(new Card<>(p)); }
        for(Weapons w : Weapons.values()){ weaponCards.add(new Card<>(w)); }
        for(Rooms r : Rooms.values()){ roomCards.add(new Card<>(r)); }

        //Shuffling
        Collections.shuffle(playerCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(roomCards);

        //Add to accuse
        accusePlayer = playerCards.get(0).getEnum();     playerCards.remove(0);
        accuseRoom = roomCards.get(0).getEnum();       roomCards.remove(0);
        accuseWeapon = weaponCards.get(0).getEnum();     weaponCards.remove(0);
        /* TESTING
        System.out.println("Accuse List: "+ accusePlayer.toString()+", "+accuseRoom.toString()+", "+accuseWeapon.toString());
         */

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


        System.out.println("Each Players Cards: ");
        for(Player p : players){
            System.out.println(p.toString() + ": "+p.getHand().toString());
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game();
        game.showMenu();
    }
}
