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

    /**
     * Adds a card to players hand
     * Use to Create a plaeyr
     * @param card
     */
    public void addToHand(Card<?> card){
        this.hand.add(card);
    }

    /**
     * Player takes their turn.
     * Gets the choice to move if they have been moved to another room
     * Has the choice of suggest / accuse
     */
    public void takeTurn(Board board) throws InvalidActionException {
        //Print out board
        System.out.println(board.toString());

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
            boolean hasMoved = false;
            String response = "";

            System.out.println("Your turn to move: you have "+numMove+" moves.");

            //Creating new move
            while(!hasMoved){
                while(response.length() != numMove) {
                    System.out.println("Please type a valid number of moves. \n" +
                            "'L' for Left, 'R' for Right, 'U' for Up and 'D' for Down");
                    response = new Scanner(System.in).nextLine().toLowerCase();
                }
                try {
                    hasMoved = new Move(board, this, response.split(""), numMove).apply();
                }catch(InvalidActionException e){ System.out.println("Invalid move, try again."); }
            }
        }

        //Checking if suggest or accuse
        String action = Game.chooseFromArray(new String[]{"Suggest", "Accuse"},
                "Would you like to Accuse or Suggest?.\n");

        //Getting weapon, room, player
        Game.Players player = Game.chooseFromArray(Game.Players.values(),
                "Please choose a Person:\n");
        Game.Weapons weapon = Game.chooseFromArray(Game.Weapons.values(),
                "Please choose a Weapon.\n");
        Game.Rooms room;
        if(action.equals("Accuse")) {
            room = Game.chooseFromArray(Game.Rooms.values(),
                    "Please choose a Weapon.\n");
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
                try {
                    hasSuggested = new Suggest(room, player, weapon, this).apply();
                }catch(InvalidActionException e){ System.out.println("Invalid move, try again."); }
            }
        }
    }

    /**
     * @return Players hand
     */
    public ArrayList<Card<?>> getHand(){ return hand; }

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
