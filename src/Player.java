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
     * @param allPlayers the list of all the players playing the game
     */
    public void takeTurn(Board board, List<Player> allPlayers) {
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
                numMove = makeMove(numMove, board);
                board.movePlayer(oldPos, newPos);
                System.out.println(board);
                System.out.println("old - "+oldPos);
                System.out.println(newPos);
            }
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
        Game.Players player = Game.chooseFromArray(Game.Players.values(),
                "Please choose a Person:\n");
        Game.Weapons weapon = Game.chooseFromArray(Game.Weapons.values(),
                "Please choose a Weapon.\n");

        //Accuse / Suggest & getting room
        if(action.equals("Accuse")){
            Game.Rooms room = Game.chooseFromArray(Game.Rooms.values(),
                    "Please choose a Weapon.\n");
            makeAccuse(room, player, weapon);
        }else if(action.equals("Suggest")){
            Game.Rooms room = Game.Rooms.valueOf(board.board[newPos.y][newPos.x].room.name);
            lastRoom = room;
            makeSuggest(room, player, weapon, allPlayers);
        }
        oldPos = new Position(newPos);
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
                System.out.println("Please type a valid number of moves. \n" +
                        "'L' for Left, 'R' for Right, 'U' for Up and 'D' for Down");
                response = new Scanner(System.in).nextLine().toLowerCase();
            }
            try {
                hasMoved = new Move(board, this, response.split(""), numMove).apply();
            }catch(InvalidMoveException e){ System.out.println("Invalid move, try again."); }
        }
        return numMove-response.length();
    }

    /**
     * Make a suggestion
     * @param room the room that is being suggested
     * @param player the player that is being suggested
     * @param weapon the weapon that is being suggested
     * @param allPlayers the list of all players that are playing the game
     */
    public void makeSuggest(Game.Rooms room, Game.Players player, Game.Weapons weapon, List<Player> allPlayers){
        boolean hasSuggested = false;
        while(!hasSuggested){
            try {
                hasSuggested = new Suggest(room, player, weapon, this, allPlayers).apply();
            }catch(InvalidMoveException e){ System.out.println("Invalid move, try again."); }
        }
    }

    /**
     * Make an accusation
     * @param room the room that is being accused
     * @param player the player that is veing accused
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
        return oldPos.x != newPos.x && oldPos.y != newPos.y;
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
    

    /**
     * Look through this hand for any matches
     * @param room Game.Rooms
     * @param accused Game.Players
     * @param weapon Game.Weapons
     * @return arraylist of the matches
     */
    public ArrayList<Card<?>> addMatches(Card<Game.Rooms> room, Card<Game.Players> accused, Card<Game.Weapons> weapon) {
        ArrayList<Card<?>> matches = new ArrayList<>();
        if (hand.contains(room)) matches.add(room);
        if (hand.contains(accused)) matches.add(accused);
        if (hand.contains(weapon)) matches.add(weapon);

        return matches;
    }
}
