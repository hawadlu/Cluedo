public class RoomTile extends Tile {
    private Room room=null;
    private Game.Rooms roomName=null;

    RoomTile(Position pos) {
        super(pos);
    }

    /**
     * Puts a player on tile
     *
     * @param p the player to be added to the tile
     */
    public void addPlayer(Player p){
        room.addPlayer(p);
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


    //GETTERS AND SETTERS

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
