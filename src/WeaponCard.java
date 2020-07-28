public class WeaponCard extends Card {
    Game.Weapons name;

    WeaponCard(Game.Weapons name) {
        this.name = name;
    }

    public Game.Weapons getWeapon(){ return name;}

    @Override
    public String toString() {
        return name.toString().substring(0, 2);
    }
}
