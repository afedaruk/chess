package Chess;

import static java.lang.Math.abs;
import static java.lang.Math.ceil;

import Chess.Pieces.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

	public final int BOARD_SIZE = 8;
	private final int MAX_MOVES_WITHOUT_PAWN_MOVE = 100; // 50 full moves
	private final List<Color> PLAYER_COLORS = List.of(Color.White, Color.Black);

	private Color turn;
	private int movesWithoutPawnMove;
	public final HistoryBuilder historyBuilder;
	private Map<Color, Integer> material;
	private BoardState state = BoardState.PLAYING;
	public Piece[][] board = new Piece[BOARD_SIZE][BOARD_SIZE];

	public Board() {
		movesWithoutPawnMove = 0;
		turn = Color.White;
		historyBuilder = new HistoryBuilder();
		material = new HashMap<Color, Integer>();
		material.put(Color.White, 39);
		material.put(Color.Black, 39);
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

	public Board(Board board) {
		this.board = new Piece[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = board.board[i][j];
				if (piece == null) {
					continue;
				}
				this.board[i][j] = piece.copy();
			}
		}
		this.turn = board.turn;
		this.state = board.state;
		this.historyBuilder = board.historyBuilder.copy();
		this.material = new HashMap<Color, Integer>(board.material);
		this.movesWithoutPawnMove = 0;
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
			historyBuilder.addCastling();
			return;
		}
		board[from.x][from.y].move();
		board[to.x][to.y] = board[from.x][from.y];
		board[from.x][from.y] = null;
		historyBuilder.add(piece.toLetter() + to.toString(), isUnderCheck(turn.getOpposite()));
	}

	public void move(Coordinate from, Coordinate to) {
		moveNoTurn(from, to);
		turn = turn.getOpposite();
	}

	public void moveAndUpdateState(Coordinate from, Coordinate to) {
		if (!(board[from.x][from.y] instanceof Pawn)) {
			movesWithoutPawnMove++;
		}
		move(from, to);
		updateIsCheckmated(turn);
		updateIsStalemated(turn);
		updateDraw();
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
				if (piece.getColor() == colorUnderCheck) {
					continue;
				}
				List<Coordinate> pieceMoves = piece.getPossibleMoves(this, new Coordinate(i, j));
				if (pieceMoves.contains(kingCoordinate)) hasCheck = true;
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
			state = BoardState.DRAW;
		}
	}

	private void updateDraw() {
		if (movesWithoutPawnMove >= MAX_MOVES_WITHOUT_PAWN_MOVE) {
			state = BoardState.DRAW;
		}
		if (material.get(Color.White) <= 3) {
			state = BoardState.DRAW;
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
					Board possibleBoard = new Board(this);
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
		if (state != BoardState.PLAYING || turn != piece.getColor()) {
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
				Board board = new Board(this);
				if (board.isUnderCheck(turn)) {
					isValidCastling = false;
					break;
				}
				while (!currKingCoordinate.equals(newKingsPosition)) {
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
				Board board = new Board(this);
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

	public void replacePiece(int x, int y, Piece chosenPiece) {
		board[x][y] = chosenPiece;
	}

	public List<Piece> getAllPieces() {
		List<Piece> pieces = new ArrayList<>();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				Piece piece = get(i, j);
				if (piece != null) {
					pieces.add(piece);
				}
			}
		}
		return pieces;
	}

	public void resign() {
		this.state = BoardState.CHECKMATE;
	}
}
