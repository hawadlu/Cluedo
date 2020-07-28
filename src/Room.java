import java.util.ArrayList;

public class Room {
    String name;
    ArrayList<Tile> tiles;

    public Room(ArrayList<Tile> tiles, String name){
        this.name=name;
        this.tiles=tiles;
        for(Tile t: tiles){
            t.setRoom(this.name);
        }
    }
}
