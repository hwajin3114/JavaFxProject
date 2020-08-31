package basic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox hbox = new HBox(); // 컨테이너
		hbox.setPadding(new Insets(10)); // 컨테이너와 컨트롤 사이 여백
		hbox.setSpacing(10); // 컨트롤 간의 수평 간격 설정

		TextField tFeild = new TextField(); // 컨트롤
		tFeild.setPrefWidth(200); // text 필드의 가로 지정. Double 타입

		Button btn = new Button(); // 컨트롤
		btn.setText("확인"); // String 타입 매개값

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit(); // 버튼에 이벤트 추가
			}
		});

		// hbox 컨테이너에 컨트롤 추가
		hbox.getChildren().add(tFeild);
		hbox.getChildren().add(btn);

		// Scene 생성시 컨테이너를 매개값으로 넣어줘야함
		Scene scene = new Scene(hbox);

		primaryStage.setScene(scene); // stage에 씬 추가
		primaryStage.show();
		primaryStage.setTitle("AppMain 화면");
	}

	public static void main(String[] args) {
		Application.launch(args); // lauch라는 메소드를 이용해서 Application에 있는 메소드들 쭈욱 호출
	}
}
