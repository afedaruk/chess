package Chess.Pieces;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

	public King(Color color) {
		super(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♔'; else {
			return '♚';
		}
	}

	@Override
	public String toLetter() {
		return "K";
	}

	@Override
	public int value() {
		return 0;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		List<Coordinate> possibleMoves = new ArrayList<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if (x == 0 && y == 0) {
					continue;
				}
				Coordinate c = coordinate.add(x, y);
				if (!board.isCoordinateValid(c)) {
					continue;
				}
				if (board.get(c) == null || board.get(c).color != color) {
					possibleMoves.add(c);
				}
			}
		}
		// Castling
		List<Coordinate> rooks = new ArrayList<>();
		for (int x = 0; x <= board.BOARD_SIZE; x++) {
			for (int y = 0; y <= board.BOARD_SIZE; y++) {
				Piece piece = board.get(x, y);
				if (piece != null && piece.color == color && piece instanceof Rook && !piece.hasMoved) {
					rooks.add(new Coordinate(x, y));
				}
			}
		}
		if (!hasMoved) {
			possibleMoves.addAll(rooks);
		}
		return possibleMoves;
	}

	@Override
	public King copy() {
		return new King(color);
	}
}
