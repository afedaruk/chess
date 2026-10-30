package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{
    public Pawn(Color color) {
        super(color);
    }
    @Override
    public char toChar() {
        if (color == Color.White)
            return '♙';
        else{
            return '♟';
        }
    }

    @Override
    public int value() {
        return 1;
    }

    @Override
    public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        Coordinate forward = coordinate.add(0, color.getDirection());
        Coordinate two_forward = coordinate.add(0, 2*color.getDirection());
        Coordinate forward_left = coordinate.add(1, color.getDirection());
        Coordinate forward_right = coordinate.add(-1, color.getDirection());
        if (board.get(forward) == null) {
            possibleMoves.add(forward);
        }
        if (board.get(forward) == null && board.get(two_forward) == null && !hasMoved) {
            possibleMoves.add(two_forward);
        }
        if (board.get(forward_left)!=null && board.get(forward_left).color != color){
            possibleMoves.add(forward_left);
        }
        if (board.get(forward_right)!=null && board.get(forward_right).color != color){
            possibleMoves.add(forward_right);
        }
        return possibleMoves;
    }
}
