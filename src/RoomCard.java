public class RoomCard extends Card {
    Game.Rooms name;

    RoomCard(Game.Rooms name) {
        this.name = name;
    }

    public Game.Rooms getRoom(){ return name;}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
