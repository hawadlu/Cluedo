import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Weapons class for visual representation
 * of Weapons in rooms
 */
public class Weapon {
    private Game.Rooms lastRoom;
    private Game.Rooms currentRoom;
    private final Game.Weapons name;
    private final BufferedImage image;

    Weapon(Game.Weapons name, Game.Rooms room) throws InvalidFileException {
        this.name = name;
        lastRoom = null;
        currentRoom = room;
        try {
            image = ImageIO.read(new File("Assets/" + name.toString() + ".png"));
        }catch(Exception e){ throw new InvalidFileException(name.toString() + ".png"); }
    }

    /**
     * Drawing a weapon at current position
     *
     * @param g graphics
     * @param pos position of current weapon
     */
    protected void draw(Graphics g, Position pos){
        g.drawImage(image, pos.x, pos.y, null);
    }
}
