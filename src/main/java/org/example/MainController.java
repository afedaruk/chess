package org.example;

import Chess.Board;
import Chess.BoardState;
import Chess.Coordinate;
import Chess.Pieces.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainController {

	@FXML
	public GridPane boardGrid;

	@FXML
	public HBox promotionChoiceContainer;

	public VBox overlayCard;
	public Label gameOverText;
	public Label gameLog;

	@FXML
	private CheckBox checkBox;

	boolean shouldFlipBoard = false;
	boolean isBoardFlipped = false;

	private final Color EVEN_SQUARE_COLOR = Color.LIGHTGRAY;
	private final Color ODD_SQUARE_COLOR = Color.DARKBLUE;

	public void handleButtonClick(ActionEvent event) {
		shouldFlipBoard = checkBox.isSelected();
		updateBoard();
	}

	StackPane[][] squares = new StackPane[8][8];
	boolean[][] squareHasMoveMark = new boolean[8][8];
	private final int BOARD_SIZE = 8;
	Board board;
	Coordinate selectedPieceCoordinate;

	@FXML
	private void initialize() {
		startNewGame();
	}

	public void startNewGame() {
		board = new Board();
		board.print();
		updateBoard();
		hideOverlay();
	}

	private void handleSquareClicked(int x, int y) {
		if (isBoardFlipped) {
			y = BOARD_SIZE - 1 - y;
		}
		if (squareHasMoveMark[x][y]) {
			if ((y == BOARD_SIZE - 1 || y == 0) && board.get(selectedPieceCoordinate) instanceof Pawn) {
				final int finalX = x;
				final int finalY = y;
				createPromotionOptionsAndWaitAsync()
					.thenAcceptAsync(
						selectedPiece -> {
							moveSelectedPieceTo(finalX, finalY);
							board.replacePiece(finalX, finalY, selectedPiece);
							updateBoard();
						},
						Platform::runLater
					);
			} else {
				moveSelectedPieceTo(x, y);
			}
			return;
		}
		if (board.get(x, y) == null) {
			clearMoveMarks();
			return;
		}
		clearMoveMarks();
		selectedPieceCoordinate = new Coordinate(x, y);
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				squareHasMoveMark[i][j] = false;
			}
		}
		var moves = board.getPieceMoves(x, y);
		System.out.println(moves);
		for (var move : moves) {
			setSquareMoveMark(move.x, move.y);
		}
	}

	private void hidePromotionOptions() {
		System.out.println("Hiding promotion options");
		promotionChoiceContainer.getChildren().clear();
	}

	private CompletableFuture<Piece> createPromotionOptionsAndWaitAsync() {
		CompletableFuture<Piece> future = new CompletableFuture<>();
		displayPromotionChoices(future);
		return future;
	}

	private void displayPromotionChoices(CompletableFuture<Piece> future) {
		System.out.println("PROMOTION CHOICE");
		Chess.Color promotionColor = board.getTurn();
		List<Piece> promotionOptions = List.of(
			new Bishop(promotionColor),
			new Rook(promotionColor),
			new Queen(promotionColor),
			new Knight(promotionColor)
		);
		for (Piece promotionOption : promotionOptions) {
			Rectangle r = new Rectangle(0, 0, 100, 100);
			StackPane cell = new StackPane();
			Label l = new Label(promotionOption.toSymbol());
			r.setFill(Color.DARKGREEN);
			l.setStyle("-fx-font-size: 60;");
			if (promotionOption.getColor() == Chess.Color.White) l.setTextFill(Color.WHITE); else l.setTextFill(
				Color.BLACK
			);
			l.getStyleClass().add("Piece");
			cell.getChildren().add(r);
			cell.getChildren().add(l);
			cell.setOnMouseClicked(event -> {
				future.complete(promotionOption);
				System.out.println(promotionOption.toSymbol());
			});
			promotionChoiceContainer.getChildren().add(cell);
		}
	}

	private void moveSelectedPieceTo(int x, int y) {
		board.moveAndUpdateState(selectedPieceCoordinate, new Coordinate(x, y));
		updateBoard();
		runPostMoveChecks(board);
		return;
	}

	private void runPostMoveChecks(Board board) {
		BoardState boardState = board.getState();
		Chess.Color colorToMove = board.getTurn();
		Coordinate kingCoordinate = board.getKingCoordinate(board.getTurn());
		int kingX = kingCoordinate.x;
		int kingY = kingCoordinate.y;
		switch (boardState) {
			case CHECKMATE:
				System.out.println("CHECKMATED. " + board.getWinner() + " WON");
				createCheckmateEffect(kingX, kingY);
				break;
			case DRAW:
				System.out.println("STALEMATE.");
				break;
			case PLAYING:
				if (board.isUnderCheck(colorToMove)) {
					createCheckEffect(kingX, kingY);
				}
				break;
		}
	}

	private void createCheckEffect(int x, int y) {
		System.out.println("Creating check effect");
		Label kingSymbol = (Label) squares[x][y].getChildren()
			.stream()
			.filter(child -> {
				return child.getStyleClass().contains("Piece");
			})
			.findFirst()
			.orElse(null);
		kingSymbol.setTextFill(Color.RED);
	}

	private void createCheckmateEffect(int x, int y) {
		System.out.println("Creating check effect");
		Label kingSymbol = (Label) squares[x][y].getChildren()
			.stream()
			.filter(child -> {
				return child.getStyleClass().contains("Piece");
			})
			.findFirst()
			.orElse(null);
		kingSymbol.setRotate(180);
		kingSymbol.setTextFill(Color.RED);
	}

	private void clearMoveMarks() {
		hidePromotionOptions();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				squares[i][j].getChildren().removeIf(child -> (child.getStyleClass().contains("MoveMark")));
				squareHasMoveMark[i][j] = false;
			}
		}
	}

	private void updateBoard() {
		isBoardFlipped = board.getTurn() == Chess.Color.Black && shouldFlipBoard;
		updateBaseBoard();
		updatePieces();
		updateGameStateVisuals();
		updateLog();
	}

	private void updateBaseBoard() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				createSquare(i, j);
				squareHasMoveMark[i][j] = false;
			}
		}
		if (!isBoardFlipped) {
			setSquareLabel(0, 0, "a1");
			for (int i = 1; i < BOARD_SIZE; i++) {
				setSquareLabel(0, i, "" + (i + 1));
			}
			for (int i = 1; i < BOARD_SIZE; i++) {
				setSquareLabel(i, 0, "" + (char) ('a' + i));
			}
		} else {
			setSquareLabel(0, BOARD_SIZE - 1, "a1");
			for (int i = 0; i < BOARD_SIZE; i++) {
				setSquareLabel(0, BOARD_SIZE - 1 - i, "" + (i + 1));
			}
			for (int i = 1; i < BOARD_SIZE; i++) {
				setSquareLabel(i, BOARD_SIZE - 1, "" + (char) ('a' + i));
			}
		}
	}

	private void updatePieces() {
		clearMoveMarks();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				squareHasMoveMark[i][j] = false;
				if (!isBoardFlipped) setSquarePiece(i, j, board.get(i, j)); else setSquarePiece(
					i,
					j,
					board.get(i, BOARD_SIZE - j - 1)
				);
			}
		}
	}

	private void setSquareMoveMark(int x, int y) {
		squareHasMoveMark[x][y] = true;
		if (isBoardFlipped) {
			y = BOARD_SIZE - 1 - y;
		}
		Label l = new Label("○");
		l.setTextFill(Color.GRAY);
		l.setStyle("-fx-font-size: 40;");
		l.getStyleClass().add("MoveMark");
		squares[x][y].getChildren().add(l);
	}

	private void createSquare(int i, int j) {
		Rectangle r = new Rectangle(0, 0, 100, 100);
		StackPane cell = new StackPane();
		cell.getChildren().add(r);
		cell.setOnMouseClicked(event -> {
			handleSquareClicked(i, BOARD_SIZE - j - 1);
		});
		if ((i + j) % 2 == 0) r.setFill(EVEN_SQUARE_COLOR); else r.setFill(ODD_SQUARE_COLOR);
		GridPane.setColumnIndex(cell, i);
		GridPane.setRowIndex(cell, j);
		squares[i][BOARD_SIZE - j - 1] = cell;
		boardGrid.getChildren().add(cell);
	}

	private void setSquarePiece(int x, int y, Piece piece) {
		squares[x][y].getChildren().removeIf(child -> (child.getStyleClass().contains("Piece")));
		if (piece == null) {
			return;
		}

		Label l = new Label(piece.toSymbol());
		l.setStyle("-fx-font-size: 60;");
		if (piece.getColor() == Chess.Color.White) l.setTextFill(Color.WHITE); else l.setTextFill(Color.BLACK);
		l.getStyleClass().add("Piece");
		squares[x][y].getChildren().add(l);
	}

	private void setSquareLabel(int x, int y, String label) {
		Label l = new Label(label);
		l.setTextFill(Color.GRAY);
		squares[x][y].getChildren().add(l);
	}

	private void updateLog() {
		gameLog.setText(board.historyBuilder.getRawHistory());
	}

	@FXML
	private void printHistory() {
		System.out.println(board.historyBuilder.getHistory());
	}

	@FXML
	private void downloadHistory() {
		board.historyBuilder.downloadHistory();
	}

	public void resign() {
		board.resign();
		updateBoard();
	}

	private void updateGameStateVisuals() {
		if (board.getState() == BoardState.CHECKMATE) {
			gameOverText.setText(board.getTurn().getOpposite().toString() + " won!");
			overlayCard.setVisible(true);
		}
		if (board.getState() == BoardState.DRAW) {
			gameOverText.setText("Draw");
			overlayCard.setVisible(true);
		}
	}

	@FXML
	private void hideOverlay() {
		overlayCard.setVisible(false);
	}
}
