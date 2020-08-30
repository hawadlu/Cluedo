import java.io.*;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

public class Tests {
    /**
     * Test the gui to see that it displays properly
     * @throws IOException if there is a I/O problem
     * @throws InvalidFileException if there is a missing file
     */
    @Test
    public void testGUI() throws IOException, InvalidFileException {
        Game.board = new Board();
        Game.gui = new GUI();

        //if we get this far the test passes
    }

    /**
     * Make a correct accusation
     * @throws InvalidFileException if there is a missing file
     */
    @Test
    public void testAccusation01() throws InvalidFileException {
        Game.murderWeapon = Game.Weapons.CANDLESTICK;
        Game.murderRoom = Game.Rooms.BILLIARD_ROOM;
        Game.murderer = Game.Suspects.GREEN;

        Player suspect = new Player(Game.Suspects.GREEN, new Position(0,0));

        new Accuse(Game.murderRoom, Game.murderer, Game.murderWeapon, suspect).apply();
        assert Game.gameOver;
    }

    /**
     * Make an incorrect accusation
     * @throws InvalidFileException if there is a missing file
     */
    @Test
    public void testAccusation02() throws InvalidFileException {
        Game.murderWeapon = Game.Weapons.ROPE;
        Game.murderRoom = Game.Rooms.LIBRARY;
        Game.murderer = Game.Suspects.MUSTARD;

        Player suspect = new Player(Game.Suspects.GREEN, new Position(0,0));

        Accuse accuse = new Accuse(Game.Rooms.BILLIARD_ROOM, Game.Suspects.PLUM, Game.Weapons.CANDLESTICK, suspect);
        accuse.apply();
        assert suspect.hasLost();
    }

    /**
     * Try to throw an error while opening a file
     */
    @Test
    public void throwFileError01() {
        try {
            openFile("Some invalid file");
        } catch (InvalidFileException e) {
            e.printStackTrace();

            //If the code reached here the test passed.
        }
    }

    /**
     * Try to open a valid file
     */
    @Test
    public void throwFileError02() {
        try {
            openFile("Assets/textcluedo.txt");
        } catch (InvalidFileException e) {
            e.printStackTrace();
        }
    }

    /**
     * Force a player to make an invalid move
     */
    @Test
    public void makeMove() throws InvalidFileException {
        Game.board = new Board();
        Player player = new Player(Game.Suspects.PLUM, new Position(10, 10));
        player.moveTo(Game.board.getTile(new Position(11, 10)));
        assert player.getPos().equals(new Position(11,10));
    }

    /**
     * Test adding messages to the console.
     * It should not display more than
     */
    @Test
    public void testConsole() throws IOException, InvalidFileException {
        Game.gui = new GUI();

        //Add messages
        for (int i = 0; i < 1001; i++) Game.gui.addToConsole("This is message " + i);
    }

    public void openFile(String path) throws InvalidFileException {
        try { new Scanner(new File(path)); }
        catch (FileNotFoundException e) {
            throw new InvalidFileException("File not found");
        }
    }
}
