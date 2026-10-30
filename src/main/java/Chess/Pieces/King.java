package Chess.Pieces;

import Chess.Board;
import Chess.Color;
import Chess.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{
    public King(Color color) {
        super(color);
    }
    @Override
    public char toChar() {
        if (color == Color.White)
            return '♔';
        else{
            return '♚';
        }
    }

    @Override
    public int value() {
        return 9999;
    }

    @Override
    public List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate) {
        List<Coordinate> possibleMoves = new ArrayList<>();
        for(int x = -1; x <= 1; x++ ){
            for(int y = -1; y <= 1; y++ ){
                if(x == 0 && y == 0){ continue; }
                Coordinate c = coordinate.add(x, y);
                if (!board.isCoordinateValid(c)){ continue; }
                if (board.get(c)==null || board.get(c).color != color){
                    possibleMoves.add(c);
                }
            }
        }
        return possibleMoves;
    }
}

