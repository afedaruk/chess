package Chess;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

import Chess.Pieces.*;
import java.util.ArrayList;
import java.util.List;

public class Board {

	public final int BOARD_SIZE = 8;
	private Color turn;
	private BoardState state = BoardState.PLAYING;
	Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

	public Board() {
		turn = Color.White;
		board[0][0] = new Rook(Color.White);
		board[1][0] = new Knight(Color.White);
		board[2][0] = new Bishop(Color.White);
		board[3][0] = new Queen(Color.White);
		board[4][0] = new King(Color.White);
		board[5][0] = new Bishop(Color.White);
		board[6][0] = new Knight(Color.White);
		board[7][0] = new Rook(Color.White);
		for (int i = 0; i < 8; i++) {
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
		for (int i = 0; i < 8; i++) {
			board[i][6] = new Pawn(Color.Black);
		}
	}

	public Board(Piece[][] board, Color turn) {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = board[i][j];
				if (piece == null) {
					continue;
				}
				this.board[i][j] = piece.copy();
			}
		}
		this.turn = turn;
	}

	public Piece get(Coordinate coordinate) {
		return get(coordinate.x, coordinate.y);
	}

	public Color getTurn() {
		return turn;
	}

	public Piece get(int x, int y) {
		if (x < 0 || x >= BOARD_SIZE) {
			return null;
		}
		if (y < 0 || y >= BOARD_SIZE) {
			return null;
		}
		return board[x][y];
	}

	public void print() {
		System.out.print(" ");
		for (int i = 0; i < BOARD_SIZE; i++) {
			System.out.print((char) (i + 'a'));
			System.out.print(" ");
		}
		System.out.println();
		for (int i = 0; i < 8; i++) {
			System.out.print((char) (i + '1'));
			for (int j = 0; j < 8; j++) {
				if (board[j][i] != null) {
					System.out.print(board[j][i].toChar());
					//System.out.print(" ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}

	public void moveNoTurn(Coordinate from, Coordinate to) {
		if (this.get(from) == null) {
			throw new IllegalArgumentException();
		}
		if (this.get(from).getColor() != turn) {
			throw new IllegalArgumentException();
		}
		// Castling
		Piece piece = this.get(from);
		Piece toPiece = this.get(to);
		if (
			toPiece != null &&
			piece.getColor() == toPiece.getColor() &&
			piece instanceof King &&
			toPiece instanceof Rook
		) {
			Coordinate newKingsPosition = new Coordinate((int) ceil((from.x + to.x) / 2.0), from.y);
			int rookShift = ((from.x + to.x) % 2 == 0) ? 1 : -1;
			Coordinate newRooksPosition = new Coordinate(newKingsPosition.x + rookShift, from.y);
			board[from.x][from.y].move();
			board[to.x][to.y].move();
			board[newKingsPosition.x][newKingsPosition.y] = piece;
			board[newRooksPosition.x][newRooksPosition.y] = toPiece;
			board[from.x][from.y] = null;
			board[to.x][to.y] = null;
			return;
		}
		board[from.x][from.y].move();
		board[to.x][to.y] = board[from.x][from.y];
		board[from.x][from.y] = null;
	}

	public void move(Coordinate from, Coordinate to) {
		moveNoTurn(from, to);
		turn = turn.getOpposite();
	}

	public void moveAndUpdateState(Coordinate from, Coordinate to) {
		move(from, to);
		updateIsCheckmated(turn);
		updateIsStalemated(turn);
	}

	public boolean isCoordinateValid(Coordinate coordinate) {
		return coordinate.x >= 0 && coordinate.x < BOARD_SIZE && coordinate.y >= 0 && coordinate.y < BOARD_SIZE;
	}

	public Coordinate getKingCoordinate(Color kingColor) {
		Coordinate kingCoordinate = new Coordinate(0, 0);
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = get(i, j);
				if (piece == null) {
					continue;
				}
				if (piece instanceof King && piece.getColor() == kingColor) kingCoordinate = new Coordinate(i, j);
			}
		}
		return kingCoordinate;
	}

	public boolean isUnderCheck(Color colorUnderCheck) {
		Coordinate kingCoordinate = getKingCoordinate(colorUnderCheck);
		var hasCheck = false;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = get(i, j);
				if (piece == null) {
					continue;
				}
				List<Coordinate> pieceMoves = piece.getPossibleMoves(this, new Coordinate(i, j));
				if (piece.getColor() != colorUnderCheck && pieceMoves.contains(kingCoordinate)) hasCheck = true;
			}
		}
		return hasCheck;
	}

	private void updateIsCheckmated(Color colorCheckmated) {
		if (!isUnderCheck(colorCheckmated)) return;
		boolean isCheckmated = hasNoMoves(colorCheckmated);
		if (isCheckmated) {
			state = BoardState.CHECKMATE;
		}
	}

	private void updateIsStalemated(Color colorStalemated) {
		if (isUnderCheck(colorStalemated)) return;
		boolean isStalemated = hasNoMoves(colorStalemated);
		if (isStalemated) {
			state = BoardState.STALEMATE;
		}
	}

	private boolean hasNoMoves(Color color) {
		boolean noMoves = true;
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = get(i, j);
				if (piece == null) {
					continue;
				}
				if (piece.getColor() != color) {
					continue;
				}
				List<Coordinate> pieceMoves = piece.getPossibleMoves(this, new Coordinate(i, j));
				for (Coordinate pieceMove : pieceMoves) {
					Board possibleBoard = new Board(board, piece.getColor());
					possibleBoard.move(new Coordinate(i, j), pieceMove);
					if (!possibleBoard.isUnderCheck(color)) {
						noMoves = false;
						break;
					}
				}
			}
		}
		return noMoves;
	}

	public int getSize() {
		return BOARD_SIZE;
	}

	public List<Coordinate> getPieceMoves(Coordinate c) {
		Piece piece = get(c);
		if (turn != piece.getColor()) {
			return List.of();
		}
		var possibleMoves = piece.getPossibleMoves(this, c);
		var validMoves = new ArrayList<Coordinate>();
		for (var move : possibleMoves) {
			Piece toPiece = get(move);
			if (
				toPiece != null &&
				piece.getColor() == toPiece.getColor() &&
				piece instanceof King &&
				toPiece instanceof Rook
			) {
				// Castling
				Coordinate newKingsPosition = new Coordinate((int) ceil((c.x + move.x) / 2.0), c.y);
				int castlingDirection = (move.x - c.x) / abs(move.x - c.x);
				Coordinate currKingCoordinate = c.clone();
				boolean isValidCastling = true;
				Board board = new Board(this.board, this.turn);
				while (!currKingCoordinate.equals(newKingsPosition)) {
					System.out.println("Curr King: " + currKingCoordinate);
					Coordinate kingCoordinateAfterStep = currKingCoordinate.add(new Coordinate(castlingDirection, 0));
					if (board.get(kingCoordinateAfterStep) != null) {
						isValidCastling = false;
						break;
					}
					board.moveNoTurn(currKingCoordinate, kingCoordinateAfterStep);
					currKingCoordinate = kingCoordinateAfterStep;
					if (board.isUnderCheck(turn)) {
						isValidCastling = false;
						break;
					}
				}
				if (isValidCastling) {
					validMoves.add(move);
				}
			} else {
				System.out.println("Checking move " + move);
				Board board = new Board(this.board, this.turn);
				board.move(c, move);
				if (!board.isUnderCheck(turn)) {
					validMoves.add(move);
				}
			}
		}
		return validMoves;
	}

	public List<Coordinate> getPieceMoves(int x, int y) {
		return getPieceMoves(new Coordinate(x, y));
	}

	public BoardState getState() {
		return state;
	}

	public Color getWinner() {
		if (state == BoardState.CHECKMATE) {
			return turn.getOpposite();
		}
		return null;
	}
}
