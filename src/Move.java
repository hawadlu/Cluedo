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
     * @return true if the move is valid, false otherwise
     * @throws InvalidMoveException if the move is invalid
     */
    @Override
    public boolean apply() throws InvalidMoveException {
        // Will throw error if not a valid move
        validateMoves();

        // Change the players position based on actions
        for (String action: actions) {
            switch (action) {
                case "l": player.newPos.x -= 1; break; //move left
                case "r": player.newPos.x += 1; break; //move right
                case "u": player.newPos.y -= 1; break; //move up
                case "d": player.newPos.y += 1; break; //move down
                default: return false;
            }
        }

        //Indicate that the move worked
        return true;
    }

    /**
     * Check if the list of proposed moves is valid;
     */
    private void validateMoves() throws InvalidMoveException {
        Position next = new Position(player.newPos);
        Position prev = new Position(player.newPos);
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

            if (!board.isValidMove(prev, next)) throw new InvalidMoveException("Cannot move to that tile");;
            prev = new Position(next);

            //Check if the player has already moved to this square in this turn.
            Tile nextTile = board.getTile(next.x, next.y);
            if (player.tilesThisTurn.contains(nextTile) || tilesThisMove.contains(nextTile)) {
                throw new InvalidMoveException("Cannot move on to the same place twice in one turn");
            } else {
                tilesThisMove.add(nextTile);
            }
        }
        player.tilesThisTurn.addAll(tilesThisMove);
        //The move was valid if code gets here
    }
}
