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
     * Player takes a turn,
     * moves, suggests and accuses
     */
    public void takeTurn(Board board, List<Player> players) throws InvalidActionException {
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
                response = new Scanner(System.in).nextLine().toLowerCase();
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
        Game.Players accused = Game.chooseFromArray(Game.Players.values(),
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
                hasAccused = new Accuse(room, accused, weapon, this).apply();
            }
        }else{
            boolean hasSuggested = false;
            while(!hasSuggested){
                hasSuggested = new Suggest(room, accused, weapon, this, players).apply();
            }
        }
    }

    public ArrayList<Card<?>> getHand(){ return hand; }

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
    public ArrayList<Card> addMatches(Game.Rooms room, Game.Players accused, Game.Weapons weapon) {
        ArrayList<Card> matches = new ArrayList<>();
        for (Card card: hand) {
            if (hand.contains(new Card<>(room))) matches.add(card);
            if (hand.contains(new Card<>(accused))) matches.add(card);
            if (hand.contains(new Card<>(weapon))) matches.add(card);
        }

        return matches;
    }
}
