public class HallwayTile implements Tile{
    private Player player=null;
    private Position pos=null;


    public HallwayTile(Position pos){
        this.pos=pos;
    }


    /**
     * Puts a player on tile
     *
     * @param p the player to be added to the tile
     */
    public void addPlayer(Player p){
            player = p;
    }

    /**
     * Removes player from tile
     */
    public void removePlayer(){
        player = null;
    }

    @Override
    public String toString() {
        if(player!=null){
            return player.toString();
        }
        return "• ";

    }

    // BOOLEAN CHECKERS

    /**
     * Check if this tile has a player
     *
     * @return boolean response
     */
    public boolean hasPlayer() {
        return player != null;
    }


    /*
    GETTERS AND SETTERS
     */

    /**
     * Gets this door position, used for leaving rooms
     *
     * @return door position
     */
    public Position getPos(){
        return pos;
    }

    /**
     * Get the player on this tile
     *
     * @return the player on this tile, null if no player
     */
    public Player getPlayer() {
        return player;
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
