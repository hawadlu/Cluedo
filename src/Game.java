import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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

    public enum Players {
        SCARLET,
        PLUM,
        WHITE,
        PEACOCK,
        GREEN,
        MUSTARD;
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
        LOUNGE;

    }

    public enum Weapons{
        CANDLESTICK,
        DAGGER,
        LEAD_PIPE,
        REVOLVER,
        ROPE,
        SPANNER;
    }

    /**
     * Shows the initial instructions.
     * Ask if the user just wants to play or view the instructions
     */
    public void showInitialInstructions() throws FileNotFoundException {
        System.out.println("Welcome to Cluedo!\n" +
                "To view the instructions type 'instructions'\n" +
                "To play the game type 'play'");

        String response = new Scanner(System.in).nextLine();

        if (response.equals("instructions")) showInstructions();
        else if (response.equals("play")) playGame();
        else {
            System.out.println("Invalid response, try again!");
            showInitialInstructions();
        }
    }

    /**
     * Show the instructions
     */
    public void showInstructions() throws FileNotFoundException {
        File instructions = new File("Assets/Instructions.txt");
        Scanner scanner = new Scanner(instructions);
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
    }

    /**
     * Get the number of players from the user
     * @param input the scanner that is scanning the input stream
     * @return the number of players playing the game
     */
    public int getNumPlayers(Scanner input) {
        System.out.println("How many players are playing? (2-6 Players only)");
        Scanner inputStr = new Scanner(input.nextLine());
        int num = 0;

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
     * @param numPlayers the number of players that will be playing the game
     * @param input the scanner that is scanning the input stream
     * @return an arraylist of players
     */
    public ArrayList<Player> createPlayers(int numPlayers, Scanner input) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            Players player = chooseFromArray(Players.values(), "Player "+i+" choose your character:", input);
            //players.add(new Player());
        }
        return null;
    }

    /**
     * Get the user to choose an option from an array of options of a given type
     * @param options the array of options
     * @param text the text at the top of the list of options, e.g. "Choose a weapon:"
     * @param input the scanner that is scanning the input stream
     * @param <T> the type of the individual options
     * @return the option that was chosen
     */
    public static <T> T chooseFromArray(T[] options, String text, Scanner input) {
        System.out.println(text+" (Enter a number 1-"+options.length+")");
        for (int i = 0; i < options.length; i++) {
            System.out.println(i+1 + ". "+options[i]);
        }

        Scanner inputStr = new Scanner(input.nextLine());
        int index = 0;

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
     * Play the game
     */
    public void playGame() {
        Scanner input = new Scanner(System.in);
        int numPlayers = getNumPlayers(input);
        List<Player> players = createPlayers(numPlayers, input);

    }

    /**
     * Creates cards, shuffles, removes 3 for accuse
     *
     * TO-DO: Add rest into players hands.
     */
    public void shuffle(List<Player> players, int numPlayers){
        //Create ArrayLists of each card type
        ArrayList<Card> playerCards = new ArrayList<>();
        ArrayList<Card> weaponCards = new ArrayList<>();
        ArrayList<Card> roomCards = new ArrayList<>();

        for(Players p : Players.values()){ playerCards.add(new PlayerCard(p)); }
        for(Weapons w : Weapons.values()){ weaponCards.add(new WeaponCard(w)); }
        for(Rooms r : Rooms.values()){ roomCards.add(new RoomCard(r)); }

        //Shuffling
        Collections.shuffle(playerCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(roomCards);

        //Add to accuse
        accusePlayer = playerCards.get(0).getPlayer();     playerCards.remove(0);
        accuseRoom = roomCards.get(0).getRoom();       roomCards.remove(0);
        accuseWeapon = weaponCards.get(0).getWeapon();     weaponCards.remove(0);
        System.out.println("Accuse List:"+ accusePlayer.toString()+", "+accuseRoom.toString()+", "+accuseWeapon.toString());

        //Add rest to big list
        ArrayList<Card> remainingCards = new ArrayList<>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(roomCards);
        remainingCards.addAll(weaponCards);
        System.out.println("Remaining cards: "+remainingCards.size());

        //Divide Among players
        int currentPlayer = 0;
        for(Card c : remainingCards){
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
        game.showInitialInstructions();
    }
}
