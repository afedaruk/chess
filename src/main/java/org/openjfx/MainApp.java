package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root, 800, 900);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Chess");
		primaryStage.show();
	}
}
