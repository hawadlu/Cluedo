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
    Player player;

    Accuse(Game.Rooms room, Game.Players suspect, Game.Weapons weapon, Player player) {
        this.room = room;
        this.suspect = suspect;
        this.weapon = weapon;
        this.player = player;
    }

    @Override
    public boolean apply() {
        //Check to see if the accused matches
        if (Game.accuseRoom.equals(room) && Game.accusePlayer.equals(suspect) && Game.accuseWeapon.equals(weapon)) {
            Game.gameOver = true;
            System.out.println("You have won!");
        } else {
            player.hasLost = true;
            System.out.println("Your accusation is incorrect. You have lost.");
        }
        System.out.println("Press 'enter' to continue.");
        Game.input.nextLine();
        return true;
    }
}
