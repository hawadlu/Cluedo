/**
 * Accuse Action
 *  -On a players 2nd action after move they can choose to accuse
 *  -Check if user has correct guess
 *  -End game if correct, current player loses if incorrect
 */
public class Accuse implements Action {
    RoomCard room;
    PlayerCard suspect;
    WeaponCard weapon;
    Game game;
    Player player;

    /**
     * Setup the accusation
     */
    Accuse(RoomCard room, PlayerCard suspect, WeaponCard weapon, Game game, Player player) {
        this.room = room;
        this.suspect = suspect;
        this.weapon = weapon;
        this.game = game;
        this.player = player;
    }

    @Override
    public boolean apply() {
        //Check to see if the accused matches
        if (!game.accuseList.contains(room)) player.hasLost = true;
        else if (!game.accuseList.contains(weapon)) player.hasLost = true;
        else if (!game.accuseList.contains(suspect)) player.hasLost = true;

        //todo end the game for a a win
        return false;
    }
}
