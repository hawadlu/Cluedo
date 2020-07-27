public class RoomCard extends Card {
    Game.rooms roomName;
    public String name;

    public enum roomChar{
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
