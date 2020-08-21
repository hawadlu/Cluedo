import java.io.*;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

public class Tests {
    @Test
    public void testGUI() throws IOException {
        GUI gui = new GUI();

        //todo remove
        for (int i = 0; i < 40; i++) {
            gui.addToConsole("Some message: " + i);
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gui.redraw();
        }
        gui.redraw();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
