package basic.example;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RootController implements Initializable {
	@FXML
	TableView<Student> tableView;
	@FXML
	Button btnAdd, btnBarChart, btnDelete, btnNext, btnPrev;

	ObservableList<Student> list;

	Stage primaryStage; // 필드 선언
	int selectedNum = 0;

	int count = 0;
	int nextCount = 0;
	int prevCount = 0;

	String sql = "";
	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	
	String style="-fx-border-color: white; -fx-border-radius: 5; -fx-text-fill: white; -fx-background-color: #849FAD;";

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		TableColumn<Student, ?> tc = tableView.getColumns().get(0);
		tc.setCellValueFactory(new PropertyValueFactory<>("name"));

		tc = tableView.getColumns().get(1);
		tc.setCellValueFactory(new PropertyValueFactory<>("korean"));

		tc = tableView.getColumns().get(2);
		tc.setCellValueFactory(new PropertyValueFactory<>("math"));

		tc = tableView.getColumns().get(3);
		tc.setCellValueFactory(new PropertyValueFactory<>("english"));

		// 성적 저장
		list = FXCollections.observableArrayList();

		tableView.setItems(getStudentList());

		// next 버튼
		btnNext.setOnAction(e -> clickBtnNextAction());

		// prv 버튼
		btnPrev.setOnAction(e -> clickBtnPrevAction());

		// 추가 버튼
		btnAdd.setOnAction(e -> handleBtnAddAction());

		// 차트 버튼
		btnBarChart.setOnAction(e -> handleBtnChartAction());

		// 삭제 버튼
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (tableView.getSelectionModel().isEmpty()) {
					showPopup(" 목록 선택 안됨 ", btnAdd);
				} else {
					selectedNum = tableView.getSelectionModel().getSelectedItem().getSnum();
					handleBtnDeleteAction(selectedNum);
				}
			}
		});

		// 클릭시 수정 창
		tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
//				System.out.println(event);
				if (event.getClickCount() == 2) { // 2 : double click
					selectedNum = tableView.getSelectionModel().getSelectedItem().getSnum();
					handleDoubleClickAction(selectedNum);
				}

			}
		});
	} // end of initialize

	private void clickBtnNextAction() {
		tableView.getSelectionModel().selectNext();
		count = tableView.getSelectionModel().getFocusedIndex();
		sql = "select * from new_board";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			int r = pstmt.executeUpdate();
			if (nextCount == count) {
				tableView.getSelectionModel().selectFirst();
			}
			nextCount = count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void clickBtnPrevAction() {
		tableView.getSelectionModel().selectPrevious();
		int num = tableView.getSelectionModel().getFocusedIndex();
		if (prevCount == num) {
			tableView.getSelectionModel().selectLast();
		}
		prevCount = num;
	}

	// 삭제
	public void handleBtnDeleteAction(int snum) {

		System.out.println("삭제 버튼 클릭");
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		// 레이아웃
		AnchorPane ap = new AnchorPane();
		ap.setStyle("-fx-background-color: #B6CEC7");
		ap.setPrefSize(150, 80);

		Label comment = new Label("삭제하시겠습니까?");
		comment.setLayoutX(28);
		comment.setLayoutY(15);
		Button btn1 = new Button("확인");
		btn1.setLayoutX(20);
		btn1.setLayoutY(43);
		btn1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sql = "delete from student where snum = " + snum;
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				stage.close();
				tableView.setItems(getStudentList()); // refresh
			}
		});
		Button btn2 = new Button("취소");
		btn2.setLayoutX(90);
		btn2.setLayoutY(43);
		btn2.setOnAction(e -> stage.close());


		btn1.setStyle(style);
		btn2.setStyle(style);
		ap.getChildren().addAll(comment, btn1, btn2);

		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.show();
	}

	// 조회
	public ObservableList<Student> getStudentList() {
		sql = "select * from student order by 1";
		list = FXCollections.observableArrayList(); // 인스턴스 생성

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Student stu = new Student(rs.getInt("snum"), rs.getString("sName"), rs.getInt("kScore"),
						rs.getInt("mScore"), rs.getInt("eScore"));
				list.add(stu);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	// 수정
	public void handleDoubleClickAction(int snum) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		// 레이아웃
		AnchorPane ap = new AnchorPane();
		ap.setPrefSize(210, 230); // 컨테이너 크기
		ap.setStyle("-fx-background-color: #B6CEC7");

		Label lKorean, lMath, lEnglish;
		TextField tName, tKorean, tMath, tEnglish;

		lKorean = new Label("국어");
		lKorean.setLayoutX(35);
		lKorean.setLayoutY(73);

		lMath = new Label("수학");
		lMath.setLayoutX(35);
		lMath.setLayoutY(99);

		lEnglish = new Label("영어");
		lEnglish.setLayoutX(35);
		lEnglish.setLayoutY(132);

		tName = new TextField();
		tName.setPrefWidth(110);
		tName.setLayoutX(72);
		tName.setLayoutY(30);
//		tName.setText(name);
		tName.setEditable(false); // 수정 불가

		tKorean = new TextField();
		tKorean.setPrefWidth(110);
		tKorean.setLayoutX(72);
		tKorean.setLayoutY(69);

		tMath = new TextField();
		tMath.setPrefWidth(110);
		tMath.setLayoutX(72);
		tMath.setLayoutY(95);

		tEnglish = new TextField();
		tEnglish.setPrefWidth(110);
		tEnglish.setLayoutX(72);
		tEnglish.setLayoutY(128);

		Button btnUpdate = new Button("수정");
		btnUpdate.setLayoutX(85);
		btnUpdate.setLayoutY(184);
		btnUpdate.setStyle(style);
		btnUpdate.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getSnum() == snum) {
						Student student = new Student(snum, tName.getText(), Integer.parseInt(tKorean.getText()),
								Integer.parseInt(tMath.getText()), Integer.parseInt(tEnglish.getText()));
						updateStudent(student);
					}
				}
				tableView.setItems(getStudentList()); // refresh
				stage.close();
			}
		});

		// 번호 기준으로 점수 가져오기
		for (Student stu : list) {
			if (stu.getSnum() == snum) {
				tName.setText(String.valueOf(stu.getName()));
				tMath.setText(String.valueOf(stu.getMath()));
				tKorean.setText(String.valueOf(stu.getKorean()));
				tEnglish.setText(String.valueOf(stu.getEnglish()));
			}
		}

		ap.getChildren().addAll(btnUpdate, tName, tKorean, tMath, tEnglish, lKorean, lMath, lEnglish);

		Scene scene = new Scene(ap);
		stage.setScene(scene);
		stage.show();
	}

	// 추가 | AddForm.fxml로 넘어가야함
	public void handleBtnAddAction() {
		// 윈도우 스타일
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		// btnAdd이 있는 윈도우를 지정하고싶다.
		stage.initOwner(btnAdd.getScene().getWindow());

		try {
			// Parent : 모든 컨테이너의 상위
			Parent parent = FXMLLoader.load(getClass().getResource("AddForm.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			// Add 화면의 컨트롤 사용하기
			Button btnFormAdd = (Button) parent.lookup("#btnFormAdd");
			btnFormAdd.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// lookup : 입력한 id 값으로 찾아오겠다.
					TextField txtName = (TextField) parent.lookup("#txtName");
					TextField txtKorean = (TextField) parent.lookup("#txtKorean");
					TextField txtMath = (TextField) parent.lookup("#txtMath");
					TextField txtEnglish = (TextField) parent.lookup("#txtEnglish");

					if (txtName.getText() == null || txtName.getText().equals("")) {
						showPopup("이름을 입력하세요", btnFormAdd);
						txtName.requestFocus();
					} else if (txtKorean.getText() == null || txtKorean.getText().equals("")) {
						showPopup("국어 점수를 입력하세요", btnFormAdd);
						txtKorean.requestFocus();
					} else if (txtMath.getText() == null || txtMath.getText().equals("")) {
						showPopup("수학 점수를 입력하세요", btnFormAdd);
						txtMath.requestFocus();
					} else if (txtEnglish.getText() == null || txtEnglish.getText().equals("")) {
						showPopup("영어 점수를 입력하세요", btnFormAdd);
						txtEnglish.requestFocus();
					} else {
						Student student = new Student(txtName.getText(), Integer.parseInt(txtKorean.getText()),
								Integer.parseInt(txtMath.getText()), Integer.parseInt(txtEnglish.getText()));
//						System.out.println("등록되었습니다.");
						insertStudent(student);

						tableView.setItems(getStudentList()); // refresh
						stage.close();
					}
				}
			});

			Button btnFormClear = (Button) parent.lookup("#btnFormClear");
			btnFormClear.setOnAction(e -> {
				TextField txtName = (TextField) parent.lookup("#txtName");
				TextField txtKorean = (TextField) parent.lookup("#txtKorean");
				TextField txtMath = (TextField) parent.lookup("#txtMath");
				TextField txtEnglish = (TextField) parent.lookup("#txtEnglish");

				txtName.clear();
				txtKorean.clear();
				txtMath.clear();
				txtEnglish.clear();
			});

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 추가
	public void insertStudent(Student student) {
		sql = "insert into student values(snum.NEXTVAL, \'" + student.getName() + "\', " + student.getKorean() + ", "
				+ student.getMath() + ", " + student.getEnglish() + ")";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 수정
	public void updateStudent(Student student) {
		System.out.println("updatenum : " + student.getSnum());
		sql = "update student set kScore = ?, mScore = ?, eScore = ? where snum =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, student.getKorean());
			pstmt.setInt(2, student.getMath());
			pstmt.setInt(3, student.getEnglish());
			pstmt.setInt(4, student.getSnum());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void handleBtnChartAction() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		// AppMain에 있는 primaryStage를 컨트롤러에 보내줘서 이렇게 가능
		stage.initOwner(primaryStage);

		try {
			Parent chart = FXMLLoader.load(getClass().getResource("BarChart.fxml"));

			Scene scene = new Scene(chart);
			stage.setScene(scene);
			stage.show();

			// 차트를 가지고 와서 시리즈를 추가해야함
			// XYChart.Series<T, U> ObservableList<XYChart.Data<T, U> -> Series에 Data 모음을 담고
			// 그걸 차트로 나타낸다.
			BarChart barChart = (BarChart) chart.lookup("#barChart");

			barChart.setTitle("학생 별 성적");

			XYChart.Series<String, Integer> seriesK = new XYChart.Series<String, Integer>();
			seriesK.setName("국어");

			ObservableList<XYChart.Data<String, Integer>> koreanList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				koreanList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getKorean()));
			}

			XYChart.Series<String, Integer> seriesM = new XYChart.Series<String, Integer>();
			seriesM.setName("수학");

			ObservableList<XYChart.Data<String, Integer>> mathList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				mathList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getMath()));
			}

			XYChart.Series<String, Integer> seriesE = new XYChart.Series<String, Integer>();
			seriesE.setName("영어");

			ObservableList<XYChart.Data<String, Integer>> englishList = FXCollections.observableArrayList();

			for (int i = 0; i < list.size(); i++) {
				englishList.add(new XYChart.Data<>(list.get(i).getName(), list.get(i).getEnglish()));
			}

			seriesK.setData(koreanList);
			seriesM.setData(mathList);
			seriesE.setData(englishList);

			barChart.getData().addAll(seriesK, seriesM, seriesE);

			Button btnClose = (Button) chart.lookup("#btnClose");
			btnClose.setOnAction(e -> stage.close());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showPopup(String msg, Button btn) {
		// poppup 타이틀 등록
		HBox hbox = new HBox();
		hbox.setStyle("-fx-background-color:#ffdc7c;");
		hbox.setAlignment(Pos.CENTER);

		ImageView iv = new ImageView();
		iv.setImage(new Image("images/dialog-info.png"));

		Label label = new Label();
		label.setText(msg);

		hbox.getChildren().addAll(iv, label);

		Popup pop = new Popup(); // 얘도 컨테이너처럼 컨트롤들이 있어야한다.
		pop.getContent().add(hbox);
		pop.setAutoHide(true);

		// primary 윈도우에 있는 컨트롤 아무거나를 기준으로 얘가 등록된 씬을 알아낼수 있다.
		// 그리고 그 씬이 소속된 윈도우 알아내기
		pop.show(btn.getScene().getWindow());
	}

	// 입력값 체크
	public boolean checkInsert(TextField txt) {
		boolean result = true;
		if (!txt.getText().matches("^[0-9]+$")) {
			result = false;
		} else {
			result = true;
		}
		System.out.println("result : " + result);
		return result;
	}
}
