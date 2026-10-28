package org.example;
import Chess.Board;
import Chess.Coordinate;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        board.print();
        List<Coordinate> possibleMoves = board.get(1, 1).getPossibleMoves(board, new Coordinate(1, 1));
        for (Coordinate coordinate : possibleMoves) {
            System.out.println(coordinate.toString());
        }
    }
}
