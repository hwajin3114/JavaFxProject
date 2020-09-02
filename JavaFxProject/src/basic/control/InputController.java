package basic.control;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InputController implements Initializable {
	@FXML
	private TextField txtTitle;
	@FXML
	private PasswordField txtPassword;
	@FXML
	private ComboBox<String> comboPublic;
	@FXML
	private DatePicker dateExit;
	@FXML
	private TextArea txtContent;
	@FXML
	private Button btnReg, btnCancel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
//		txtTitle.setText("제목1");
//		comboPublic.setValue("공개");	// comboPublic 선택

		btnReg.setOnAction((ae) -> handleBtnRegAction());
	}

	public void handleBtnRegAction() {
		String title = txtTitle.getText();
		System.out.println("title : " + title);

		String password = txtPassword.getText();
		System.out.println("password : " + password);

		String strPublic = comboPublic.getValue();
		System.out.println("public : " + strPublic);

		LocalDate localDate = dateExit.getValue();
		if (localDate != null) {
			System.out.println("dateExit : " + localDate.toString());
		}

		String content = txtContent.getText();
		System.out.println("content : " + content);

		if (txtTitle.getText() == null || txtTitle.getText().equals("")) {
			showPopup("타이틀을 입력하세요");
		} else if (txtPassword.getText() == null || txtPassword.getText().equals("")) {
			showPopup("패스워드를 입력하세요");
		} else if (comboPublic.getValue() == null || comboPublic.getValue().equals("")) {
			showPopup("공개여부를 지정하세요");
		} else if (dateExit.getValue() == null) {
			showCustomDialog("날짜를 입력하세요");
		} else if (txtContent.getText() == null || txtContent.getText().equals("")) {
			showPopup("내용을 입력하세요");
		} else {
			insetData();
			Platform.exit();
		}
	}

	public void handleBtncancelAction(ActionEvent e) {
		Platform.exit();
	}

	// DB Start
	public void insetData() {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr", passwd = "hr";
		Connection conn = null; // 커넥션 생성(java.sql)
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		// 연결 객체가 필요하다
		String sql = "insert into new_board values(?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// 각 n번째 파라미터에 해당하는 입력값을 담겠다.
			pstmt.setString(1, txtTitle.getText());
			pstmt.setString(2, txtPassword.getText());
			pstmt.setString(3, comboPublic.getValue());
			pstmt.setString(4, dateExit.getValue().toString());
			pstmt.setString(5, txtContent.getText());

			int r = pstmt.executeUpdate();
			System.out.println(r + "건 입력");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	// DB End

	public void showCustomDialog(String msg) {
		// stage : window 기능
		// StageStyle.UTILITY : 흰 배경, 제목줄에 타이틀, 종료버튼만 있다
		Stage stage = new Stage(StageStyle.UTILITY);
		// 새 윈도우 열리면 뒤에 창은 선택이 안된다.
		stage.initModality(Modality.WINDOW_MODAL);
		// 어느 윈도우에 공급할것인가?
		stage.initOwner(btnReg.getScene().getWindow());

		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(400, 150);

		ImageView iv = new ImageView();
		iv.setImage(new Image("images/dialog-info.png"));
		iv.setFitWidth(50);
		iv.setFitHeight(50);
		iv.setLayoutX(15);
		iv.setLayoutY(15);
		iv.setPreserveRatio(true);

		Button btnOk = new Button("확인");
		btnOk.setLayoutX(336);
		btnOk.setLayoutY(104);
		btnOk.setOnAction((e) -> stage.close());

		Label label = new Label(msg);
		label.setLayoutX(87);
		label.setLayoutY(33);
		label.setPrefSize(290, 15);

		ap.getChildren().addAll(iv, btnOk, label);

		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.show();
	}

	public void showPopup(String msg) {
		// poppup 타이틀 등록
		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color:black;");
		hbox.setAlignment(Pos.CENTER);

		ImageView iv = new ImageView();
		iv.setImage(new Image("images/dialog-info.png"));

		Label label = new Label();
		label.setText(msg);
		label.setStyle("-fx-text-fill:yellow;");

		hbox.getChildren().addAll(iv, label);

		Popup pop = new Popup(); // 얘도 컨테이너처럼 컨트롤들이 있어야한다.
		pop.getContent().add(hbox);
		pop.setAutoHide(true);

		// primary 윈도우에 있는 컨트롤 아무거나를 기준으로 얘가 등록된 씬을 알아낼수 있다.
		// 그리고 그 씬이 소속된 윈도우 알아내기
		pop.show(btnReg.getScene().getWindow());
	}
}
