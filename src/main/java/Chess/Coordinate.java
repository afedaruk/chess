package Chess;

import java.util.Objects;

public class Coordinate implements Cloneable {

	public int x;
	public int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate(String coordinate) throws IllegalArgumentException {
		if (coordinate.length() != 2) {
			throw new IllegalArgumentException();
		}
		this.x = parseX(coordinate.charAt(0));
		this.y = parseY(coordinate.charAt(1));
	}

	public static int parseX(char x) throws IllegalArgumentException {
		return x - 'a';
	}

	public static int parseY(char y) throws IllegalArgumentException {
		return y - '0' - 1;
	}

	public Coordinate(Coordinate coordinate) {
		if (coordinate == null) {
			return;
		}
		this.x = coordinate.x;
		this.y = coordinate.y;
	}

	public Coordinate add(Coordinate coordinate) {
		return new Coordinate(x + coordinate.x, y + coordinate.y);
	}

	public Coordinate add(int x, int y) {
		return new Coordinate(x + this.x, y + this.y);
	}

	public Coordinate substract(Coordinate coordinate) {
		return new Coordinate(x - coordinate.x, y - coordinate.y);
	}

	public Coordinate substract(int x, int y) {
		return new Coordinate(x - this.x, y - this.y);
	}

	public String toString() {
		return ((char) ('a' + x) + "" + (y + 1));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Coordinate other = (Coordinate) obj;
		return this.x == other.x && this.y == other.y;
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
