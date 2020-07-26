import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Simple class for taking input from the console
 */
public class Input {
    public static String read(String statement) throws IOException
    {
        System.out.println(statement);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.readLine();
    }
}
