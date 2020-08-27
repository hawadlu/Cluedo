import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Card class
 * -Main class for 3 card subclasses to map to.
 */
public class Card<T extends Enum<T>> {
    private final T name;
    private final JLabel image;
    private final String extraInfo;
    private boolean hidden = false;
    private final JLabel defaultImg;

    // Initialise defaultImg

    Card(T name, HashMap<String, String> cardInfo) throws InvalidFileException {
        this.extraInfo = cardInfo.get(name.toString());
        this.name = name;
        try {
            image = new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/Cards/" + name.toString() + ".png"))));
        }catch(IOException e){ throw new InvalidFileException("Assets/Cards/"+ name.toString() + ".png"); }
        try {
            defaultImg = new JLabel(new ImageIcon(ImageIO.read(
                    new File("Assets/Cards/DEFAULT.png"))));
        }catch(IOException e){ throw new InvalidFileException("Assets/Cards/DEFAULT.png"); }
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

    public JLabel getImage(){
        if (hidden)
            return defaultImg;
        return image;
    }

    public String getExtraInfo() {return name.toString() + ": " + extraInfo;}

    /**
     * Hide this card
     */
    public void hide() {
        hidden = true;
    }

    /**
     * Show this card
     */
    public void show() {
        hidden = false;
    }
}
