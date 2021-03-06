import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Weapons class for visual representation.
 * of Weapons in rooms
 */
public class Weapon {
    private final Game.Rooms currentRoom;
    private final Game.Weapons name;
    private final BufferedImage image;
    private Position newPos;

    Weapon(Game.Weapons name, Game.Rooms room) throws InvalidFileException {
        this.name = name;
        currentRoom = room;
        try {
            image = ImageIO.read(new File("Assets/WeaponPieces/" + name.toString() + ".png"));
        }catch(IOException e){ throw new InvalidFileException(name.toString() + ".png"); }
    }


    /**
     * Set the newest position of this weapon.
     *
     * @param newPos the new position to be set as this weapons newest position
     */
    public void setNewPos(Position newPos) {
        this.newPos = newPos;
    }

    /**
     * Get the position of this weapon.
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

    @Override
    public String toString() {
        return name.toString();
    }
}
