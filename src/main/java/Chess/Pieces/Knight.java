package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

	public Knight(Color color) {
		super(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♘'; else {
			return '♞';
		}
	}

	@Override
	public String toLetter() {
		return "N";
	}

	@Override
	public int value() {
		return 3;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		List<Coordinate> possibleMoves = new ArrayList<>();
		List<Coordinate> moves = List.of(
			new Coordinate(1, 2),
			new Coordinate(2, 1),
			new Coordinate(-1, 2),
			new Coordinate(-2, 1),
			new Coordinate(1, -2),
			new Coordinate(2, -1),
			new Coordinate(-1, -2),
			new Coordinate(-2, -1)
		);
		for (Coordinate move : moves) {
			Coordinate newPosition = coordinate.add(move);
			if (
				board.isCoordinateValid(newPosition) &&
				(board.get(newPosition) == null || board.get(newPosition).color != getColor())
			) {
				possibleMoves.add(newPosition);
			}
		}
		return possibleMoves;
	}

	@Override
	public Knight copy() {
		return new Knight(color);
	}
}
