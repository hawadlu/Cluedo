public class RoomCard extends Card {
    Game.Rooms name;

    RoomCard(Game.Rooms name) {
        this.name = name;
    }

    public String getName(){ return name.toString();}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
