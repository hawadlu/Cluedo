import java.util.ArrayList;
import java.util.HashSet;

public class Room {
    String name;
    ArrayList<Tile> tiles;
    HashSet<Player> players = new HashSet<>();
    ArrayList<Tile> playerSeats = new ArrayList<>();
    ArrayList<Tile> doors = new ArrayList<>();

    public Room(ArrayList<Tile> tiles, String name){
        this.name=name;
        this.tiles=tiles;
        for(Tile t: tiles){
            t.setRoom(this);
            t.setRoomName(this.name);
        }
    }

    /**allocates room tile spots for players
     *
     * @param b
     * @param x
     * @param y
     */
    public void setSeats(Board b, int x, int y){
        playerSeats.add(b.board[y][x]);
        playerSeats.add(b.board[y][x+1]);
        playerSeats.add(b.board[y][x+2]);
        playerSeats.add(b.board[y+1][x]);
        playerSeats.add(b.board[y+1][x+1]);
        playerSeats.add(b.board[y+1][x+2]);
    }

    public void addDoor(Tile t){
        doors.add(t);
    }

    public int getNumberOfDoors(){
        return doors.size();
    }

    public Tile getDoor(int n){
        return doors.get(n);
    }


    /**checks if a player is in this room
     *
     * @param p
     * @return
     */
    public boolean inRoom(Player p){
        return players.contains(p);
    }

    public void addPlayer(Player p){
        //'take a seat'
        for(Tile t: playerSeats){
            if(t.player==null){
                t.setPlayer(p);
                break;
            }
        }
        players.add(p);
    }

    public void removePlayer(Player p){
        players.remove(p);
    }

}
