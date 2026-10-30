package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.List;
import java.util.Optional;

public abstract class Piece {

	protected Color color;
	protected boolean hasMoved;

	public abstract char toChar();

	public abstract int value();

	public Piece(Color color) {
		this.color = color;
		hasMoved = false;
	}

	public abstract List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate);

	public Color getColor() {
		return color;
	}

	public void move() {
		hasMoved = true;
	}
}
