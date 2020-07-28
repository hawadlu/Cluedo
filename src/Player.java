import java.util.ArrayList;
import java.util.Scanner;

/**
 * Physical PLayer.
 * -Contains their hand, pos & if they've lost
 */
public class Player {
    Game.Players name;
    ArrayList<Card> hand;
    boolean hasLost;
    Position newPos;
    Position oldPos;

<<<<<<< Updated upstream
    Player(Game.Players name, Position pos) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        position = pos;
=======
    Player(Game.Players name, Position startPos) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        newPos = startPos;
        oldPos = startPos;
>>>>>>> Stashed changes
    }

    public void addToHand(Card card){
        this.hand.add(card);
    }

    /**
     * Player takes a turn,
     * moves, suggests and accuses
     */
    public void takeTurn(Board board){
        boolean willMove = true;

        //If they have been moved to a room, Player chooses if they want to move again
        if(oldPos != newPos){
            String response = "";
            System.out.println("Do you want to move?\nType '1' for yes or '2' for no");
            while(!response.equals("1") && !response.equals("2")) {
                response = new Scanner(System.in).nextLine();
            }
            if(response.equals("2")){ willMove = false; }
        }

        //Moving
        if(willMove){
            int numMove = (int)(Math.random() * 6) + 1;
            numMove += (int)(Math.random() * 6) + 1;
            System.out.println("Your turn to move: you have "+numMove+" moves.");
            String response = "";
            System.out.println("Please type a valid number of moves. \n" +
                    "'L' for Left, 'R' for Right, 'U' for Up and 'D' for Down");
            while(response.length() != numMove) {
                response = new Scanner(System.in).nextLine();
            }

            //Creating new move
            boolean hasMoved = false;
            while(!hasMoved){
                hasMoved = new Move(board, this, response, numMove);
            }
        }

        //Checking if
        System.out.println("Would you like to accuse or suggest?\n" +
                "Please type '1' for accuse or '2' for suggest.");
        String response = "";
        while(!response.equals("1") && !response.equals("2")) {
            response = new Scanner(System.in).nextLine();
        }

        if(response.equals("1")){

        }
    }

    public ArrayList<Card> getHand(){ return hand; }

    @Override
    public String toString() {
        return name.toString();
    }
}
