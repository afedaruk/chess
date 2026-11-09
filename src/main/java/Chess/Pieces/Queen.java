package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece implements BishopPrototype, RookPrototype {

	public Queen(Color color) {
		super(color);
	}

	@Override
	public Queen copy() {
		return new Queen(color);
	}

	@Override
	public char toChar() {
		if (color == Color.White) return '♕'; else {
			return '♛';
		}
	}

	@Override
	public String toLetter() {
		return "Q";
	}

	@Override
	public int value() {
		return 9;
	}

	@Override
	public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
		List<Coordinate> moves = new ArrayList<>();
		moves.addAll(getBishopMoves(board, coordinate, this.color));
		moves.addAll(getRookMoves(board, coordinate, this.color));
		return moves;
	}
}
