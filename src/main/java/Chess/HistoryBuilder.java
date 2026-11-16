package Chess;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HistoryBuilder {

	private final List<String> history;
	private BoardState boardState;
	private Color winner;

	public HistoryBuilder() {
		history = new ArrayList<String>();
	}

	public HistoryBuilder(List<String> history) {
		this.history = history;
	}

	public HistoryBuilder copy() {
		return new HistoryBuilder(new ArrayList<>(history));
	}

	public void setWinner(Color winner) {
		this.winner = winner;
		boardState = BoardState.CHECKMATE;
	}

	public void setDraw() {
		boardState = BoardState.DRAW;
	}

	public void add(String entry, boolean withCheck) {
		if (withCheck) {
			entry += "+";
		}
		history.add(entry);
	}

	public void addCastling(boolean extended) {
		if (extended) {
			history.add("O-O-O");
		} else {
			history.add("O-O");
		}
	}

	public String getMatchResultString() {
		if (boardState == BoardState.CHECKMATE) {
			if (winner == Color.White) {
				return "[1-0]";
			} else {
				return "[0-1]";
			}
		} else {
			return "[1/2-1/2]";
		}
	}

	public String getHistory() {
		String historyString = "";
		historyString += "[Event \"Local Game\"]\n";
		historyString += "[Site \"Local\"]\n";
		historyString += "[Round \"1\"]\n";
		LocalDate today = LocalDate.now();
		historyString += "[Date \"" + today.toString() + "\"]\n";
		historyString += "[Result \"" + getMatchResultString() + "\"]\n";
		historyString += getRawHistory();
		historyString += "\n";
		historyString += getMatchResultString();
		return historyString;
	}

	public String getRawHistory() {
		String historyString = "";
		for (int entryNumber = 0; entryNumber < history.size(); entryNumber++) {
			if (entryNumber % 2 == 0) historyString += (entryNumber / 2 + 1) + ". ";
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
}
