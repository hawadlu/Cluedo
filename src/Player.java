import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Physical PLayer.
 * -Contains their hand, pos & if they've lost
 */
public class Player {
    private final Game.Players name;
    private final ArrayList<Card<?>> hand;
    private boolean hasLost;
    private Position newPos;
    private Position oldPos;
    private Game.Rooms lastRoom;
    private HashSet<Tile> tilesThisTurn = new HashSet<>();
    private boolean suggested = false;
    private int movement = 0;
    private final BufferedImage image;

    private enum Actions {VIEW_HAND, MOVE, SUGGEST, ACCUSE, LEAVE_ROOM, END_TURN }

    Player(Game.Players name, Position startPos) throws InvalidFileException {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        newPos = startPos;
        oldPos = new Position(startPos);
        lastRoom = null;
        try {
            image = ImageIO.read(new File("Assets/" + name.toString() + ".png"));
        }catch(Exception e){ throw new InvalidFileException(name.toString() + ".png"); }
    }

    /**
     * Player takes their turn.
     * Gets the choice to move if they have been moved to another room
     * Has the choice of suggest / accuse
     *
     * @param board the board that the game is running on
     */
    public void takeTurn(Board board) {
        System.out.println(board);

        // Simulates rolling dice for movement
        movement = Game.rollDice();

        tilesThisTurn = new HashSet<>();
        suggested = false;
        boolean takingTurn = true;

        while (takingTurn) {
            System.out.println(this.getName() + "'s Turn - "+movement+" moves left");
            Actions action = Game.chooseFromArray(getActions(board), "What would you like to do?");

            switch (action) {
                case VIEW_HAND:
                    System.out.println("Your hand: " + getHand());
                    break;

                case MOVE:
                    System.out.println(board);
                    makeMove(board);
                    System.out.println(board);
                    break;

                case SUGGEST:
                case ACCUSE:
                    // Choose a suspect and weapon to accuse/suggest
                    Game.Players player = Game.chooseFromArray(Game.Players.values(), "Please choose a Suspect:");
                    Game.Weapons weapon = Game.chooseFromArray(Game.Weapons.values(), "Please choose a Weapon:");
                    Game.Rooms room;
                    String confirm;

                    switch (action) {
                        case ACCUSE:
                            room = Game.chooseFromArray(Game.Rooms.values(), "Please choose a Room:");

                            confirm = Game.chooseFromArray(new String[]{"Yes", "No"}, "Are you sure you want to Accuse "+player+" with the "+weapon+" in the "+room+"?");
                            if (confirm.equals("No")) break;

                            new Accuse(room, player, weapon, this).apply();
                            takingTurn = false;
                            break;
                        case SUGGEST:
                            room = board.getTile(newPos).getEnum();

                            confirm = Game.chooseFromArray(new String[]{"Yes", "No"}, "Are you sure you want to Suggest "+player+" with the "+weapon+" in the "+room+"?");
                            if (confirm.equals("No")) break;

                            lastRoom = room;
                            suggested = true;
                            movement = 0;
                            new Suggest(room, player, weapon, this).apply();
                    }
                    System.out.println(board);
                    break;

                case LEAVE_ROOM:
                    leaveRoom(board);
                    break;

                case END_TURN:
                    takingTurn = false;
                    break;
            }
        }

        //Update lastRoom, will be null if outside of room, used in accuse
        if (!suggested) lastRoom = null;
        else lastRoom = board.getTile(newPos).getEnum();
    }

    /**
     * Generate an array of actions that can be taken
     *
     * @param board the board the game is being played on
     * @return the array of actions that can be taken
     */
    public Actions[] getActions(Board board) {
        List<Actions> actions = new ArrayList<>();
        Game.Rooms currentRoom = board.getTile(newPos).getEnum();
        boolean inRoom = board.getTile(newPos).isRoom();

        actions.add(Actions.VIEW_HAND);

        if (inRoom && movement > 0)
            actions.add(Actions.LEAVE_ROOM);
        else if (movement > 0)
            actions.add(Actions.MOVE);

        if (inRoom && !suggested && currentRoom != lastRoom)
            actions.add(Actions.SUGGEST);

        actions.add(Actions.ACCUSE);
        actions.add(Actions.END_TURN);

        return actions.toArray(new Actions[]{});
    }

    /**
     * Attempt to leave the room the player is in
     *
     * @param board the board that the game is playing on
     */
    public void leaveRoom(Board board) {
        Room room = board.getTile(newPos).getRoom();
        int numDoors = room.getNumberOfDoors();
        List<Position> doors = new ArrayList<>();

        // Display doors on board
        room.toggleDoorNumbers();
        System.out.println(board);

        // Create Questions
        List<String> options = new ArrayList<>();

        // Ask what door they want to leave from
        for (int i = 0; i < numDoors; i++) {
            Position doorPos = room.getDoor(i);
            if (!board.getTile(doorPos).hasPlayer()) {
                options.add("Door " + (i + 1));
                doors.add(doorPos);
            }
        }
        options.add("Stay in room");
        String doorString = Game.chooseFromArray(options.toArray(new String[]{}),
                "What door would you like to leave from?\n");

        // Leave the room
        if(!doorString.equals("Stay in room")) {
            int door = options.indexOf(doorString);
            System.out.println("TEST ~~~~~ "+door);
            System.out.println("TEST ~~~~~ "+doors.toString());
            System.out.println("TEST ~~~~~ "+doorString);
            board.movePlayer(newPos, doors.get(door));

            newPos = new Position(doors.get(door));
            oldPos = new Position(newPos);
            movement -= 1;

            tilesThisTurn.addAll(room.getTiles());

            room.toggleDoorNumbers();
            System.out.println(board);
        }
    }

    /**
     * Make a move action
     *
     * @param board the board the game is running on
     */
    public void makeMove(Board board){
        // Take movement commands until run out of movement
        while(movement > 0){
            oldPos = new Position(newPos);

            System.out.println(board);
            System.out.println(name +" has "+movement+" moves left.");

            // Request intended movement
            System.out.println("'L' for Left, 'R' for Right, 'U' for Up and 'D' for Down" +
                    "\n(You can make multiple moves at once e.g. LLDLLU)" +
                        "\nEnter nothing to stop moving");
            String response = new Scanner(System.in).nextLine().toLowerCase();

            // Player chooses to stop moving
            if (response.trim().length() == 0) return;

            // Add the current position to the set of visited tiles
            tilesThisTurn.add(board.getTile(newPos));

            // Process the requested move
            String[] actions = response.split("");
            try {
                // Check to see if the player is trying to move more than their remaining movement
                if (actions.length > movement) throw new InvalidMoveException("Move too long");

                // Try to apply the move
                new Move(board, this, actions).apply();

                // Move worked, reduce movement
                movement -= actions.length;

                // If entered room, set movement to 0
                if(board.getTile(newPos).getEnum() != null)
                    movement = 0;

                // Execute the movement on the board
                board.movePlayer(oldPos, newPos);
                oldPos = new Position(newPos);

            } catch(InvalidMoveException e) {
                System.out.println("Try again (Press Enter to Continue)");
                Game.input.nextLine();
            }
        }
    }

    /**
     * Look through this hand for any matches
     *
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

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
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

    /*
    GETTERS AND SETTERS
     */

    /**
     * Get this players hand
     *
     * @return arraylist of cards in this players hand
     */
    public ArrayList<Card<?>> getHand(){ return hand; }

    /**
     * Get name of this player
     *
     * @return enum from Players in Game class
     */
    public Game.Players getName() {
        return name;
    }

    /**
     * Get the position of this player
     *
     * @return the position of this player
     */
    public Position getPos(){ return newPos; }

    /**
     * Get the tiles that this player has been in this and last turn
     *
     * @return a hashset of tiles that this player was in this and last turn
     */
    public HashSet<Tile> getTilesThisTurn() {
        return tilesThisTurn;
    }

    /**
     * Get the newest position of this player
     *
     * @return newest position of this player
     */
    public Position getNewPos() {
        return newPos;
    }

    /**
     * Adds a card to players hand
     * Use to Create a player
     *
     * @param card the card to add to the hand
     */
    public void addToHand(Card<?> card){
        this.hand.add(card);
    }

    /**
     * Set the newest position of this player
     *
     * @param newPos the new position to be set as this players newest position
     */
    public void setNewPos(Position newPos) {
        this.newPos = newPos;
    }

    /**
     * Set whether this player has lost or not
     *
     * @param hasLost whether this player has lost or not
     */
    public void setHasLost(boolean hasLost) {
        this.hasLost = hasLost;
    }

    /**
     * Has this player lost?
     *
     * @return boolean response
     */
    public boolean hasLost() {
        return hasLost;
    }

    /**
     * Drawing a player at current position
     *
     * @param g graphics
     * @param pos position of current player
     */
    protected void drawPlayer(Graphics g, Position pos){
        g.drawImage(image, pos.x, pos.y, null);
    }
}
