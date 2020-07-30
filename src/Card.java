/**
 * Card class
 * -Main class for 3 card subclasses to map to
 */
public class Card<T extends Enum<T>> {
    T name;

    Card(T name) {
        this.name = name;
    }

    public T getEnum(){ return name;}

    @Override
    public String toString() {
        return name.toString();//.substring(0, 2);
    }
}
