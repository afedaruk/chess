package Chess;

import Chess.Pieces.*;
import java.util.ArrayList;
import java.util.List;

public class GameConstants {

	public static final List<Color> PLAYER_COLORS = List.of(Color.White, Color.Black);
	public static final List<Class<? extends Piece>> PIECES = List.of(
		Bishop.class,
		King.class,
		Knight.class,
		Pawn.class,
		Queen.class,
		Rook.class
	);
	public static final Coordinate SHORT_CASTLING_VECTOR = new Coordinate(3, 0);
	public static final Coordinate LONG_CASTLING_VECTOR = new Coordinate(-4, 0);

	public static String getPiecesLetters() {
		StringBuilder sb = new StringBuilder();
		for (Class<? extends Piece> pieceClass : PIECES) {
			try {
				Piece pieceInstance = pieceClass.getDeclaredConstructor(Color.class).newInstance(Color.White);
				sb.append(pieceInstance.toLetter());
			} catch (Exception e) {}
		}
		return sb.toString();
	}
}
