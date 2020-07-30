/**
 * Action Class
 * -Main class for 3 action subclasses to map to
 */
public interface Action {
    public boolean apply() throws InvalidMoveException;
}
