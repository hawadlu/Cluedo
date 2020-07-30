import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Suggest implements Action {
    private final Card<Game.Rooms> room;
    private final List<Player> allPlayers;
    private final Card<Game.Weapons> weapon;
    private final Player player;
    private final Card<Game.Players> accused;

    Suggest(Game.Rooms room, Game.Players accused, Game.Weapons weapon, Player player, List<Player> allPlayers) {
        this.room = new Card<>(room);
        this.weapon = new Card<>(weapon);
        this.accused = new Card<>(accused);
        this.allPlayers = allPlayers;
        this.player = player;
    }

    @Override
    public boolean apply() throws InvalidMoveException {
        //Move the accused players to this room
        for (Player playerToMove: allPlayers) {
            if (playerToMove.name.equals(accused.name)) {
                //Get the room
                Board.rooms.get(room.name).addPlayer(playerToMove);
                break;
            }
        }

        allPlayers.remove(player);

        //Go through all of the players hand looking for a match
        for (Player other: allPlayers) {
            ArrayList<Card<?>> cardOptions = other.addMatches(room, accused, weapon);

            if (!cardOptions.isEmpty()) {
                System.out.println("Found a match");

                Card<?> toShow = Game.chooseFromArray(cardOptions.toArray(new Card<?>[]{}), "Choose a card to show");
                System.out.println("Show card: " + toShow);

                return true;
            }
        }
        return false;
    }
}
