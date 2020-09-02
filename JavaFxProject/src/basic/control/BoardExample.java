package basic.control;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

// UI : BoardControl
// Controller : BoardController
public class BoardExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// this.getClass() : BoardExample를 기준으로 resource를 가져오겠다.
		AnchorPane ap = FXMLLoader.load(this.getClass().getResource("BoardControl.fxml"));

		Scene scene = new Scene(ap);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
