package org.example;
import Chess.Board;
import Chess.Coordinate;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board();
        Scanner scan = new Scanner(System.in);
        while(true){
            board.print();
            String str_input = scan.nextLine();
            Coordinate coordinate =  new Coordinate(str_input);
            List<Coordinate> possibleMoves = board.get(coordinate).getPossibleMoves(board, coordinate);
            for (Coordinate moveTo : possibleMoves) {
                System.out.println(moveTo.toString());
            }
            str_input = scan.nextLine();
            Coordinate coordinateTo =  new Coordinate(str_input);
            if (!possibleMoves.contains(coordinateTo)){throw new IllegalArgumentException();}
            board.move(coordinate, coordinateTo);
        }
    }
}
