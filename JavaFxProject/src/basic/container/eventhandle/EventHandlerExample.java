package basic.container.eventhandle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class EventHandlerExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane root = FXMLLoader.load(getClass().getResource("root.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
