public class Tile {
    Room room=null;
    String roomName=null;
    Player player=null;
    Boolean door=false;
    String doorRoom=null;

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
    public void setRoomName(String roomName){
        this.roomName=roomName;
    }

    /**
     * Set this tile to be a door
     * @param r the room to add this door to
     */
    public void setDoor(Room r){
        door=true;
        r.addDoor(this);
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
        }

        return "• ";

    }
}
