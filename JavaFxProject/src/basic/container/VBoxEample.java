package basic.container;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxEample extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox root = new VBox();
		root.setPadding(new Insets(10, 10, 10, 10));

		ImageView iv = new ImageView();
		iv.setFitWidth(200);
		iv.setPreserveRatio(true);
		iv.setImage(new Image("/images/fruit1.jpg"));

		HBox hbox = new HBox();
		// Pos : 열거형 데이터
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(20);

		// HBOX 속 컨트롤들 S
		Button btnPrev = new Button();
		btnPrev.setText("이전");
		Button btnNext = new Button("다음");
		// Priority : 열거형 데이터
		HBox.setHgrow(btnNext, Priority.ALWAYS);
		btnNext.setMaxWidth(Double.MAX_VALUE);

		hbox.getChildren().add(btnPrev);
		hbox.getChildren().add(btnNext);
		VBox.setMargin(hbox, new Insets(10));
		// HBOX 속 컨트롤들 E

		// 이벤트 핸들러를 해당 컨트롤(btnNext)에 등록
		// EventHandler(인터페이스) 익명 구현 객체 생성
		// ActionEvent라는 제네릭 타입을 받는다.
		// 람다표현식으로 수정해보았다. -> Functional Interface라서 가능
//		btnNext.setOnAction((event) -> {
//			System.out.println(event.getSource());
//		});

		btnNext.setOnAction(new EventHandler<ActionEvent>() {
			int count = 1;

			@Override
			public void handle(ActionEvent event) {
				if (count == 7)
					count = 1;
				iv.setImage(new Image("/images/fruit" + count++ + ".jpg"));
			}
		});

		btnPrev.setOnAction(new EventHandler<ActionEvent>() {
			int count = 6;

			@Override
			public void handle(ActionEvent event) {
				if (count == 1)
					count = 6;
				iv.setImage(new Image("/images/fruit" + count-- + ".jpg"));
			}
		});

		root.getChildren().add(iv);
		root.getChildren().add(hbox);

		Scene scene = new Scene(root); // 씬 생성해서 컨테이너 담아주고
		primaryStage.setScene(scene); // 스테이지에 씬 담아주고
		primaryStage.show(); // 스테이지 show
		primaryStage.setTitle("VBox 예제");
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
