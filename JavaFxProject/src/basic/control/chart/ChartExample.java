package basic.control.chart;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// UI : Chart.fxml
// Control : CartController.java
public class ChartExample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// this.getClass() : ChartExample를 기준으로 resource를 가져오겠다.
		HBox hb = FXMLLoader.load(this.getClass().getResource("Chart.fxml"));

		Scene scene = new Scene(hb);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}