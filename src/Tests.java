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
        Game game = new Game();

        List<Player> players = game.createPlayers(6);

        game.dealCards(players, 6);

        System.out.println("Dealt cards");

        Player testPlayer = players.remove(0);

        //Test the suggest mechanism with some random cards
        Suggest suggest = new Suggest(Game.Rooms.BILLARD_ROOM, Game.Players.PEACOCK, Game.Weapons.ROPE, testPlayer, players);
        suggest.apply();
    }
}
