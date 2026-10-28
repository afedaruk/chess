package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import javafx.util.Pair;

import java.util.List;

public class Bishop extends Piece{

    public Bishop(Color color) {
        super(color);
    }
    @Override
    public char toChar() {
        if (color == Color.White)
            return '♗';
        else{
            return '♝';
        }
    }

    @Override
    public int value() {
        return 3;
    }

    @Override
    public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {

        return List.of();
    }
}
