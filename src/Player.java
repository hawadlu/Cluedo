import java.util.ArrayList;
import java.util.Scanner;

/**
 * Physical PLayer.
 * -Contains their hand, pos & if they've lost
 */
public class Player {
    Game.Players name;
    ArrayList<Card<?>> hand;
    boolean hasLost;
    Position newPos;
    Position oldPos;

    Player(Game.Players name, Position startPos) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        newPos = startPos;
        oldPos = startPos;
    }

    public void addToHand(Card<?> card){
        this.hand.add(card);
    }

    /**
     * Player takes a turn,
     * moves, suggests and accuses
     */
    public void takeTurn(Board board) throws InvalidActionException {
        boolean willMove = true;

        //If they have been moved to a room, Player chooses if they want to move again
        if(oldPos != newPos){
            String response = Game.chooseFromArray(new String[]{"Yes", "No"},
                    "Would you like to move?.\n");
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
                hasMoved = new Move(board, this, response.split(""), numMove).apply();
            }
        }

        //Checking if suggest or accuse
        String action = Game.chooseFromArray(new String[]{"Suggest", "Accuse"},
                "Would you like to Accuse or Suggest?.\n");

        //Getting weapon, room, player
        Game.Players player = Game.chooseFromArray(Game.Players.values(),
                "Please choose a Person:\n");
        Game.Weapons weapon = Game.chooseFromArray(Game.Weapons.values(),
                "Please choose a Weapon.\n", new Scanner(System.in));
        Game.Rooms room;
        if(action.equals("Accuse")) {
            room = Game.chooseFromArray(Game.Rooms.values(),
                    "Please choose a Weapon.\n", new Scanner(System.in));
        }else {
            room = Game.Rooms.valueOf(board.board[newPos.y][newPos.x].room.name);
        }

        //Accuse / Suggest
        if(action.equals("Accuse")){
            boolean hasAccused = false;
            while(!hasAccused){
                hasAccused = new Accuse(room, player, weapon, this).apply();
            }
        }else{
            boolean hasSuggested = false;
            while(!hasSuggested){
                hasSuggested = new Suggest(room, player, weapon, this).apply();
            }
        }
    }

    public ArrayList<Card<?>> getHand(){ return hand; }

    @Override
    public String toString() {
        return name.toString();
    }
}
