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
     * @throws InvalidActionException
     */
    @Override
    public boolean apply() throws InvalidActionException {
        //Check to see if the player is trying to move more than their dice roll
        if (actions.length != diceRoll) throw new InvalidActionException("Invalid number of moves");

        //Validate the moves
        if (!validateMoves()) return false;

        for (String action: actions) {
            if (action.equals("l")) player.newPos.x -= 1; //move left
            else if (action.equals("r")) player.newPos.x += 1; //move right
            else if (action.equals("u")) player.newPos.y -= 1; //move up
            else if (action.equals("d")) player.newPos.y += 1; //move down
            else return false;
        }

        //Indicate if the move worked
        return true; //todo this will not always be true
    }

    /**
     * Check if the list of proposed moves is valid;
     * @return
     */
    private boolean validateMoves() {
        //todo might need to change this?
        Position current = player.newPos;
        Position prev = player.newPos;

        for (String action: actions) {
            if (action.equals("l")){
                //move left
                current.x -= 1;
                if (!board.isValidMove(current, prev)) return false;
                prev = current;
            } else if (action.equals("r")) {
                //move right
                current.x += 1;
                if (!board.isValidMove(current, prev)) return false;
            } else if (action.equals("u")) {
                //move up
                current.y -= 1;
                if (!board.isValidMove(current, prev)) return false;
                prev = current;
            } else if (action.equals("d")) {
                //move down
                current.y += 1;
                if (!board.isValidMove(current, prev)) return false;
                prev = current;
            }
            else return false;
        }

        //The move was valid.
        return true;
    }
}
