package Chess;

import Chess.Pieces.Piece;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PGNBuilder extends PGNBase {

	private final List<String> history;
	private BoardState boardState;
	private Color winner;

	public PGNBuilder() {
		history = new ArrayList<String>();
	}

	public PGNBuilder(List<String> history) {
		this.history = history;
	}

	public PGNBuilder copy() {
		return new PGNBuilder(new ArrayList<>(history));
	}

	public void setWinner(Color winner) {
		this.winner = winner;
		boardState = BoardState.CHECKMATE;
		int lastMove = history.size() - 1;
		int lastCheckSymbol = history.get(lastMove).length() - 1;
		history.set(lastMove, history.get(lastMove).substring(0, lastCheckSymbol) + CHECKMATE_SYMBOL);
	}

	public void setDraw() {
		boardState = BoardState.DRAW;
	}

	public void add(String entry, boolean withCheck) {
		if (withCheck) {
			entry += CHECK_SYMBOL;
		}
		history.add(entry);
	}

	public void addCastling(boolean isLong) {
		if (isLong) {
			history.add(LONG_CASTLING);
		} else {
			history.add(SHORT_CASTLING);
		}
	}

	public String getMatchResultString() {
		if (boardState == BoardState.CHECKMATE) {
			if (winner == Color.White) {
				return RESULT_WHITE_WIN;
			} else {
				return RESULT_BLACK_WIN;
			}
		}
		if (boardState == BoardState.DRAW) {
			return RESULT_DRAW;
		}
		return RESULT_UNFINISHED;
	}

	public String getHistory() {
		String historyString = "";
		historyString += getTag("Event", "Local Game");
		historyString += getTag("Site", "Local PC");
		historyString += getTag("Round", "1");
		historyString += getTag("Date", LocalDate.now().toString());
		historyString += getTag("Result", getMatchResultString());
		historyString += getRawHistory();
		historyString += "\n";
		historyString += getMatchResultString();
		return historyString;
	}

	public String getRawHistory() {
		String historyString = "";
		for (int entryNumber = 0; entryNumber < history.size(); entryNumber++) {
			if (entryNumber % 2 == 0) historyString += (entryNumber / 2 + 1) + MOVE_NUMBER_SYMBOL + " ";
			historyString += history.get(entryNumber) + " ";
		}
		return historyString;
	}

	public void downloadHistory() {
		try {
			String userHome = System.getProperty("user.home");
			Path downloadsPath = Paths.get(userHome, "Downloads", "game.pgn");
			Files.createDirectories(downloadsPath.getParent());
			Files.writeString(
				downloadsPath,
				getHistory(),
				StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING
			);
			System.out.println("Successfully wrote to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public void promotePiece(Piece chosenPiece) {
		int lastMove = history.size() - 1;
		history.set(lastMove, history.get(lastMove) + PROMOTION_SYMBOL + chosenPiece.toLetter());
	}

	public String getTag(String tag, String value) {
		return TAG_OPENING_SYMBOL + tag + " " + TAG_VALUE_SYMBOL + value + TAG_VALUE_SYMBOL + TAG_CLOSING_SYMBOL + "\n";
	}
}
