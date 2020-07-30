public class Tile {
    Room room=null;
    String roomName=null;
    Player player=null;
    Boolean door=false;
    String doorRoom=null;

    public void setRoom(Room r){
        room=r;
    }

    public void setRoomName(String roomName){
        this.roomName=roomName;
    }
    public void setDoor(boolean bool, Room r){
        door=bool;
        r.addDoor(this);
    }

    public boolean isRoom(){
        if(room == null){
            return false;
        }
        return true;
    }

    public boolean isDoor(){
        return door;
    }

    public void setPlayer(Player p){
        player = p;
    }

    /**puts player on tile
     *
     * @param p
     */
    public void addPlayer(Player p){
        if(room==null) {
            player = p;
        }else{
            room.addPlayer(p);
        }
    }

    /**removes player form tile
     *
     * @param p
     */
    public void removePlayer(Player p){
        player = null;
        if(room!=null) {
            room.removePlayer(p);
        }
    }

    //todo implement this properly.
    @Override
    public String toString() {
        if(player!=null){
           return " " + player.toString();
        }else if(room!=null){
            return room.toString();
        }

        return " . ";

    }
}
