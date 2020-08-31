package basic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// 이제 HBox에 Root.fxml 내용을 불러와야함
		// FxmlMain이랑 Root.fxml이 같은위치라서 getClass().getResource("Root.fxml")로 읽어옴.(다른위치였으면 상대경로 입력)
		// HBox, VBox 상위 : Parent
		Parent root = FXMLLoader.load(getClass().getResource("FlowRoot.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show(); // 화면 띄워주기
		primaryStage.setTitle("FXML 화면");
	}

	public static void main(String[] args) {
		launch(args);
	}
}
