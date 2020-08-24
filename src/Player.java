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
    private final Game.Suspects suspect;
    private String name;
    private final ArrayList<Card<?>> hand;
    private boolean hasLost;
    private Position newPos;
    private Position oldPos;
    private Game.Rooms lastRoom;
    private HashSet<Tile> tilesThisTurn = new HashSet<>();
    private boolean suggested = false;
    private int movement = 0;
    private final BufferedImage image;
    private boolean takingTurn = false;
    public boolean hasMoved = false;

    public enum Actions {MOVE, SUGGEST, ACCUSE, END_TURN }

    Player(Game.Suspects suspect, Position startPos) throws InvalidFileException {
        this.suspect = suspect;
        hand = new ArrayList<>();
        hasLost = false;
        newPos = startPos;
        oldPos = new Position(startPos);
        lastRoom = null;
        try {
            image = ImageIO.read(new File("Assets/PlayerPieces/" + suspect.toString() + ".png"));
        }catch(Exception e){ throw new InvalidFileException(suspect.toString() + ".png"); }
    }

    /**
     * Player takes their turn.
     * Gets the choice to move if they have been moved to another room
     * Has the choice of suggest / accuse
     *
     * @param board the board that the game is running on
     */
    public void takeTurn(Board board) throws InvalidFileException {
        //Draws cards in hand
        Game.gui.cardPanel.drawCards(hand);

        // Simulates rolling dice for movement
        Die[] dice = Game.getDice();
        dice[0].setBlank();
        dice[1].setBlank();
        movement = 0;

        tilesThisTurn = new HashSet<>();
        suggested = false;
        takingTurn = true;
        hasMoved = false;

        // Auto find path if not moved into a room by a suggestion
        if (Arrays.stream(getActions(board)).noneMatch(act -> act==Actions.MOVE))
            takeAction(Actions.MOVE, board);

        while (takingTurn) {
            //Drawing Buttons & dice in Action Panel
            Game.gui.actionPanel.drawButtons(getActions(board), this, dice);

            // Wait for an action to be taken
            synchronized (this) {
                try { this.wait(); }
                catch (InterruptedException ignored) { }
            }
        }

        // Unhighlight tiles
        if (movement > 0)
            for (Tile tile : tilesThisTurn)
                if (tile.isHighlighted()) tile.toggleHighlight();

        //Update lastRoom, will be null if outside of room, used in accuse
        if (!suggested) lastRoom = null;
        else lastRoom = board.getTile(newPos).isRoom() ?
                ((RoomTile)board.getTile(newPos)).getEnum() : null;
    }

    /**
     * Have this player take the provided action
     * @param action The action to be taken
     * @param board The board being played on
     */
    public void takeAction(Actions action, Board board) {
        switch (action) {
            case MOVE:
                movement = Game.rollDice();
                oldPos = new Position(newPos);

                //Complete flood search from each entrance
                if (board.getTile(oldPos).isRoom()) {
                    for (int i=0; i<((RoomTile) board.getTile(oldPos)).getRoom().getNumberOfDoors(); i++) {
                        findPath(board, movement-1, ((RoomTile) board.getTile(oldPos)).getRoom().getDoor(i));
                    }
                }
                findPath(board, movement, oldPos);
                Game.gui.boardPanel.repaint();
                hasMoved = true;
                break;

            case SUGGEST:
                System.out.println("The player would like to make a suggestion, but this behaviour has not yet been coded.");
            case ACCUSE:
                Game.print("\n");

                // Choose a suspect and weapon to accuse/suggest
                Game.Suspects player = Game.makeDropDown(Game.Suspects.values(),
                        "Suggest Suspect", "Who do you think is a killer?");
                Game.Weapons weapon = Game.makeDropDown(Game.Weapons.values(),
                        "Suggest Weapon", "What was the weapon?");
                Game.Rooms room;
                String confirm;

                switch (action) {
                    case ACCUSE:
                        room = Game.makeDropDown(Game.Rooms.values(),
                                "Suggest Room", "Which room was the murder?");

                        confirm = Game.makeDropDown(new String[]{"Yes", "No"}, "Confirmation",
                                "Are you sure you want to Accuse: \n"+player+" with the "+weapon+" in the "+room+"?");
                        if (confirm.equals("No")) break;

                        new Accuse(room, player, weapon, this).apply();
                        takingTurn = false;
                        break;
                    case SUGGEST:
                        room = ((RoomTile)board.getTile(newPos)).getEnum();

                        confirm = Game.makeDropDown(new String[]{"Yes", "No"}, "Confirmation",
                                "Are you sure you want to Suggest: \n"+player+" with the "+weapon+" in the "+room+"?");
                        if (confirm.equals("No")) break;

                        lastRoom = room;
                        suggested = true;
                        movement = 0;
                        new Suggest(room, player, weapon, this).apply();
                }
                break;

            case END_TURN:
                takingTurn = false;
                break;
        }
        // Inform the taking turn loop that an action has been taken
        synchronized (this) { this.notifyAll(); }
    }

    /**
     * Generate an array of actions that can be taken
     *
     * @param board the board the game is being played on
     * @return the array of actions that can be taken
     */
    public Actions[] getActions(Board board) {
        List<Actions> actions = new ArrayList<>();
        boolean inRoom = board.getTile(newPos).isRoom();

        //todo, inRoom returns false each time because newPos isnt updated before this point
        //todo, moves highlighted -> this method run -> then move is carried out.
        if (inRoom && !suggested) {
            Game.Rooms currentRoom = ((RoomTile)board.getTile(newPos)).getEnum();
            if (currentRoom != lastRoom)
                actions.add(Actions.SUGGEST);
        }

        if (actions.contains(Actions.SUGGEST) && !hasMoved)
            actions.add(0, Actions.MOVE);

        actions.add(Actions.ACCUSE);
        actions.add(Actions.END_TURN);

        return actions.toArray(new Actions[]{});
    }


    /**
     * Move this player to the provided tile
     * @param newTile tile to move to
     */
    public void moveTo(Tile newTile) {
        newPos = newTile.getPos();
        Game.board.movePlayer(oldPos, newPos);
        movement = 0;

        // Unhighlight tiles
        for (Tile tile : tilesThisTurn)
            if (tile.isHighlighted()) tile.toggleHighlight();

        //Redraw the action panel
        try {
            Game.gui.actionPanel.drawButtons(getActions(Game.board), this, Game.getDice());
        } catch (InvalidFileException e) {
            e.printStackTrace();
        }

        Game.gui.boardPanel.repaint();
    }

    /**
     * Recursive method, highlight tiles that are free
     * highlights all tiles within movement.
     *
     * @param board board that players are playing on
     * @param movement int of moves
     * @param pos curr pos of tile
     */
    public void findPath(Board board, int movement, Position pos){
        if (board.getTile(pos).isRoom()) {
            for (RoomTile tile : ((RoomTile) board.getTile(pos)).getRoom().getTiles()) {
                tilesThisTurn.add(tile);
                tile.setHighlighted();
            }
            return;
        }


        //Add tile to available tiles
        tilesThisTurn.add(board.getTile(pos));
        board.getTile(pos).setHighlighted();

        if(movement > 0){
            //Checking North
            highlightTile(board, pos, new Position(pos.x, pos.y-1), movement);

            //Checking South
            highlightTile(board, pos, new Position(pos.x, pos.y+1), movement);

            //Checking East
            highlightTile(board, pos, new Position(pos.x+1, pos.y), movement);

            //Checking West
            highlightTile(board, pos, new Position(pos.x-1, pos.y), movement);
        }
    }

    /**
     * Checks whether this tile can be highlighted.
     *
     * @param board the board being played on
     * @param current the current position
     * @param next the position being moved to
     * @param movement the amount of movement left
     */
    public void highlightTile(Board board, Position current, Position next, int movement) {
        if (board.isValidMove(current, next)) {
            findPath(board, movement - 1, next);
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
    public ArrayList<Card<?>> addMatches(Card<Game.Rooms> room, Card<Game.Suspects> suspect, Card<Game.Weapons> weapon) {
        ArrayList<Card<?>> matches = new ArrayList<>();
        for (Card<?> card : hand)
            if (card.equals(room) || card.equals(suspect) || card.equals(weapon))
                matches.add(card);
        return matches;
    }

    @Override
    public String toString() {
        return suspect+"";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return hasLost == player.hasLost &&
                suspect == player.suspect &&
                Objects.equals(hand, player.hand) &&
                Objects.equals(newPos, player.newPos) &&
                Objects.equals(oldPos, player.oldPos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suspect, hand, hasLost, newPos, oldPos);
    }


    // GETTERS AND SETTERS

    /**
     * Get this players hand
     *
     * @return arraylist of cards in this players hand
     */
    public ArrayList<Card<?>> getHand(){ return hand; }

    /**
     * Get the suspect this player is playing as
     *
     * @return enum from Suspects in Game class
     */
    public Game.Suspects getSuspect() {
        return suspect;
    }

    /**
     * Get the name of this player
     * @return name of the person playing this suspect
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of this player
     * @param name name of the person playing this suspect
     */
    public void setName(String name) {
        this.name = name;
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
}
