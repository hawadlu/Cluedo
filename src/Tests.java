import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Tests {

    // ================================================
    // Valid Tests
    // ================================================

    @Test
    public void testMain() throws IOException {
        System.out.println("main");
        String[] args = null;
        Game.main(args);
    }

    @Test
    public void testSuggest() throws InvalidMoveException {
        List<Player> players = createPlayers(6);

        dealCards(players, 6);

        System.out.println("Dealt cards");

        Player testPlayer = players.remove(0);

        //Test the suggest mechanism with some random cards
        Suggest suggest = new Suggest(Game.Rooms.BILLARD_ROOM, Game.Players.PEACOCK, Game.Weapons.ROPE, testPlayer, players);
        suggest.apply();

    }

    /**
     * THESE METHODS ARE HERE SO THAT THEY CAN BE TESTED WITHOUT USER INPUTS VIA THE CONSOLE
     * BECAUSE THESE DON'T WORK WITH JUNIT.
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
            catch (Exception e) {
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
        //Create ArrayLists of each card type
        ArrayList<Card<Game.Players>> playerCards = new ArrayList<>();
        ArrayList<Card<Game.Weapons>> weaponCards = new ArrayList<>();
        ArrayList<Card<Game.Rooms>> roomCards = new ArrayList<>();

        for(Game.Players p : Game.Players.values()){ playerCards.add(new Card<>(p)); }
        for(Game.Weapons w : Game.Weapons.values()){ weaponCards.add(new Card<>(w)); }
        for(Game.Rooms r : Game.Rooms.values()){ roomCards.add(new Card<>(r)); }

        //Shuffling
        Collections.shuffle(playerCards);
        Collections.shuffle(weaponCards);
        Collections.shuffle(roomCards);

        //Add to accuse
        Game.Players accusePlayer = playerCards.get(0).getEnum();
        playerCards.remove(0);
        Game.Rooms accuseRoom = roomCards.get(0).getEnum();
        roomCards.remove(0);
        Game.Weapons accuseWeapon = weaponCards.get(0).getEnum();
        weaponCards.remove(0);
        /* TESTING
        System.out.println("Accuse List: "+ accusePlayer.toString()+", "+accuseRoom.toString()+", "+accuseWeapon.toString());
         */

        //Add rest to big list
        ArrayList<Card<?>> remainingCards = new ArrayList<>();
        remainingCards.addAll(playerCards);
        remainingCards.addAll(roomCards);
        remainingCards.addAll(weaponCards);

        //Shuffle all the cards
        Collections.shuffle(remainingCards);

        //Divide Among players
        int currentPlayer = 0;
        for(Card<?> c : remainingCards){
            players.get(currentPlayer).addToHand(c);
            currentPlayer ++;
            currentPlayer %= numPlayers;
        }

        /* TESTING
        System.out.println("Each Players Cards: ");
        for(Player p : players){
            System.out.println(p.toString() + ": "+p.getHand().toString());
        }
         */
    }
}
