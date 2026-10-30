package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

	public Queen(Color color) {
		super(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♕'; else {
			return '♛';
		}
	}

	@Override
	public int value() {
		return 9;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		List<Coordinate> possibleMoves = new ArrayList<>();
		for (Coordinate c = new Coordinate(coordinate.x + 1, coordinate.y); c.x < board.getSize(); c.x++) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (Coordinate c = new Coordinate(coordinate.x - 1, coordinate.y); c.x >= 0; c.x--) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (Coordinate c = new Coordinate(coordinate.x, coordinate.y + 1); c.y < board.getSize(); c.y++) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (Coordinate c = new Coordinate(coordinate.x, coordinate.y - 1); c.y >= 0; c.y--) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (
			Coordinate c = new Coordinate(coordinate.x + 2, coordinate.y + 2);
			c.x < board.getSize() && c.y < board.getSize();
			c.x++, c.y++
		) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (
			Coordinate c = new Coordinate(coordinate.x + 2, coordinate.y - 2);
			c.x < board.getSize() && c.y >= 0;
			c.x++, c.y--
		) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (
			Coordinate c = new Coordinate(coordinate.x - 2, coordinate.y + 2);
			c.x >= 0 && c.y < board.getSize();
			c.x--, c.y++
		) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		for (Coordinate c = new Coordinate(coordinate.x - 2, coordinate.y - 2); c.x >= 0 && c.y >= 0; c.x--, c.y--) {
			if (board.get(c) == null) {
				possibleMoves.add(c.clone());
				continue;
			}
			if (board.get(c).getColor() != color) {
				possibleMoves.add(c.clone());
				break;
			}
			break;
		}
		return possibleMoves;
	}
}
