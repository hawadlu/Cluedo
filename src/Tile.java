public interface Tile {
    Player player=null;
    Position pos=null;

    /**
     * Check if this tile has a player
     *
     * @return boolean response
     */
    boolean hasPlayer();

    /**
     * Gets this door position, used for leaving rooms
     *
     * @return door position
     */
    Position getPos();

    /**
     * Get the player on this tile
     *
     * @return the player on this tile, null if no player
     */
    Player getPlayer();

    /**
     * Set the player on this tile
     *
     * @param p the player to give this tile
     */
    void addPlayer(Player p);

    /**
     * Removes player from tile
     */
    void removePlayer();

    /**
     * Is this tile a room
     * @return boolean response
     */
    default boolean isRoom() {
        return this instanceof RoomTile;
    }

    /**
     * Is this tile a room
     * @return boolean response
     */
    default boolean isDoor() {
        return this instanceof DoorTile;
    }
}
