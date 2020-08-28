public class HallwayTile extends Tile {

    public HallwayTile(Position pos){
        super(pos);
    }

    /**
     * Puts a player on tile.
     *
     * @param p the player to be added to the tile.
     */
    public void addPlayer(Player p){
            player = p;
    }

    @Override
    public String toString() {
        if(player!=null){
            return player.toString();
        }
        return "• ";
    }


    // GETTERS AND SETTERS
    
    /**
     * Set the player on this tile.
     *
     * @param p the player to give this tile
     */
    public void setPlayer(Player p){
        player = p;
    }
}
