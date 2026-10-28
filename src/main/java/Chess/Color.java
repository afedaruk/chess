package Chess;

public enum Color {
    White(1),
    Black(-1);

    private final int direction;

    Color(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
