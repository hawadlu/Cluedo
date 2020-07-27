import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Game {
    public static void main(String[] args) throws FileNotFoundException {
        showInitialInstructions();
//        System.out.println("Please input something: ");
//        Scanner sc = new Scanner(System.in);
//        System.out.println(sc.nextLine());
    }

    /**
     * Shows the initial instructions.
     * Ask if the user just wants to play or view the instructions
     */
    private static void showInitialInstructions() throws FileNotFoundException {
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
     * Play the game
     */
    private static void playGame() {
        System.out.println("You are playing the game");
    }

    /**
     * Show the instructions
     */
    private static void showInstructions() throws FileNotFoundException {
        File instructions = new File("Assets/Instructions.txt");
        Scanner scanner = new Scanner(instructions);
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
    }

    public enum players {
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
     * Initialise the game
     */
    public void initGame() {
        System.out.println("How many players are playing?");
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextLine());

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

    public static void main(String[] args){
        Game newGame = new Game();
        newGame.initGame();
        newGame.shuffle();
    }
}
