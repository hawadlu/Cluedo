import java.util.ArrayList;

/**
 * Physical PLayer
 */
public class Player {
    Game.Players name;
    ArrayList<Card> hand;
    boolean hasLost;
    Position position;

    Player(Game.Players name) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        //position = pos;
    }

    public void addHand(ArrayList<Card> newHand){
        this.hand = newHand;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
