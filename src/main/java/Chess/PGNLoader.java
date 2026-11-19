package Chess;

import Chess.Pieces.Piece;
import Chess.Pieces.Rook;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PGNLoader extends PGNBase {

	private static final String INVALID_PGN = "Invalid PGN";

	public static Board LoadPGN(String PGN) throws IllegalArgumentException {
		Board board = new Board();
		String token = "";
		boolean readingTags = false;
		for (char symbolChar : PGN.toCharArray()) {
			String symbol = Character.toString(symbolChar);
			if (symbol.equals(TAG_OPENING_SYMBOL)) {
				readingTags = true;
				continue;
			}
			if (symbol.equals(TAG_CLOSING_SYMBOL)) {
				readingTags = false;
				continue;
			}
			if (readingTags) {
				continue;
			}
			if (symbol.equals(CHECKMATE_SYMBOL) || symbol.equals(RESULT_UNFINISHED)) {
				break; // game is over
			}
			if (symbol.equals(CHECK_SYMBOL)) {
				continue;
			}
			if (symbol.equals(MOVE_NUMBER_SYMBOL)) {
				token = "";
				continue;
			}
			if (token.isEmpty()) {
				if (GameConstants.getPiecesLetters().contains(symbol)) {
					token = symbol;
					continue;
				}
			}
			if (symbol.equals(" ") || symbol.equals("\n")) {
				reviewToken(board, token);
				token = "";
				continue;
			}
			token += symbol;
		}
		if (!token.isEmpty()) reviewToken(board, token);
		return board;
	}

	private static void reviewToken(Board board, String token) {
		if (token.isEmpty()) {
			return;
		}
		boolean isNumberMarker = token.endsWith(MOVE_NUMBER_SYMBOL);
		if (isNumberMarker) {
			return;
		}
		boolean isPiecePawn = !GameConstants.getPiecesLetters().contains(token.substring(0, 1));
		if (isPiecePawn && token.length() == 2 || token.length() >= 3) {
			movePiece(board, token);
		}
	}

	private static void movePiece(Board board, String token) throws IllegalArgumentException {
		Color color = board.getTurn();
		if (token.equals(RESULT_WHITE_WIN)) {
			board.resign(Color.Black);
			return;
		}
		if (token.equals(RESULT_BLACK_WIN)) {
			board.resign(Color.White);
			return;
		}
		if (token.equals(RESULT_DRAW)) {
			board.draw();
			return;
		}
		if (token.equals(SHORT_CASTLING)) {
			// Short castling
			Coordinate kingPosition = board.getKingCoordinate(board.getTurn());
			List<Coordinate> moves = board.getPieceMoves(kingPosition);
			for (Coordinate move : moves) {
				if (board.get(move) instanceof Rook) {
					Coordinate rookPosition = kingPosition.add(GameConstants.SHORT_CASTLING_VECTOR);
					if (move.equals(rookPosition)) {
						board.move(kingPosition, rookPosition);
						return;
					}
				}
			}
		}
		if (token.equals(LONG_CASTLING)) {
			// Long castling
			Coordinate kingPosition = board.getKingCoordinate(board.getTurn());
			List<Coordinate> moves = board.getPieceMoves(kingPosition);
			for (Coordinate move : moves) {
				if (board.get(move) instanceof Rook) {
					Coordinate rookPosition = kingPosition.add(GameConstants.LONG_CASTLING_VECTOR);
					if (move.equals(rookPosition)) {
						board.move(kingPosition, rookPosition);
						return;
					}
				}
			}
		}
		String pieceLetter = token.substring(0, 1);
		if (!GameConstants.getPiecesLetters().contains(pieceLetter)) {
			// The piece is a pawn
			pieceLetter = "";
		} else {
			token = token.substring(1);
		}
		// Remove capture indicator
		token = token.replace(CAPTURE_SYMBOL, "");
		String pieceLetterAfterMove = pieceLetter;
		if (token.startsWith(PROMOTION_SYMBOL, token.length() - 2)) {
			// Handle promotion
			pieceLetterAfterMove = token.substring(token.length() - 1);
			token = token.substring(0, token.length() - 2);
		}
		List<Coordinate> allowedSquares = new ArrayList<>();
		for (int i = 0; i < board.BOARD_SIZE; i++) {
			for (int j = 0; j < board.BOARD_SIZE; j++) {
				allowedSquares.add(new Coordinate(i, j));
			}
		}
		if (token.length() == 3) {
			char c = token.charAt(0);
			allowedSquares = getFilteredAllowedSquares(allowedSquares, c);
			token = token.substring(1);
		}
		if (token.length() == 4) {
			char cx = token.charAt(0);
			char cy = token.charAt(1);
			allowedSquares = getFilteredAllowedSquares(allowedSquares, cx);
			allowedSquares = getFilteredAllowedSquares(allowedSquares, cy);
			token = token.substring(2);
		}
		String move = token;
		for (Coordinate square : allowedSquares) {
			Piece pieceAt = board.get(square);
			if (pieceAt != null && pieceAt.toLetter().equals(pieceLetter) && pieceAt.getColor() == board.getTurn()) {
				Coordinate from = square;
				Coordinate to = new Coordinate(move);
				List<Coordinate> pieceMoves = board.getPieceMoves(from);
				for (Coordinate pieceMove : pieceMoves) {
					if (pieceMove.equals(to)) {
						board.move(from, to);
						if (!pieceLetter.equals(pieceLetterAfterMove)) {
							replacePiece(board, to, pieceLetterAfterMove, color);
						}
						return;
					}
				}
			}
		}
		throw new IllegalArgumentException(INVALID_PGN);
	}

	private static void replacePiece(Board board, Coordinate coordinate, String pieceLetter, Color color) {
		for (Class<? extends Piece> pieceClass : GameConstants.PIECES) {
			try {
				Piece newPiece = pieceClass.getDeclaredConstructor(Color.class).newInstance(color);
				if (newPiece.toLetter().equals(pieceLetter)) {
					board.replacePiece(coordinate.x, coordinate.y, newPiece);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static List<Coordinate> getFilteredAllowedSquares(List<Coordinate> allowedSquares, char c)
		throws IllegalArgumentException {
		if ('a' <= c && c <= 'h') {
			final int allowedX = Coordinate.parseX(c);
			return allowedSquares.stream().filter(coordinate -> coordinate.x == allowedX).toList();
		}
		if ('1' <= c && c <= '8') {
			final int allowedY = Coordinate.parseY(c);
			return allowedSquares.stream().filter(coordinate -> coordinate.y == allowedY).toList();
		}
		throw new IllegalArgumentException(INVALID_PGN);
	}
}
