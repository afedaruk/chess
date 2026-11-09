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

	public HistoryBuilder() {
		history = new ArrayList<String>();
	}

	public HistoryBuilder(List<String> history) {
		this.history = history;
	}

	public HistoryBuilder copy() {
		return new HistoryBuilder(new ArrayList<>(history));
	}

	public void add(String entry, boolean withCheck) {
		if (withCheck) {
			entry += "+";
		}
		history.add(entry);
	}

	public void addCastling() {
		history.add("O-O-O");
	}

	public String getHistory() {
		String historyString = "";
		LocalDate today = LocalDate.now();
		historyString += "[Date \"" + today.toString() + "\"]\n";
		historyString += getRawHistory();
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
