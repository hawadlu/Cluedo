public class DoorTile implements Tile{
    private final Room roomName;
    private Player player=null;
    private Boolean toggleDoor=false;
    private int doorNumber=0;
    private final Position pos;

    public DoorTile(Room room, Position pos){
        roomName = room;
        this.pos=pos;
        room.addDoor(this);
    }

    /**
     * Toggles the display of the door
     */
    public void toggleDoor(){
        toggleDoor= !toggleDoor;
    }

    /**
     * Puts a player on tile
     *
     * @param p the player to be added to the tile
     */
    public void addPlayer(Player p){
            player = p;
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
        }else if(toggleDoor){
            return (doorNumber+1)+" ";
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

    //GETTERS AND SETTERS

    /**
     * Gets the room
     *
     * @return the room this door is associated with
     */
    public Room getRoom(){
        return roomName;
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
     * Get the player on this tile
     *
     * @return the player on this tile, null if no player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets room door number
     *
     * @param i is the new door number
     */
    public void setDoorNumber(int i){
        doorNumber=i;
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
