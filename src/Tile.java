public class Tile {
    Room room=null;
    Game.Rooms roomName=null;
    Player player=null;
    Boolean door=false;
    Boolean toggleDoor=false;
    Game.Rooms doorRoom=null;
    int doorNumber=0;
    Game.Rooms enumDoor=null;
    Position doorPos=null;

    /**
     * Set this tile to a room
     * @param r the room to set this tile as
     */
    public void setRoom(Room r){
        room=r;
    }

    /**
     * Set the name of this room
     * @param roomName new name for this room
     */
    public void setRoomName(Game.Rooms roomName){
        this.roomName=roomName;
    }

    /**
     * Set this tile to be a door
     * @param r the room to add this door to
     */
    public void setDoor(Room r, Position pos){
        door=true;
        doorPos=pos;
        r.addDoor(this);
    }

    /**
     * Gets the room enum
     * @return room enum
     */
    public Game.Rooms getEnum(){
        return roomName;
    }

    /**
     * Gets the room the player's in
     */
    public Room getRoom(){
        return room;
    }

    /**
     * Gets this door position, used for leaving rooms
     * @return door position
     */
    public Position getDoorPos(){
        return doorPos;
    }

    /**toggles the display of the door
     *
    /**
     * Toggles the display of the door
     */
    public void toggleDoor(){
        if(!toggleDoor){
            toggleDoor=true;
        }else{
            toggleDoor=false;
        }
    }

    /**
     * Sets room door number
     * @param i is the new door number
     */
    public void setDoorNumber(int i){
        doorNumber=i;
    }

    /**
     * Is this tile a room?
     * @return boolean response
     */
    public boolean isRoom(){
        return room != null;
    }

    /**
     * Is this tile a door?
     * @return boolean response
     */
    public boolean isDoor(){
        return door;
    }

    /**
     * Set the player on this tile
     * @param p the player to give this tile
     */
    public void setPlayer(Player p){
        player = p;
    }

    /**
     * Puts a player on tile
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
     * @param p the player to be removed from the tile
     */
    public void removePlayer(Player p){
        player = null;
        if(room!=null) {
            room.removePlayer(p);
        }
    }

    @Override
    public String toString() {
        if(player!=null){
            return player.toString();
        }else if(room!=null){
            return "  ";
        }else if(toggleDoor){
            return doorNumber+" ";
        }

        return "• ";

    }
}
