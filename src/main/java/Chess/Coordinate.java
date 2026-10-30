package Chess;

import java.util.Objects;

public class Coordinate implements Cloneable {
    public int x;
    public int y;
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(String coordinate) {
        if (coordinate.length() != 2) {throw new IllegalArgumentException();}
        this.x = coordinate.charAt(0) - 'a';
        this.y = coordinate.charAt(1) - '0' - 1;
    }

    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
    }

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(x + coordinate.x, y + coordinate.y);
    }

    public Coordinate add(int x, int y) {
        return new Coordinate(x + this.x, y + this.y);
    }

    public String toString() {
        return ((char)('a'+x) +""+ (y+1));
    }

    @Override
    public boolean equals(Object obj) {
        // 1. Same memory reference check
        if (this == obj) return true;

        // 2. Null check and EXACT class equality check
        if (obj == null || getClass() != obj.getClass()) return false;

        // 3. Safe cast since runtime types match exactly
        Coordinate other = (Coordinate) obj;

        // 4. Field-by-field comparison
        return this.x == other.x
                && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public Coordinate clone() {
        try {
            return (Coordinate) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
