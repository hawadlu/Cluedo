import java.util.Objects;

/**
 * Records the Players position
 *  - Use with 2d array?
 */
public class Position {
    public int x, y;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
