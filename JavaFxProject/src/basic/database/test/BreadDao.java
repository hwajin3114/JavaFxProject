package basic.database.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import basic.common.CommonCode;
import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.FileChooser.ExtensionFilter;

public class BreadDao {
	CommonCode comn = new CommonCode();

	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	ResultSet rs;
	String sql = "";

	String checkInt = "^[0-9]+$";
	File selected;

	ObservableList<Bread> list;

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

	// 추가
	public File insertBread(Button btn) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btn.getScene().getWindow());

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
						if (selected != null) {
							// 파일 읽어오기
							FileInputStream fis = new FileInputStream(selected);
							BufferedInputStream bis = new BufferedInputStream(fis);
							// 이미지 생성하기
							Image img = new Image(bis);

							imgView.setFitHeight(150);
							imgView.setFitWidth(300);
							// 이미지 띄우기
							imgView.setImage(img);
						}
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
						comn.showPopup("빵 이름을 입력하세요", breadAdd);
						txtName.requestFocus();
					} else if (txtPrice.getText() == null || txtPrice.getText().equals("")) {
						comn.showPopup("가격 입력하세요", breadAdd);
						txtPrice.requestFocus();
					} else if (txtContent.getText() == null || txtContent.getText().equals("")) {
						comn.showPopup("설명을 입력하세요", breadAdd);
						txtContent.requestFocus();
					} else if (selected == null || selected.equals("")) {
						comn.showPopup("이미지를 선택하세요", breadAdd);
					} else {
						if (!Pattern.matches(checkInt, txtPrice.getText())) {
							System.out.println("test > "+txtPrice.getText());
							comn.showPopup("숫자만 입력하세요", breadAdd);
							txtPrice.clear();
							txtPrice.requestFocus();
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
							Parent parent;
							try {
								parent = FXMLLoader.load(getClass().getResource("Main.fxml"));
								TableView<Bread> boardView = (TableView<Bread>) parent.lookup("#reserveView");
								boardView.setItems(getBoardList()); // refresh
								stage.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return selected;
	}
}
