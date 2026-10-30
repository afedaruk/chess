package Chess;

import Chess.Pieces.*;

public class Board {
    private final int BOARD_SIZE = 8;
    private Color turn;
    Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];
    public Board() {

        board[0][2] = new Rook(Color.Black);
        turn = Color.White;
        board[0][0] = new Rook(Color.White);
        board[1][0] = new Knight(Color.White);
        board[2][0] = new Bishop(Color.White);
        board[3][0] = new Queen(Color.White);
        board[4][0] = new King(Color.White);
        board[5][0] = new Bishop(Color.White);
        board[6][0] = new Knight(Color.White);
        board[7][0] = new Rook(Color.White);
        for(int i = 0; i < 8; i++) {
            board[i][1] = new Pawn(Color.White);
        }
        board[0][7] = new Rook(Color.Black);
        board[1][7] = new Knight(Color.Black);
        board[2][7] = new Bishop(Color.Black);
        board[3][7] = new Queen(Color.Black);
        board[4][7] = new King(Color.Black);
        board[5][7] = new Bishop(Color.Black);
        board[6][7] = new Knight(Color.Black);
        board[7][7] = new Rook(Color.Black);
        for(int i = 0; i < 8; i++) {
            board[i][6] = new Pawn(Color.Black);
        }
    }

    public Piece get(Coordinate coordinate) {
        return get(coordinate.x, coordinate.y);
    }

    public Piece get(int x, int y) {
        if (x < 0 || x >= BOARD_SIZE) { return null;}
        if (y < 0 || y >= BOARD_SIZE) { return null;}
        return board[x][y];
    }

    public void print() {
        System.out.print(" ");
        for(int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((char)(i+'a'));
            System.out.print(" ");
        }
        System.out.println();
        for(int i = 0; i < 8; i++) {
            System.out.print((char)(i+'1'));
            for(int j = 0; j < 8; j++) {
                if (board[j][i] != null) {
                    System.out.print(board[j][i].toChar());
                    //System.out.print(" ");
                }
                else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
    public void move(Coordinate from, Coordinate to) {
        if (this.get(from)==null){
            throw new IllegalArgumentException();
        }
        if (this.get(from).getColor()!=turn){
            throw new IllegalArgumentException();
        }
        board[from.x][from.y].move();
        board[to.x][to.y] = board[from.x][from.y];
        board[from.x][from.y] = null;
        turn = turn.getOpposite();
    }

    public boolean isCoordinateValid(Coordinate coordinate) {
        return coordinate.x >= 0 &&
                coordinate.x < BOARD_SIZE &&
                coordinate.y >= 0 &&
                coordinate.y < BOARD_SIZE;
    }

    public int getSize(){
        return BOARD_SIZE;
    }
}
