import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Overall class to run game
 * -Initializes players, card and board
 * -Loops through players to run game
 * -Checks if game is finished
 */
public class Game {
    int numPlayers;
    ArrayList<Card> accuseList;

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
    private void showInitialInstructions() throws FileNotFoundException {
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
    private void playGame() {
        System.out.println("You are playing the game");
        System.out.println("How many players?\n");
    }

    /**
     * Show the instructions
     */
    private void showInstructions() throws FileNotFoundException {
        File instructions = new File("Assets/Instructions.txt");
        Scanner scanner = new Scanner(instructions);
        while (scanner.hasNextLine()) System.out.println(scanner.nextLine());
    }

    /**
     * Creates cards, shuffles, removes 3 for accuse
     *
     * TO-DO: Add rest into players hands.
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
        accuseList = new ArrayList<>();
        accuseList.add(playerCards.get(0));     playerCards.remove(0);
        accuseList.add(roomCards.get(0));       roomCards.remove(0);
        accuseList.add(weaponCards.get(0));     weaponCards.remove(0);
        System.out.println("Accuse List:"+ accuseList.toString());

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
