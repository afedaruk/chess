package Chess.Pieces;

import Chess.Board;
import Chess.Color;

import java.util.ArrayList;
import java.util.List;

import Chess.Coordinate;
import javafx.util.Pair;

public abstract class Piece {
    protected Color color;
    protected boolean hasMoved;
    public abstract char toChar();
    public abstract int value();

    public Piece(Color color) {
        this.color = color;
        hasMoved = false;
    }

    public abstract List<Coordinate> getPossibleMoves(Board board, Coordinate coordinate);
}
