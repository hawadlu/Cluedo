/**
 * Records the Players position
 *  - Use with 2d array?
 */
public class Position {
    int x, y;

    Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    Position(Position other) {
        x = other.x;
        y = other.y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
