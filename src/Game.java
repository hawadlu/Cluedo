import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Game {

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

        return inputStr.nextInt();
    }

    /**
     * Create the players that will be playing the game
     * @param numPlayers the number of players that will be playing the game
     * @return an arraylist of players
     */
    public ArrayList<Player> createPlayers(int numPlayers) {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            //players.add(new Player());
        }
        return null;
    }

    /**
     * Play the game
     */
    public void playGame() {
        Scanner input = new Scanner(System.in);
        int numPlayers = getNumPlayers(input);
        List<Player> players = createPlayers(numPlayers);

    }

    /**
     * Creates cards, shuffles, removes 3 for accuse
     *
     * TO-DO: Add rest into players hands.
     *        Add to board class?
     */
    public void shuffle(){
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
        ArrayList<Card> accuse = new ArrayList<>();
        accuse.add(playerCards.get(0));     playerCards.remove(0);
        accuse.add(roomCards.get(0));       roomCards.remove(0);
        accuse.add(weaponCards.get(0));     weaponCards.remove(0);
        System.out.println("Accuse List:"+ accuse.toString());

        //Add rest to big list
        ArrayList<Card> remainingCards = new ArrayList<>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(roomCards);
        remainingCards.addAll(weaponCards);
        System.out.println("Remaining cards: "+remainingCards.size());

    }

    public static void main(String[] args) throws FileNotFoundException {
        Game game = new Game();
        game.showInitialInstructions();
    }
}
