package Chess;

public class Coordinate {
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

    public Coordinate add(Coordinate coordinate) {
        return new Coordinate(x + coordinate.x, y + coordinate.y);
    }

    public Coordinate add(int x, int y) {
        return new Coordinate(x + this.x, y + this.y);
    }

    public String toString() {
        return ((char)('a'+x) +""+ (y+1));
    }
}
