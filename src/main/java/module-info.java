module chess.app {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.media;
	exports org.openjfx ;
	opens org.openjfx to javafx.fxml;
	opens org.example to javafx.fxml;
}
