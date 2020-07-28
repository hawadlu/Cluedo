import java.util.ArrayList;

/**
 * Physical PLayer.
 * -Contains their hand, pos & if they've lost
 */
public class Player {
    Game.Players name;
    ArrayList<Card> hand;
    boolean hasLost;
    Position position;

    Player(Game.Players name, Position pos) {
        this.name = name;
        hand = new ArrayList<>();
        hasLost = false;
        position = pos;
    }

    public void addToHand(Card card){
        this.hand.add(card);
    }

    public ArrayList<Card> getHand(){ return hand; }

    @Override
    public String toString() {
        return name.toString();
    }
}
