package basic.control;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
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

	ObservableList<String> list = FXCollections.observableArrayList("공개", "비공개");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		comboPublic.setItems(list);
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
	}

	public ObservableList<Board> getBoardList() {
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		String user = "hr", passwd = "hr";
		Connection conn = null; // 커넥션 생성(java.sql)
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(url, user, passwd);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

		String sql = "select * from new_board order by 1";
		// observableArrayList : 정적 메소드
		ObservableList<Board> list = FXCollections.observableArrayList(); // 인스턴스 생성
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
