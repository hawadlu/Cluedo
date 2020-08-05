import java.util.Objects;

/**
 * Card class
 * -Main class for 3 card subclasses to map to
 */
public class Card<T extends Enum<T>> {
    private final T name;

    Card(T name) {
        this.name = name;
    }

    /**
     * Get the enum (from Game class) that this is a card of
     *
     * @return enum Players/Weapons/Rooms from Game class
     */
    public T getEnum(){ return name;}

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
     * Get the enum this card is for
     *
     * @return enum from Game - Rooms, Weapons, Players
     */
    public T getName() {
        return name;
    }
}
