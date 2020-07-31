import java.util.*;

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
    Game.Rooms lastRoom;


    Player(Game.Players name, Position startPos) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        newPos = startPos;
        oldPos = new Position(startPos);
        lastRoom = null;
    }

    /**
     * Adds a card to players hand
     * Use to Create a player
     * @param card the card to add to the hand
     */
    public void addToHand(Card<?> card){
        this.hand.add(card);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return hasLost == player.hasLost &&
                name == player.name &&
                Objects.equals(hand, player.hand) &&
                Objects.equals(newPos, player.newPos) &&
                Objects.equals(oldPos, player.oldPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, hand, hasLost, newPos, oldPos);
    }

    /**
     * Player takes their turn.
     * Gets the choice to move if they have been moved to another room
     * Has the choice of suggest / accuse
     * @param board the board that the game is running on
     */
    public void takeTurn(Board board) {
        boolean willMove = true;
        //If they have been moved to a room, Player chooses if they want to move again
        if(isDiffPos()){
            String response = Game.chooseFromArray(new String[]{"Yes", "No"},
                    "Would you like to move?.\n");
            if(response.equals("No")){ willMove = false; }
        }

        //Moving
        if(willMove){
            int numMove = (int)(Math.random() * 6) + 1;
            numMove += (int)(Math.random() * 6) + 1;

            while(numMove > 0){
                oldPos = new Position(newPos);
                numMove = makeMove(numMove, board);
                board.movePlayer(oldPos, newPos);
                System.out.println(board);
            }
            oldPos = new Position(newPos);
        }

        //Checking if suggest or accuse
        String action;
        if(isDiffPos() && Game.Rooms.valueOf(board.board[newPos.y][newPos.x].room.name) != lastRoom) {
            action = Game.chooseFromArray(new String[]{"Suggest", "Accuse"},
                    "Would you like to Accuse or Suggest?.\n");
        }else{
            action = Game.chooseFromArray(new String[]{"Accuse", "Don't accuse"},
                    "Would you like to Accuse?.\n");
        }

        //Getting player and weapon
        Game.Players player = null; Game.Weapons weapon = null;
        if(action.equals("Accuse") || action.equals("Suggest")) {
            player = Game.chooseFromArray(Game.Players.values(),
                    "Please choose a Person:\n");
            weapon = Game.chooseFromArray(Game.Weapons.values(),
                    "Please choose a Weapon.\n");
        }

        //Accuse / Suggest & getting room
        if(action.equals("Accuse")){
            Game.Rooms room = Game.chooseFromArray(Game.Rooms.values(),
                    "Please choose a Room.\n");
            makeAccuse(room, player, weapon);
        }else if(action.equals("Suggest")){
            Game.Rooms room = Game.Rooms.valueOf(board.board[newPos.y][newPos.x].room.name);
            lastRoom = room;
            new Suggest(room, player, weapon, this).apply();
        }
    }

    /**
     * Make a move action
     * @param numMove the amount that this player can move this turn
     * @param board the board the game is running on
     * @return the amount of moves left
     */
    public int makeMove(int numMove, Board board){
        boolean hasMoved = false;
        String response = "";
        System.out.println("--------------------------------------------");
        System.out.println("Your turn to move: you have "+numMove+" moves.");

        //Creating new move
        while(!hasMoved){
            response = "";
            while(response.length() < 1) {
                System.out.println("'L' for Left, 'R' for Right, 'U' for Up and 'D' for Down, 'S' to show the board");
                response = new Scanner(System.in).nextLine().toLowerCase();
            }
            try {
                //show the board if requested
                if (response.equals("s")) {
                    System.out.println(board);
                    return numMove;
                } else {
                    //process the requested move
                    hasMoved = new Move(board, this, response.split(""), numMove).apply();
                }
            }catch(InvalidMoveException e){ System.out.println("Invalid move, try again."); }
        }
        return numMove-response.length();
    }

    /**
     * Make an accusation
     * @param room the room that is being accused
     * @param player the player that is being accused
     * @param weapon the weapon that is being accused
     */
    public void makeAccuse(Game.Rooms room, Game.Players player, Game.Weapons weapon){
        boolean hasAccused = false;
        while(!hasAccused){
            hasAccused = new Accuse(room, player, weapon, this).apply();
        }
    }

    /**
     * Has this player moved between their turns (oldPos == newPos)
     * @return boolean if player moved
     */
    public boolean isDiffPos(){
        return !oldPos.equals(newPos);
    }

    /**
     * Get this players hand
     * @return arraylist of cards in this players hand
     */
    public ArrayList<Card<?>> getHand(){ return hand; }

    /**
     * Get name of this player
     * @return enum from Players in Game class
     */
    public Game.Players getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }


    public Position getPos(){ return newPos; }

    /**
     * Look through this hand for any matches
     * @param room Game.Rooms
     * @param suspect Game.Players
     * @param weapon Game.Weapons
     * @return arraylist of the matches
     */
    public ArrayList<Card<?>> addMatches(Card<Game.Rooms> room, Card<Game.Players> suspect, Card<Game.Weapons> weapon) {
        ArrayList<Card<?>> matches = new ArrayList<>();
        for (Card<?> card : hand)
            if (card.equals(room) || card.equals(suspect) || card.equals(weapon))
                matches.add(card);
        return matches;
    }

    public void setHasLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    public boolean hasLost() {
        return hasLost;
    }
}
