package Chess;

import Chess.Pieces.Piece;
import java.util.List;

public class PGNLoader {

	public static Board LoadPGN(String PGN) throws IllegalArgumentException {
		Board board = new Board();
		String token = "";
		boolean readingTags = false;
		for (char symbol : PGN.toCharArray()) {
			if (symbol == '[') {
				readingTags = true;
				continue;
			}
			if (symbol == ']') {
				readingTags = false;
				continue;
			}
			if (readingTags) {
				continue;
			}
			if (symbol == '#' || symbol == '*') {
				break; // game is over
			}
			if (symbol == '+') {
				continue;
			}
			if (symbol == '.') {
				token = "";
				continue;
			}
			if (token.isEmpty()) {
				System.out.println(
					"Does " + GameConstants.getPiecesLetters() + " contain" + Character.toString(symbol)
				);
				if (GameConstants.getPiecesLetters().contains(Character.toString(symbol))) {
					token = Character.toString(symbol);
					continue;
				}
			}
			if (symbol == ' ') {
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
		System.out.println("REVIEWING TOKEN " + token);
		if (token.isEmpty()) {
			return;
		}
		boolean isNumberMarker = token.endsWith(".");
		if (isNumberMarker) {
			return;
		}
		boolean isPiecePawn = !GameConstants.getPiecesLetters().contains(token.substring(0, 1));
		if (isPiecePawn && token.length() == 2 || token.length() == 3) {
			movePiece(board, token);
		}
	}

	private static void movePiece(Board board, String token) throws IllegalArgumentException {
		String pieceLetter = token.substring(0, 1);
		String move = token.substring(1);
		if (!GameConstants.getPiecesLetters().contains(pieceLetter)) {
			// The piece is a pawn
			pieceLetter = "";
			move = token;
		}
		for (int i = 0; i < board.BOARD_SIZE; i++) {
			for (int j = 0; j < board.BOARD_SIZE; j++) {
				Piece pieceAt = board.get(i, j);
				if (
					pieceAt != null && pieceAt.toLetter().equals(pieceLetter) && pieceAt.getColor() == board.getTurn()
				) {
					Coordinate from = new Coordinate(i, j);
					Coordinate to = new Coordinate(move);
					System.out.println(from + " -> " + to);
					List<Coordinate> pieceMoves = board.getPieceMoves(from);
					for (Coordinate pieceMove : pieceMoves) {
						if (pieceMove.equals(to)) {
							board.move(from, to);
							return;
						}
					}
				}
			}
		}
		System.out.println("no move found for " + pieceLetter + " to " + move);
		throw new IllegalArgumentException("Invalid PGN");
	}
}
