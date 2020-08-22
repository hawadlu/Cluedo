import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

/**
 * Card class
 * -Main class for 3 card subclasses to map to
 */
public class Card<T extends Enum<T>> {
    private final T name;
    private final JLabel image;

    Card(T name) throws InvalidFileException {
        this.name = name;
        try {
            image = new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/Cards/" + name.toString() + ".png"))));
        }catch(Exception e){ throw new InvalidFileException("Assets/Cards/"+ name.toString() + ".png"); }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card<?> card = (Card<?>) o;
        return Objects.equals(name, card.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return name.toString();
    }

    /**
     * Get the enum (from Game class) that this is a card of
     *
     * @return enum Players/Weapons/Rooms from Game class
     */
    public T getEnum(){ return name;}

    public JLabel getImage(){ return image; }
}
