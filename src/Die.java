import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Die {
    private int number;

    public Die() {
        roll();
    }

    /**
     * Roll this 6-faced die
     * @return the number rolled
     */
    public int roll() {
        return number = (int) (Math.random() * 6 + 1);
    }

    /**
     * Set this die to be blank
     */
    public void setBlank() {
        number = 0;
    }

    /**
     * Draw the die
     *
     * @return a new JLabel for drawing the image
     */
    public JLabel getImage() throws InvalidFileException {
        try {
            return new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/DieFaces/DieFace" + number + ".png"))));
        } catch (IOException e) { throw new InvalidFileException("DieFace" + number + ".png"); }
    }
}
