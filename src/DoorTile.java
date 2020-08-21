public class DoorTile extends Tile{
    private final Room room;
    private Boolean toggleDoor=false;
    private int doorNumber=0;

    public DoorTile(Room room, Position pos){
        super(pos);

        this.room = room;
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

    @Override
    public String toString() {
        if(player!=null){
            return player.toString();
        }else if(toggleDoor){
            return (doorNumber+1)+" ";
        }
        return "• ";
    }


    //GETTERS AND SETTERS

    /**
     * Gets the room
     *
     * @return the room this door is associated with
     */
    public Room getRoom(){
        return room;
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
