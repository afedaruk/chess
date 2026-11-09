package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece implements RookPrototype {

	public Rook(Color color) {
		super(color);
	}

	public Rook copy() {
		return new Rook(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♖'; else {
			return '♜';
		}
	}

	@Override
	public String toLetter() {
		return "R";
	}

	@Override
	public int value() {
		return 5;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		return getRookMoves(board, coordinate, color);
	}
}
