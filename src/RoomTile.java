public class RoomTile implements Tile {
    private Room room=null;
    private Game.Rooms roomName=null;
    private Player player=null;
    private Position pos=null;


    public RoomTile(Position pos){
        this.pos=pos;
    }

    /**
     * Puts a player on tile
     *
     * @param p the player to be added to the tile
     */
    public void addPlayer(Player p){
        if(room==null) {
            player = p;
        }else{
            room.addPlayer(p);
        }
    }

    /**
     * Removes player from tile
     */
    public void removePlayer(){
        player = null;
    }

    @Override
    public String toString() {
        if(player!=null){
            return player.toString();
        }else if(room!=null){
            return "  ";
        }

        return "• ";

    }

    // BOOLEAN CHECKERS

    /**
     * Check if this tile has a player
     *
     * @return boolean response
     */
    public boolean hasPlayer() {
        return player != null;
    }


    /*
    GETTERS AND SETTERS
     */

    /**
     * Gets the room enum
     *
     * @return room enum
     */
    public Game.Rooms getEnum(){
        return roomName;
    }

    /**
     * Get the room that this tile is a part of
     *
     * @return the room associated with this tile, null if no room
     */
    public Room getRoom(){
        return room;
    }

    /**
     * Gets this door position, used for leaving rooms
     *
     * @return door position
     */
    public Position getPos(){
        return pos;
    }

    /**
     * Get this tile's room name as a enum from Game
     *
     * @return room enum - Game.Rooms, or null if no room
     */
    public Game.Rooms getRoomName() {
        return roomName;
    }

    /**
     * Get the player on this tile
     *
     * @return the player on this tile, null if no player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Set this tile to a room
     *
     * @param r the room to set this tile as
     */
    public void setRoom(Room r){
        room=r;
    }

    /**
     * Set the name of this room
     *
     * @param roomName new name for this room
     */
    public void setRoomName(Game.Rooms roomName){
        this.roomName=roomName;
    }

    /**
     * Set the player on this tile
     *
     * @param p the player to give this tile
     */
    public void setPlayer(Player p){
        player = p;
    }
}
