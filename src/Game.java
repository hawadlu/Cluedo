import java.util.Scanner;

public class Game {
    int numPlayers;

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
     * Initialise the game
     */
    public void initGame() {
        System.out.println("How many players are playing?");
        Scanner sc = new Scanner(System.in);
        System.out.println(sc.nextLine());

    }

    public static void main(String[] args){
        Game newGame = new Game();
        newGame.initGame();
    }
}
