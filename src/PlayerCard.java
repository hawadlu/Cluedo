/**
 * A Player Card
 * -Extends Card class
 */
public class PlayerCard extends Card {
    Game.Players name;

    PlayerCard(Game.Players name) {
        this.name = name;
    }

    public String getName(){ return name.toString();}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
