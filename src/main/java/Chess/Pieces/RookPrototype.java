package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public interface RookPrototype {
	public default List<Coordinate> getRookMoves(Board board, Coordinate coordinate, Color color) {
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
		return possibleMoves;
	}
}
