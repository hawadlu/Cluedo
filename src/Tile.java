abstract class Tile {
    protected Player player=null;
    protected final Position pos;
    protected boolean highlighted=false;

    Tile(Position pos) {
        this.pos = pos;
    }

    // Abstract Methods

    /**
     * Set the player on this tile
     *
     * @param p the player to give this tile
     */
    abstract void addPlayer(Player p);


    // Common Methods

    /**
     * Check if this tile has a player
     *
     * @return boolean response
     */
    public boolean hasPlayer() {
        return player != null;
    }

    /**
     * Gets this door position, used for leaving rooms
     *
     * @return door position
     */
    public Position getPos() {
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
     * Removes player from tile
     */
    public void removePlayer() {
        player = null;
    }

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
    public void toggleHighlight(){ highlighted = !highlighted; }

    /**
     * toggle highlighted
     */
    public void setHighlighted(){ highlighted = true; }
}
