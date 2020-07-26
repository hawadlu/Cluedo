import java.io.IOException;

public class Game {
    public static void main(String[] args) throws IOException {
        //read something in
        String something = TextIO.read("Please input something: ");
        System.out.println(something);
    }
}
