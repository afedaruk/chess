package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;

import java.util.List;

public class Rook extends Piece{
    public Rook(Color color) {
        super(color);
    }
    @Override
    public char toChar() {
        if (color == Color.White)
            return '♖';
        else{
            return '♜';
        }
    }

    @Override
    public int value() {
        return 5;
    }

    @Override
    public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
        return List.of();
    }
}
