public class Die {
    private int number;

    public Die() {
        roll();
    }

    public int roll() {
        return number = (int) (Math.random() * 6 + 1);
    }

    public void draw() {
        String fileName = "Assets/DieFaces/DieFace"+number+".png";
    }
}
