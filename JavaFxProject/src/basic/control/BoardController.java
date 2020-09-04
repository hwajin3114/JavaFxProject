package basic.control;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import basic.common.ConnectionDB;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BoardController implements Initializable {
	@FXML
	TableView<Board> boardView;
	@FXML
	TextField txtTitle;
	@FXML
	ComboBox<String> comboPublic;
	@FXML
	TextField txtExitDate;
	@FXML
	TextArea txtContent;

	@FXML
	Button btnNext, btnPrev, btnModify;

	ObservableList<String> Plist = FXCollections.observableArrayList("공개", "비공개");
	ObservableList<Board> list;

	int count = 0;
	int nextCount = 0;
	int prevCount = 0;
	String sql = "";
	Connection conn = ConnectionDB.getDB();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboPublic.setItems(Plist);
		comboPublic.getSelectionModel().selectFirst();

		// TableColumn<Board, String> : Board 값을 받아와서 String 값으로 반환(화면에 내보내는 값)
		TableColumn<Board, String> tcTitle = new TableColumn<>("제목");
		tcTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
		tcTitle.setPrefWidth(80);
		// 칼럼 생성할 때 마다 view에 추가해 준다.
		boardView.getColumns().add(tcTitle);

		TableColumn<Board, String> tcPublicity = new TableColumn<>("공개여부");
		tcPublicity.setCellValueFactory(new PropertyValueFactory<>("publicity"));
		tcPublicity.setPrefWidth(80);
		boardView.getColumns().add(tcPublicity);

		// 컬럼을 지정하기 위해 알맞은 데이터값을 가져와야함
		boardView.setItems(getBoardList());

		// 값을 선택할 때마다 리스너
		boardView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Board>() {

			@Override
			public void changed(ObservableValue<? extends Board> observable, Board oldValue, Board newValue) {
				// 상세 정보를 하나씩 가져오겠다.
				txtTitle.setText(newValue.getTitle());
				comboPublic.setValue(newValue.getPublicity());
				txtExitDate.setText(newValue.getExitDate());
				txtContent.setText(newValue.getContent());
			}
		});

		// next 버튼
		btnNext.setOnAction(e -> clickBtnNextAction());

		// prv 버튼
		btnPrev.setOnAction(e -> clickBtnPrevAction());

		// modify 버튼
		btnModify.setOnAction(e -> clickBtnModifyAction());
	}

	private void clickBtnNextAction() {
		boardView.getSelectionModel().selectNext();
		count = boardView.getSelectionModel().getFocusedIndex();
		sql = "select * from new_board";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			int r = pstmt.executeUpdate();
			if (nextCount == count) {
				boardView.getSelectionModel().selectFirst();
			}
			nextCount = count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void clickBtnPrevAction() {
		boardView.getSelectionModel().selectPrevious();
		int num = boardView.getSelectionModel().getFocusedIndex();
		System.out.println("num> "+num);
		System.out.println("prevCount> "+prevCount);
		if (prevCount == num) {
			boardView.getSelectionModel().selectLast();
		}
		prevCount = num;
	}

	private void clickBtnModifyAction() {
		if (boardView.getSelectionModel().isEmpty()) {
			System.out.println("선택안됨");
		} else {
			System.out.println("선택됨");
			txtTitle.setEditable(false); // 수정 불가
//			System.out.println(boardView.getSelectionModel().);
			System.out.println(txtTitle.getText());
		}
	}

	public ObservableList<Board> getBoardList() {
		sql = "select * from new_board order by 1";
		// observableArrayList : 정적 메소드
		list = FXCollections.observableArrayList(); // 인스턴스 생성
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery(); // 쿼리 결과를 ResultSet에 담는다.
			while (rs.next()) { // rs에서 값을 하나씩 가져온다. 값 있으면 true | 없으면 false
				Board board = new Board(rs.getString("title"), rs.getString("password"), rs.getString("publicity"),
						rs.getString("exit_date"), rs.getString("content")); // db 칼럼명
				list.add(board);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	public void btnCancel() {
		Platform.exit();
	}
}
