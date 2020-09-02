package basic.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ButtonController implements Initializable {
	@FXML
	private CheckBox chk1;
	@FXML
	private CheckBox chk2;
	@FXML
	private ImageView checkImageView;
	@FXML
	private ToggleGroup group;
	@FXML
	private ImageView radioImageView;
	@FXML
	private Button btnExit;
	@FXML
	private RadioButton rad1, rad2, rad3;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ToggleGroup의 selectedToggle 속성 감시 리스너 설정
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				// newValue.getUserData().toString() : 선택된 컨트롤의 userData 속성값
				// Image 인스턴스에 넣을 이미지 저장
				Image image = new Image(getClass()
						.getResource("../../images/" + newValue.getUserData().toString() + ".png").toString());
				radioImageView.setImage(image);
			}
		});

		rad1.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("rad1 clicked");
			}
		});

		// 람다식
		rad2.setOnMouseClicked((a) -> System.out.println("rad2 clicked"));
		rad3.setSelected(true); // setSelected : 선택여부
		
		chk1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				handleChkAction();
			}
		});
		
		chk2.setOnAction((e) -> handleChkAction());

		btnExit.setOnAction((event) -> Platform.exit());
	}

	public void handleChkAction() {
		String imgName = "";
		if (chk1.isSelected() && chk2.isSelected()) {
			imgName = "geek-glasses-hair.gif";
		} else if (chk1.isSelected()) {
			imgName = "geek-glasses.gif";
		} else if (chk2.isSelected()) {
			imgName = "geek-hair.gif";
		} else {
			imgName = "geek.gif";
		}
		checkImageView.setImage(new Image("images/" + imgName));
	}

	public void handleBtnExitAction(ActionEvent e) {
		Platform.exit();
	}
}
