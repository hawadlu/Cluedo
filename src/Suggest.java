import java.util.ArrayList;
import java.util.List;

public class Suggest implements Action {
    Game.Rooms room;
    List<Player> allPlayers;
    Game.Weapons weapon;
    Player player;
    Game.Players accused;

    public Suggest(Game.Rooms room, Game.Players accused, Game.Weapons weapon, Player player, List<Player> allPlayers) {
        this.room = room;
        this.weapon = weapon;
        this.accused = accused;
        this.allPlayers = allPlayers;
        this.player = player;
    }

    @Override
    public boolean apply() throws InvalidActionException {
        //Go through all of the players hand looking for a match

        for (Player other: allPlayers) {
            ArrayList<Card> cardOptions = other.addMatches(room, accused, weapon);

            if (!cardOptions.isEmpty()) {
                System.out.println("Found a match");
            }
        }

        //todo move the accused into the correct room

        return false;
    }
}
