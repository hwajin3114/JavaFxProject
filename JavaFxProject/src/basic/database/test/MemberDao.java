package basic.database.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MemberDao {
	CommonCode comn = new CommonCode();

	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	ResultSet rs;
	String sql = "";

	ObservableList<Member> mlist;

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

	// 수정시 값 출력
	public ObservableList<Member> getMList(int mnum) {
		sql = "select * from member where mnum = " + mnum;
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

	// 추가
	public void insertMember(Button btn) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btn.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("AddMember.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			Button btnReg = (Button) parent.lookup("#btnReg");
			btnReg.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					TextField tbName = (TextField) parent.lookup("#tbName");
					TextField tbPhone = (TextField) parent.lookup("#tbPhone");
					TextField tbBirth = (TextField) parent.lookup("#tbBirth");

					tbPhone.setPromptText("010-xxxx-xxxx");
					tbBirth.setPromptText("19xx-xx-xx");

					if (tbName.getText() == null || tbName.getText().equals("")) {
						comn.showPopup("이름을 입력하세요", btnReg);
						tbName.requestFocus();
					} else if (tbPhone.getText() == null || tbPhone.getText().equals("")) {
						comn.showPopup("전화번호를 입력하세요", btnReg);
						tbPhone.requestFocus();
					} else if (tbBirth.getText() == null || tbBirth.getText().equals("")) {
						comn.showPopup("생년월일을 입력하세요", btnReg);
						tbBirth.requestFocus();
					} else {
						Member member = new Member(tbName.getText(), tbPhone.getText(), tbBirth.getText());
						sql = "insert into member(mnum, mname, mphone, mbirth) values(mnum.NEXTVAL, \'"
								+ member.getMName() + "\', \'" + member.getMPhone() + "\', \'" + member.getMBirth()
								+ "\')";

						try {
							pstmt = conn.prepareStatement(sql);
							pstmt.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						Parent parent;
						try {
							parent = FXMLLoader.load(getClass().getResource("Main.fxml"));
							TableView<Member> memView = (TableView<Member>) parent.lookup("#memView");
							stage.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			});

			Button btnCancel = (Button) parent.lookup("#btnCancel");
			btnCancel.setOnAction(e -> stage.close());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
