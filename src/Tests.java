import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.*;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Tests {

    // ================================================
    // Valid Tests
    // ================================================

    @Test
    public void testMain() throws IOException {
        System.out.println("main");
        String[] args = null;
        Game.main(args);
    }
}
