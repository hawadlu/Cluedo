public abstract class Tile {
    Player player=null;
    Position pos=null;
    boolean highlighted=false;

    /**
     * Check if this tile has a player
     *
     * @return boolean response
     */
    abstract boolean hasPlayer();

    /**
     * Gets this door position, used for leaving rooms
     *
     * @return door position
     */
    abstract Position getPos();

    /**
     * Get the player on this tile
     *
     * @return the player on this tile, null if no player
     */
    abstract Player getPlayer();

    /**
     * Set the player on this tile
     *
     * @param p the player to give this tile
     */
    abstract void addPlayer(Player p);

    /**
     * Removes player from tile
     */
    abstract void removePlayer();

    /**
     * Is this tile a room
     * @return boolean response
     */
    public boolean isRoom() {
        return this instanceof RoomTile;
    }

    /**
     * Is this tile a room
     * @return boolean response
     */
    public boolean isDoor() {
        return this instanceof DoorTile;
    }

    /**
     * Returns whether this tile is highlighted
     *
     * @return boolean if tile is highlighted
     */
     public boolean isHighlighted(){ return highlighted; }

    /**
     * toggle highlighted
     */
    public void setHighlighted(){ highlighted = !highlighted; }
}
