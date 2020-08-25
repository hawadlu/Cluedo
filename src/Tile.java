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
    * @return boolean response
    */
    public boolean isRoom() {
        return this instanceof RoomTile;
    }

    /**
     * Is this tile a room
     * @return boolean response
    */
    public boolean isNotNullRoom() {
        return this instanceof RoomTile && ((RoomTile) this).getEnum() != null;
    }

    /**
     * Is this tile the same type of room as the provided tile
     * @return boolean response
     */
    public boolean isSameRoom(Tile otherRoom) {
        return this instanceof RoomTile && otherRoom instanceof RoomTile &&
                ((RoomTile) this).getEnum() == ((RoomTile) otherRoom).getEnum();
    }

    /**
     * Is this a null tile
     * @return boolean response
     */
    public boolean isNull() {
        return this instanceof RoomTile && ((RoomTile) this).getEnum() == null;
    }

    /**
     * Is this tile a hallway
     * @return boolean response
     */
    public boolean isHallway() {
        return this instanceof HallwayTile;
    }

    /**
     * Is this tile a door
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
     * set tile as highlighted
     */
    public void setHighlighted(){ highlighted = true; }
}
