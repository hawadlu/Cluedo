import java.util.ArrayList;
import java.util.HashSet;

public class Room {
    Game.Rooms name;
    ArrayList<Tile> tiles;
    HashSet<Player> players = new HashSet<>();
    ArrayList<Tile> playerSeats = new ArrayList<>();
    ArrayList<Tile> doors = new ArrayList<>();

    public Room(ArrayList<Tile> tiles, Game.Rooms name){
        this.name=name;
        this.tiles=tiles;
        for(Tile t: tiles){
            t.setRoom(this);
            t.setRoomName(this.name);
        }
    }

    /**
     * Allocates room tile spots for players in 3x2 area
     * @param b board game is being played on
     * @param x leftmost position of 3x2 area
     * @param y topmost position of 3x2 area
     */
    public void setSeats(Board b, int x, int y){
        playerSeats.add(b.board[y][x]);
        playerSeats.add(b.board[y][x+1]);
        playerSeats.add(b.board[y][x+2]);
        playerSeats.add(b.board[y+1][x]);
        playerSeats.add(b.board[y+1][x+1]);
        playerSeats.add(b.board[y+1][x+2]);
    }

    /**
     * Add a door to this room
     * @param t the tile which is the door to add to this room
     */
    public void addDoor(Tile t){
        doors.add(t);
        t.setDoorNumber(doors.indexOf(t));
    }

    /**toggles display of the rooms doors
     *
     */
    public void toggleDoorNumbers(){
        for(Tile t: doors){
            t.toggleDoor();
        }
    }


    /**
     * The number of doors into this room
     * @return number of doors into this room
     */
    public int getNumberOfDoors(){
        return doors.size();
    }

    /**
     * Get a door from this room
     * @param n the index of the door to be got
     * @return the door at the provided index
     */
    public Tile getDoor(int n){
        return doors.get(n);
    }


    /**
     * Checks if a player is in this room
     * @param p is this player in this room
     * @return boolean response
     */
    public boolean inRoom(Player p){
        return players.contains(p);
    }

    /**
     * Add a player into this room
     * @param p the player to be added to this room
     */
    public void addPlayer(Player p){
        for(Tile t: playerSeats){
            if(t.player==null){
                t.setPlayer(p);
                System.out.println(p.name + " has entered the " + this.name); //primarily for debugging
                break;
            }
        }
        players.add(p);
    }

    /**
     * Remove a player from this room
     * @param p the player to be removed
     */
    public void removePlayer(Player p){
        players.remove(p);
    }
}
