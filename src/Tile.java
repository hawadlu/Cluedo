public class Tile {
    String room=null;
    Player player=null;
    Boolean door=false;
    String doorRoom=null;

    public void setRoom(String roomName){
        room=roomName;
    }
    public void setDoor(boolean bool){
        door=bool;
    }
}
