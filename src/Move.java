/**
 * Checks where are valid moves to make?
 */
public class Move implements Action {
    Move(Player player, String[] actions, int diceRoll) throws InvalidActionException {
        //Check to see if the player is trying to move more than their dice roll
        if (actions.length != diceRoll) throw new InvalidActionException("Invalid number of moves");
        for (String action: actions) {
            if (action.equals("l")) performAction(player, "l");
            else if (action.equals("r")) performAction(player, "r");
            else if (action.equals("u")) performAction(player,"u");
            else if (action.equals("d")) performAction(player, "d");
            else throw new InvalidActionException("That is an invalid action");
        }
    }

    @Override
    public void performAction(Player player, String action) {

    }
}
