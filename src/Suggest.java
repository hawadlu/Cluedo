import java.util.ArrayList;
import java.util.List;

public class Suggest implements Action {
    Card room;
    List<Player> allPlayers;
    Card weapon;
    Player player;
    Card accused;

    public Suggest(Game.Rooms room, Game.Players accused, Game.Weapons weapon, Player player, List<Player> allPlayers) {
        this.room = new Card(room);
        this.weapon = new Card(weapon);
        this.accused = new Card(accused);
        this.allPlayers = allPlayers;
        this.player = player;
    }

    @Override
    public boolean apply() throws InvalidMoveException {
        //Move the accused players to this room
        //todo implement this part.

        allPlayers.remove(player);

        //Go through all of the players hand looking for a match
        for (Player other: allPlayers) {
            ArrayList<Card> cardOptions = other.addMatches(room, accused, weapon);

            if (!cardOptions.isEmpty()) {
                System.out.println("Found a match");

                Card toShow = (Card) Game.chooseFromArray(cardOptions.toArray(), "Choose a card to show");
                System.out.println("Show card: " + toShow);

                return true;
            }
        }

        //todo move the accused into the correct room

        return false;
    }
}
