/**
 * Action Class
 * -Main class for 3 action subclasses to map to
 */
public interface Action {
    /**
     * Apply this action
     * @return has this action applied correctly
     * @throws InvalidMoveException thrown if an invalid move is made
     */
    boolean apply() throws InvalidMoveException;
}
