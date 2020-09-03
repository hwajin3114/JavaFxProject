package basic.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

// UI : Root.fxml, AppForm.fxml(추가), BarChart.fxml(차트)
// Control : RootController.java
public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 지금까지는 컨테이너 생성하면서 load 정적 메소드 사용했었음
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Root.fxml"));
		BorderPane root = loader.load();

		// controller에서 stage.initOwner하는 방법 말고 이 방법도있다.
		// 이 파일이 컨트롤러랑 같은 패키지에 있어서 (basic.example.)RootController 생략가능
		RootController controller = loader.getController(); // Root.fxml에 연결된 컨트롤러를 찾는다.
		controller.setPrimaryStage(primaryStage); // 컨트롤러에 생성한 메소드. AppMain에 있는 primaryStage를 컨트롤에 보내기 위함.

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setResizable(false);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
