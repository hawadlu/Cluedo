public class WeaponCard extends Card {
    Game.Weapons name;

    WeaponCard(Game.Weapons name) {
        this.name = name;
    }

    public String getName(){ return name.toString();}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
