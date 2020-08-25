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
    private Position newPos;

    Weapon(Game.Weapons name, Game.Rooms room) throws InvalidFileException {
        this.name = name;
        lastRoom = null;
        currentRoom = room;
        try {
            image = ImageIO.read(new File("Assets/WeaponPieces/" + name.toString() + ".png"));
        }catch(Exception e){ throw new InvalidFileException(name.toString() + ".png"); }
    }


    /**
     * Set the newest position of this weapon
     *
     * @param newPos the new position to be set as this weapons newest position
     */
    public void setNewPos(Position newPos) {
        this.newPos = newPos;
    }

    /**
     * Get the position of this weapon
     *
     * @return the position of this weapon
     */
    public Position getPos(){ return newPos; }

    public Game.Rooms getRoom(){
        return currentRoom;
    }

    public BufferedImage getImage() {
        return image;
    }
}
