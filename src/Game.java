import java.util.Scanner;

public class Game {
    public static void main(String[] args){
        System.out.println("Welcome to Cluedo!\n" +
                "To view the instructions type 'instructions'\n" +
                "To play the game type 'play'");

        String response = new Scanner(System.in).nextLine();

        if (response.equals("instructions")) showInstructions();
        else if (response.equals("play")) playGame();

        System.out.println("Please input something: ");
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextLine());
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
    private static void showInstructions() {
        //todo write the actual instructions
        System.out.println("You are viewing the instructions");
    }

    public enum players {
        SCARLET,
        PLUM,
        WHITE,
        PEACOCK,
        GREEN,
        MUSTARD
    }

    public enum rooms{
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

    public enum weapons{
        CANDLESTICK,
        DAGGER,
        LEAD_PIPE,
        REVOLVER,
        ROPE,
        SPANNER
    }
}
