/**
 * A Player Card
 * -Extends Card class
 */
public class PlayerCard extends Card {
    Game.Players name;

    PlayerCard(Game.Players name) {
        this.name = name;
    }

    public Game.Players getPlayer(){ return name;}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
