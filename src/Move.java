import java.util.HashSet;
import java.util.Set;

/**
 * Checks where are valid moves to make?
 */
public class Move implements Action {
    private final Board board;
    private final Player player;
    private final String[] actions;


    Move(Board board, Player player, String[] actions) {
        this.board = board;
        this.player = player;
        this.actions = actions;
    }

    /**
     * Check and apply a move
     *
     * @throws InvalidMoveException if the move is invalid
     */
    @Override
    public void apply() throws InvalidMoveException {
        // Will throw error if not a valid move
        validateMoves();

        // Change the players position based on actions
        Position pos = player.getNewPos();
        for (String action: actions) {
            switch (action) {
                case "l": pos.x -= 1; break; //move left
                case "r": pos.x += 1; break; //move right
                case "u": pos.y -= 1; break; //move up
                case "d": pos.y += 1; break; //move down
                default: return;
            }
        }

        //Indicate that the move worked
    }

    /**
     * Check if the list of proposed moves is valid;
     */
    private void validateMoves() throws InvalidMoveException {
        Position next = new Position(player.getNewPos());
        Position prev = new Position(player.getNewPos());
        Set<Tile> tilesThisMove = new HashSet<>();

        for (String action: actions) {
            //Check if the move is recognised
            switch (action) {
                case "l": next.x -= 1; break; //move left
                case "r": next.x += 1; break; //move right
                case "u": next.y -= 1; break; //move up
                case "d": next.y += 1; break; //move down
                default: throw new InvalidMoveException("Unrecognised move type");
            }

            if (!board.isValidMove(prev, next)) throw new InvalidMoveException("Cannot move to that tile");
            prev = new Position(next);

            //Check if the player has already moved to this square in this turn.
            Tile nextTile = board.getTile(next);
            if (player.getTilesThisTurn().contains(nextTile) || tilesThisMove.contains(nextTile)) {
                throw new InvalidMoveException("Cannot move on to the same place twice in one turn");
            } else {
                tilesThisMove.add(nextTile);
            }
        }
        player.getTilesThisTurn().addAll(tilesThisMove);
        //The move was valid if code gets here
    }
}
