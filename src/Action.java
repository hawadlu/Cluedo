/**
 * Action Class.
 * -Main class for 3 action subclasses to map to
 */
public interface Action {

    /**
     * Apply this action.
     *
     * @throws InvalidMoveException thrown if an invalid move is made
     */
    void apply() throws InvalidMoveException;
}
