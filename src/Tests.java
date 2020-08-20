import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Tests {

    // ================================================
    // Valid Tests
    // ================================================

    @Test
    public void testMain() {
        System.out.println("main");
        Game.main(null);
    }

    @Test
    public void testSuggest() {
        List<Player> players = createPlayers(6);

        dealCards(players, 6);

        System.out.println("Dealt cards");

        Player testPlayer = players.remove(0);

        //Test the suggest mechanism with some random cards
        Suggest suggest = new Suggest(Game.Rooms.BILLIARD_ROOM, Game.Players.PEACOCK, Game.Weapons.ROPE, testPlayer);
        suggest.apply();

    }

    /*
      THESE METHODS ARE HERE SO THAT THEY CAN BE TESTED WITHOUT USER INPUTS VIA THE CONSOLE
      BECAUSE THESE DON'T WORK WITH JUNIT.
     */

    /**
     * Create the players that will be playing the game
     *
     * @param numPlayers the number of players that will be playing the game
     * @return an arraylist of players
     */
    public ArrayList<Player> createPlayers(int numPlayers) {
        ArrayList<Player> players = new ArrayList<>();
        // Make a list of available players so that chosen players can be removed from the list
        ArrayList<Game.Players> availablePlayers = new ArrayList<>(Arrays.asList(Game.Players.values()));

        // Make player objects for each player that wants to play
        for (int i = 0; i < numPlayers; i++) {
            Game.Players player = availablePlayers.remove(0);
            availablePlayers.remove(player);

            // Catch choosing a player that doesnt have a starting position set
            try {
                Position startPos = getStartingPosition(player);

                players.add(new Player(player, startPos));
            }
            catch (Exception | InvalidFileException e) {
                System.out.println(e.toString());
                i--;
            }
        }
        return players;
    }

    /**
     * Gets the default starting position of a player
     *
     * @param player the player enum to find the position of
     * @return a position object, containing the starting position coordinates
     */
    public Position getStartingPosition(Game.Players player)  {
        switch (player) {
            case WHITE: return new Position(9, 0);
            case GREEN: return new Position(14, 0);
            case PEACOCK: return new Position(23, 6);
            case PLUM: return new Position(23, 19);
            case SCARLET: return new Position(7, 24);
            case MUSTARD: return new Position(0, 17);
        }
        return null;
    }

    /**
     * Creates cards, chooses a person, weapon and room at random to be the murder items
     * shuffles the remaining cards and deals them out to the players

     * @param players the list of players playing the game to be dealt cards to
     * @param numPlayers the number of players playing the game
     */
    public void dealCards(List<Player> players, int numPlayers){
        Game game = new Game();
        game.dealCards(players, numPlayers);
    }
}
