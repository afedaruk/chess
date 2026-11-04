package org.example;

import Chess.Board;
import Chess.BoardState;
import Chess.Coordinate;
import Chess.Pieces.Piece;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainController {

	@FXML
	public GridPane boardGrid;

	@FXML
	private Label myLabel;

	@FXML
	private void handleButtonClick() {
		myLabel.setText("Button Was Clicked!");
	}

	StackPane[][] squares = new StackPane[8][8];
	boolean[][] doesSquareHaveMoveMark = new boolean[8][8];
	private final int BOARD_SIZE = 8;
	Board board = new Board();
	Coordinate selectedPieceCoordinate;

	@FXML
	private void initialize() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				createSquare(i, j);
				doesSquareHaveMoveMark[i][j] = false;
			}
		}
		setSquareLabel(0, 0, "a1");
		for (int i = 1; i < BOARD_SIZE; i++) {
			setSquareLabel(0, i, "" + (i + 1));
		}
		for (int i = 1; i < BOARD_SIZE; i++) {
			setSquareLabel(i, 0, "" + (char) ('a' + i));
		}
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				setSquarePiece(i, j, board.get(i, j));
			}
		}
	}

	private void handleSquareClicked(int x, int y) {
		if (doesSquareHaveMoveMark[x][y]) {
			board.moveAndUpdateState(selectedPieceCoordinate, new Coordinate(x, y));
			updateBoard();
			BoardState boardState = board.getState();
			switch (boardState) {
				case CHECKMATE:
					System.out.println("CHECKMATED. " + board.getWinner() + " WON");
					break;
				case STALEMATE:
					System.out.println("STALEMATE.");
					break;
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
				doesSquareHaveMoveMark[i][j] = false;
			}
		}
		var moves = board.getPieceMoves(x, y);
		System.out.println(moves);
		for (var move : moves) {
			setSquareMoveMark(move.x, move.y);
		}
	}

	private void clearMoveMarks() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				squares[i][j].getChildren().removeIf(child -> (child.getStyleClass().contains("MoveMark")));
				doesSquareHaveMoveMark[i][j] = false;
				setSquarePiece(i, j, board.get(i, j));
			}
		}
	}

	private void updateBoard() {
		clearMoveMarks();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				squares[i][j].getChildren().removeIf(child -> (child.getStyleClass().contains("Piece")));
				doesSquareHaveMoveMark[i][j] = false;
				setSquarePiece(i, j, board.get(i, j));
			}
		}
	}

	private void setSquareMoveMark(int x, int y) {
		Label l = new Label("○");
		l.setTextFill(Color.GRAY);
		l.setStyle("-fx-font-size: 40;");
		l.getStyleClass().add("MoveMark");
		squares[x][y].getChildren().add(l);
		doesSquareHaveMoveMark[x][y] = true;
	}

	private void createSquare(int i, int j) {
		Rectangle r = new Rectangle(0, 0, 100, 100);
		StackPane cell = new StackPane();
		cell.getChildren().add(r);
		cell.setOnMouseClicked(event -> {
			handleSquareClicked(i, BOARD_SIZE - j - 1);
		});
		if ((i + j) % 2 == 0) r.setFill(Color.LIGHTGRAY); else r.setFill(Color.DARKBLUE);
		GridPane.setColumnIndex(cell, i);
		GridPane.setRowIndex(cell, j);
		squares[i][BOARD_SIZE - j - 1] = cell;
		boardGrid.getChildren().add(cell);
	}

	private void setSquarePiece(int x, int y, Piece piece) {
		if (piece == null) return;
		Label l = new Label(piece.toSymbol());
		l.setStyle("-fx-font-size: 60;");
		l.setTextFill(Color.WHITE);
		l.getStyleClass().add("Piece");
		squares[x][y].getChildren().add(l);
	}

	private void setSquareLabel(int x, int y, String label) {
		Label l = new Label(label);
		l.setTextFill(Color.GRAY);
		squares[x][y].getChildren().add(l);
	}
}
