import javax.imageio.ImageIO;
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
     * Draw the die
     *
     * @param g the graphics plane to draw on
     * @param pos the position to draw the die at
     */
    public void draw(Graphics g, Position pos) throws InvalidFileException {
        try {
            String fileName = "Assets/DieFaces/DieFace" + number + ".png";
            BufferedImage image = ImageIO.read(new File(fileName));
            g.drawImage(image, pos.x, pos.y, null);
        } catch (IOException e) { throw new InvalidFileException("DieFace" + number + ".png"); }
    }
}
