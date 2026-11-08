package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Bishop extends Piece implements BishopPrototype {

	public Bishop(Color color) {
		super(color);
	}

	@Override
	public Bishop copy() {
		return new Bishop(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♗'; else {
			return '♝';
		}
	}

	@Override
	public int value() {
		return 3;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		return getBishopMoves(board, coordinate, color);
	}
}
