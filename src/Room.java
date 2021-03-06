import java.util.ArrayList;

public class Room {
    private final ArrayList<RoomTile> roomTiles;
    private final ArrayList<RoomTile> playerSeats = new ArrayList<>();
    private final ArrayList<RoomTile> weaponSpots = new ArrayList<>();
    private final ArrayList<DoorTile> doors = new ArrayList<>();

    public Room(ArrayList<RoomTile> tiles, Game.Rooms name){
        this.roomTiles=tiles;
        for(RoomTile t: tiles){
            t.setRoom(this);
            t.setRoomName(name);
        }
    }

    /**
     * Allocates room tile spots for players in 3x2 area.
     *
     * @param b board game is being played on
     * @param x leftmost position of 3x2 area
     * @param y topmost position of 3x2 area
     */
    public void setSeats(Board b, int x, int y){

        for(int i=0; i<2; i++){
            for(int j=0; j<3; j++){
                playerSeats.add((RoomTile) b.getTile(new Position(x+j, y+i)));
            }
        }
    }

    /**
     * Allocates room tile spots for weapons in 2x3 area.
     *
     * @param b board game is being played on
     * @param x leftmost position of 3x2 area
     * @param y topmost position of 3x2 area
     */
    public void setWeapons(Board b, int x, int y){

        for(int i=0; i<3; i++){
            for(int j=0; j<2; j++){
                weaponSpots.add((RoomTile) b.getTile(new Position(x+j, y+i)));
            }
        }
    }

    /**
     * Add a door to this room.
     *
     * @param t the tile which is the door to add to this room
     */
    public void addDoor(DoorTile t){
        doors.add(t);
        t.setDoorNumber(doors.indexOf(t));
    }

    /**
     * The number of doors into this room.
     *
     * @return number of doors into this room
     */
    public int getNumberOfDoors(){
        return doors.size();
    }

    /**
     * Get a door from this room.
     *
     * @param n the index of the door to be got
     * @return the door at the provided index
     */
    public Position getDoor(int n){
        return doors.get(n).getPos();
    }

    /**
     * Add a player into this room.
     *
     * @param p the player to be added to this room
     */
    public void addPlayer(Player p){
        for(RoomTile t: playerSeats){
            if(t.getPlayer()==null){
                t.setPlayer(p);
                p.setNewPos(t.getPos());
                break;
            }
        }
    }

    /**
     * Add a weapon into this room.
     *
     * @param w the weapon to be added to this room
     */
    public void addWeapon(Weapon w){
        for(RoomTile t: weaponSpots){
            if(t.getPlayer()==null){
                t.setWeapon(w);
                w.setNewPos(t.getPos());
                break;
            }
        }
    }

    /**
     * Get the tiles of this room.
     *
     * @return arraylist of the tiles in this room
     */
    public ArrayList<RoomTile> getTiles() {
        return roomTiles;
    }
}
