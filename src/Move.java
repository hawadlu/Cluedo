/**
 * Checks where are valid moves to make?
 */
public class Move implements Action {
    Board board;
    Player player;
    String[] actions;
    int diceRoll;


    Move(Board board, Player player, String[] actions, int diceRoll) {
        this.board = board;
        this.player = player;
        this.actions = actions;
        this.diceRoll = diceRoll;
    }

    /**
     * Check and apply a move
     * @return true if the move is valid, false otherwise
     * @throws InvalidMoveException
     */
    @Override
    public boolean apply() throws InvalidMoveException {
        //Check to see if the player is trying to move more than their dice roll
        if (actions.length > diceRoll) throw new InvalidMoveException("Move too long");

        // Will throw error if not a valid move
        validateMoves();

        for (String action: actions) {
            switch (action) {
                case "l": player.newPos.x -= 1; break; //move left
                case "r": player.newPos.x += 1; break; //move right
                case "u": player.newPos.y -= 1; break; //move up
                case "d": player.newPos.y += 1; break; //move down
                default: return false;
            }
        }

        //Indicate if the move worked
        return true;
    }

    /**
     * Check if the list of proposed moves is valid;
     */
    private void validateMoves() throws InvalidMoveException {
        Position next = new Position(player.newPos);
        Position prev = new Position(player.newPos);

        for (String action: actions) {
            //Check if the move is recognised
            switch (action) {
                case "l": next.x -= 1; break; //move left
                case "r": next.x += 1; break; //move right
                case "u": next.y -= 1; break; //move up
                case "d": next.y += 1; break; //move down
                default: throw new InvalidMoveException("Unrecognised move type");
            }

            //Check if the player has already moved to this square in this turn.
            if (player.tilesThisTurn.contains(next)) {
                throw new InvalidMoveException("Cannot move to the same twice square in one turn");
            } else {
                player.tilesThisTurn.add(next);
            }

            if (!board.isValidMove(prev, next)) throw new InvalidMoveException("Cannot move to that tile");;
            prev = new Position(next);
        }
        //The move was valid if code gets here
    }
}
