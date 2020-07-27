public class RoomCard extends Card {
    Game.Rooms roomName;
    public String name;

    public enum RoomChar{
        KI,
        BA,
        ST,
        BI,
        CO,
        DI,
        HA,
        LI,
        LO
    }

    public String getName(){
        return name;
    }

    //TO-DO
    @Override
    public String toString(){
        return "";
    }
}
