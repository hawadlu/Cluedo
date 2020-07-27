public class Player {
    Game.Players name;

    Player(Game.Players name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toString();
    }
}
