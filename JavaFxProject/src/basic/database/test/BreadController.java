package basic.database.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BreadController implements Initializable {
	@FXML
	TableView<Bread> boardView;
	@FXML
	TableView<Member> memView;
	@FXML
	TextField txtName, txtPrice, txtRegDate, txtContent;
	@FXML
	ImageView img;
	@FXML
	Button btnNext, btnPrev, btnModify, btnCancel, btnAdd, btnDelete;

	ObservableList<Bread> list;
	ObservableList<Member> mlist;
	File selected;
	int selectedNum = 0;

	int count = 0, num = 0;
	int nextCount = 0;
	int prevCount = 0;
	String sql = "";
	PreparedStatement pstmt;
	Connection conn = ConnectionDB.getDB();

	String style = "-fx-border-color: white; -fx-border-radius: 5; -fx-text-fill: white; -fx-background-color: #849FAD;";

	Stage primaryStage;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		BreadTable();
		MemberTable();

		btnCancel.setOnAction(e -> Platform.exit());

		// next 버튼
		btnNext.setOnAction(e -> clickBtnNextAction());

		// prv 버튼
		btnPrev.setOnAction(e -> clickBtnPrevAction());

		// 빵 add 버튼
		btnAdd.setOnAction(e -> insertBread());

		// 빵 modify 버튼
		btnModify.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (boardView.getSelectionModel().isEmpty()) {
					System.out.println("선택 안됨");
				} else {
					selectedNum = boardView.getSelectionModel().getSelectedItem().getBnum();
					System.out.println("selectnum : " + selectedNum);
					clickBtnModifyAction(selectedNum);
				}
				boardView.setItems(getBoardList()); // refresh
			}
		});

		// 빵 delete
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (boardView.getSelectionModel().isEmpty()) {
					showPopup(" 목록을 선택 해 주세요 ", btnDelete);
				} else {
					selectedNum = boardView.getSelectionModel().getSelectedItem().getBnum();
					deleteBread(selectedNum);
				}
			}
		});

		// 회원 add
		// 회원 modify
		// 회원 delete
	}

	// 빵 테이블
	public void BreadTable() {
		TableColumn<Bread, String> tcName = new TableColumn<>("빵이름");
		tcName.setCellValueFactory(new PropertyValueFactory<>("bName"));
		tcName.setPrefWidth(80);
		boardView.getColumns().add(tcName);

		TableColumn<Bread, String> tcPrice = new TableColumn<>("가격");
		tcPrice.setCellValueFactory(new PropertyValueFactory<>("bPrice"));
		tcPrice.setPrefWidth(80);
		boardView.getColumns().add(tcPrice);

		TableColumn<Bread, String> tcContent = new TableColumn<>("설명");
		tcContent.setCellValueFactory(new PropertyValueFactory<>("content"));
		tcContent.setPrefWidth(80);
		boardView.getColumns().add(tcContent);

		boardView.setItems(getBoardList());

		// 값을 선택할 때마다 리스너
		boardView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bread>() {

			@Override
			public void changed(ObservableValue<? extends Bread> observable, Bread oldValue, Bread newValue) {
				// 상세 정보를 하나씩 가져오겠다.
				txtName.setText(newValue.getBName());
				txtName.setEditable(false); // 수정 불가
				txtPrice.setText(String.valueOf(newValue.getBPrice()));
				txtRegDate.setText(newValue.getRegDate());
				txtContent.setText(newValue.getContent());

				try {
					if (newValue.getBImg() == null) {
						img.setImage(new Image("@../../images/apeach.png"));
					} else {
						// 파일 읽어오기
						FileInputStream fis = new FileInputStream(newValue.getBImg());
						BufferedInputStream bis = new BufferedInputStream(fis);
						// 이미지 생성하기
						Image image = new Image(bis);
						img.setFitHeight(100);
						img.setFitWidth(300);

						// 이미지 띄우기
						img.setImage(image);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 회원 테이블
	public void MemberTable() {
		TableColumn<Member, String> tcName = new TableColumn<>("회원명");
		tcName.setCellValueFactory(new PropertyValueFactory<>("mName"));
		tcName.setPrefWidth(80);
		memView.getColumns().add(tcName);

		TableColumn<Member, String> tcPhone = new TableColumn<>("전화번호");
		tcPhone.setCellValueFactory(new PropertyValueFactory<>("mPhone"));
		memView.getColumns().add(tcPhone);

		TableColumn<Member, String> tcBirth = new TableColumn<>("생년월일");
		tcBirth.setCellValueFactory(new PropertyValueFactory<>("mBirth"));
		tcBirth.setPrefWidth(100);
		memView.getColumns().add(tcBirth);

		TableColumn<Member, String> tcPoint = new TableColumn<>("포인트");
		tcPoint.setCellValueFactory(new PropertyValueFactory<>("mPoint"));
		tcPoint.setPrefWidth(90);
		memView.getColumns().add(tcPoint);

		TableColumn<Member, String> tcResYn = new TableColumn<>("예약유무");
		tcResYn.setCellValueFactory(new PropertyValueFactory<>("mResYn"));
		tcResYn.setPrefWidth(80);
		memView.getColumns().add(tcResYn);

		memView.setItems(getMemberList());
	}

	// 추가
	private void insertBread() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAdd.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("AddBread.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			Button regImg = (Button) parent.lookup("#regImg");
			ImageView imgView = (ImageView) parent.lookup("#imgView");
			regImg.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					FileChooser choose = new FileChooser();
					choose.setTitle("이미지 선택");
					choose.setInitialDirectory(new File("C:/"));

					// 확장자 제한
					ExtensionFilter imgType = new ExtensionFilter("image file", "*.jpg", "*.gif", "*.png");
					choose.getExtensionFilters().add(imgType);

					selected = choose.showOpenDialog(null);
					System.out.println(selected);

					try {
						// 파일 읽어오기
						FileInputStream fis = new FileInputStream(selected);
						BufferedInputStream bis = new BufferedInputStream(fis);
						// 이미지 생성하기
						Image img = new Image(bis);

						imgView.setFitHeight(150);
						imgView.setFitWidth(300);
						// 이미지 띄우기
						imgView.setImage(img);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
			});

			Button breadAdd = (Button) parent.lookup("#breadAdd");
			breadAdd.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					TextField txtName = (TextField) parent.lookup("#tName");
					TextField txtPrice = (TextField) parent.lookup("#tPrice");
					TextField txtContent = (TextField) parent.lookup("#tContent");

					System.out.println(txtName.getText());
					if (txtName.getText() == null || txtName.getText().equals("")) {
						showPopup("빵 이름을 입력하세요", breadAdd);
						txtName.requestFocus();
					} else if (txtPrice.getText() == null || txtPrice.getText().equals("")) {
						showPopup("가격 입력하세요", breadAdd);
						txtPrice.requestFocus();
					} else if (txtContent.getText() == null || txtContent.getText().equals("")) {
						showPopup("설명을 입력하세요", breadAdd);
						txtContent.requestFocus();
					} else {
						Bread bread = new Bread(txtName.getText(), Integer.parseInt(txtPrice.getText()),
								selected.toString(), txtContent.getText());

						sql = "insert into bread values(bnum.NEXTVAL, \'" + bread.getBName() + "\', "
								+ bread.getBPrice() + ", \'" + bread.getBImg() + "\', \'" + bread.getContent()
								+ "\', sysdate)";

						try {
							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						boardView.setItems(getBoardList()); // refresh
						stage.close();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteBread(int bnum) {
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
				sql = "delete from bread where bnum = " + bnum;
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				stage.close();
				boardView.setItems(getBoardList()); // refresh
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
		num = boardView.getSelectionModel().getFocusedIndex();
		System.out.println("num> " + num);
		System.out.println("prevCount> " + prevCount);
		if (prevCount == num) {
			boardView.getSelectionModel().selectLast();
		}
		prevCount = num;
	}

	private void clickBtnModifyAction(int bnum) {
		System.out.println("clickBtnModifyAction : " + bnum);
		// 이미지 넣기.. 흠
		Bread bread = new Bread(bnum, Integer.parseInt(txtPrice.getText()), null, txtContent.getText(),
				txtRegDate.getText());
		sql = "update bread set bprice = ?, content = ?, regdate = ? where bnum = ?";
		try {
			System.out.println("try");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bread.getBPrice());
			pstmt.setString(2, bread.getContent());
			pstmt.setString(3, bread.getRegDate());
			pstmt.setInt(4, bnum);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 조회
	public ObservableList<Bread> getBoardList() {
		sql = "select * from bread order by 1";
		list = FXCollections.observableArrayList();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Bread bread = new Bread(rs.getInt("bnum"), rs.getString("bname"), rs.getInt("bprice"),
						rs.getString("bimg"), rs.getString("content"), rs.getString("regdate"));
				list.add(bread);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 회원 조회
	public ObservableList<Member> getMemberList() {
		sql = "select * from member order by 1";
		mlist = FXCollections.observableArrayList();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Member member = new Member(rs.getInt("mnum"), rs.getString("mname"), rs.getString("mphone"),
						rs.getString("mbirth"), rs.getInt("mpoint"), rs.getString("mresyn"), rs.getString("regdate"));
				mlist.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return mlist;
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
}
