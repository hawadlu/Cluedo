/**
 * Accuse Action
 *  -On a players 2nd action after move they can choose to accuse
 *  -Check if user has correct guess
 *  -End game if correct, current player loses if incorrect
 */
public class Accuse implements Action {
    Game.Rooms room;
    Game.Players suspect;
    Game.Weapons weapon;
    Game game;
    Player player;

    /**
     * Setup the accusation
     */
    Accuse(Game.Rooms room, Game.Players suspect, Game.Weapons weapon, Player player) {
        this.room = room;
        this.suspect = suspect;
        this.weapon = weapon;
        this.player = player;
    }

    @Override
    public boolean apply() {
        //Check to see if the accused matches
        if (!game.accuseRoom.equals(room)) player.hasLost = true;
        else if (!game.accusePlayer.equals(suspect)) player.hasLost = true;
        else if (!game.accuseWeapon.equals(weapon)) player.hasLost = true;

        //todo end the game for a a win
        return false;
    }
}
