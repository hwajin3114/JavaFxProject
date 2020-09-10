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
import java.util.List;
import java.util.ResourceBundle;

import basic.common.CommonCode;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BreadController implements Initializable {
	@FXML
	TableView<Bread> breadView;
	@FXML
	TableView<Member> memView;
	@FXML // 빵
	TextField txtName, txtPrice, txtRegDate, txtContent;
	@FXML
	ImageView img;
	@FXML // 빵
	Button btnNext, btnPrev, btnModify, btnCancel, btnAdd, btnDelete, btnImg, btnReserve;
	@FXML // 회원
	Button btnModify1, btnAdd1, btnDelete1;

	ObservableList<Member> mlist; // 수정값 담기용
	ObservableList<String> blist = FXCollections.observableArrayList();
	ObservableList<Integer> clist = FXCollections.observableArrayList(1, 2, 3, 4, 5);

	BreadDao bDao = new BreadDao();
	MemberDao mDao = new MemberDao();
	ReserveDao rDao = new ReserveDao();
	CommonCode comn = new CommonCode();

	File selected;
	String oldImg;

	int selectedNum = 0;

	int count = 0, num = 0;
	int nextCount = 0;
	int prevCount = 0;

	String sql = "";
	PreparedStatement pstmt;
	Connection conn = ConnectionDB.getDB();

	String style = "-fx-border-color: white; -fx-border-radius: 5; -fx-text-fill: white; -fx-background-color: #849FAD;";

	Stage primaryStage;

	TableView<Reservation> reserveView;

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		BreadTable();
		MemberTable();

//		btnImg.setDisable(true);
		btnImg.setVisible(false);

		// cancel 버튼
		btnCancel.setOnAction(e -> Platform.exit());

		// next 버튼
		btnNext.setOnAction(e -> clickBtnNextAction());

		// prv 버튼
		btnPrev.setOnAction(e -> clickBtnPrevAction());

		// 예약 버튼
		btnReserve.setOnAction(e -> reserveLogin());

		// 빵 add 버튼
		btnAdd.setOnAction(e -> {
			selected = bDao.insertBread(btnAdd);
			breadView.setItems(bDao.getBoardList());
		});

		// 빵 modify 버튼
		btnModify.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (breadView.getSelectionModel().isEmpty()) {
					comn.showPopup(" 목록을 선택 해 주세요 ", btnModify);
				} else {
					selectedNum = breadView.getSelectionModel().getSelectedItem().getBnum();
					clickBtnModifyAction(selectedNum);
				}
				breadView.setItems(bDao.getBoardList()); // refresh
			}
		});

		// 빵 delete
		btnDelete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (breadView.getSelectionModel().isEmpty()) {
					comn.showPopup(" 목록을 선택 해 주세요 ", btnDelete);
				} else {
					selectedNum = breadView.getSelectionModel().getSelectedItem().getBnum();
					deleteBread(selectedNum);
				}
			}
		});

		// 회원 add
		btnAdd1.setOnAction(e -> {
			mDao.insertMember(btnAdd1);
			memView.setItems(mDao.getMemberList());
		});

		// 회원 modify
		btnModify1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (memView.getSelectionModel().isEmpty()) {
					comn.showPopup("회원 선택 안됨", btnModify1);
				} else {
					selectedNum = memView.getSelectionModel().getSelectedItem().getMnum();
					updateMember(selectedNum);
				}
			}
		});

		// 회원 delete
		btnDelete1.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (memView.getSelectionModel().isEmpty()) {
					comn.showPopup(" 목록을 선택 해 주세요 ", btnDelete1);
				} else {
					selectedNum = memView.getSelectionModel().getSelectedItem().getMnum();
					deleteMember(selectedNum);
				}
			}
		});
	}

	// 빵 테이블
	public void BreadTable() {
		TableColumn<Bread, String> tcName = new TableColumn<>("빵이름");
		tcName.setCellValueFactory(new PropertyValueFactory<>("bName"));
		tcName.setPrefWidth(80);
		breadView.getColumns().add(tcName);

		TableColumn<Bread, String> tcPrice = new TableColumn<>("가격");
		tcPrice.setCellValueFactory(new PropertyValueFactory<>("bPrice"));
		tcPrice.setPrefWidth(80);
		breadView.getColumns().add(tcPrice);

		TableColumn<Bread, String> tcContent = new TableColumn<>("설명");
		tcContent.setCellValueFactory(new PropertyValueFactory<>("content"));
		tcContent.setPrefWidth(80);
		breadView.getColumns().add(tcContent);

		breadView.setItems(bDao.getBoardList());

		// 값을 선택할 때마다 리스너
		breadView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Bread>() {
			@Override
			public void changed(ObservableValue<? extends Bread> observable, Bread oldValue, Bread newValue) {
				btnImg.setVisible(true);
				// 상세 정보를 하나씩 가져오겠다.
				if (newValue != null) {
					txtName.setText(newValue.getBName());
					txtName.setEditable(false); // 수정 불가
					txtPrice.setText(String.valueOf(newValue.getBPrice()));
					txtRegDate.setText(newValue.getRegDate().substring(0, 10));
					txtContent.setText(newValue.getContent());

					// 빵 image 버튼
					btnImg.setOnAction(e -> fileUploader());

					try {
						// 파일 읽어오기
						FileInputStream fis = new FileInputStream(newValue.getBImg());
						BufferedInputStream bis = new BufferedInputStream(fis);
						// 이미지 생성하기
						Image image = new Image(bis);
						img.setFitHeight(100);
						img.setFitWidth(300);

						// 이미지 띄우기
						img.setImage(image);
						oldImg = newValue.getBImg();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
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

		memView.setItems(mDao.getMemberList());
	}

	// 예약 테이블
	public void ReserveTable(String name) {
		TableColumn<Reservation, String> tcMName = new TableColumn<>("예약자명");
		tcMName.setCellValueFactory(new PropertyValueFactory<>("memName"));
		tcMName.setPrefWidth(80);
		reserveView.getColumns().add(tcMName);
		TableColumn<Reservation, String> tcBName = new TableColumn<>("빵 이름");
		tcBName.setCellValueFactory(new PropertyValueFactory<>("breadName"));
		tcBName.setPrefWidth(100);
		reserveView.getColumns().add(tcBName);
		TableColumn<Reservation, String> tcCount = new TableColumn<>("갯수");
		tcCount.setCellValueFactory(new PropertyValueFactory<>("breadCnt"));
		tcCount.setPrefWidth(80);
		reserveView.getColumns().add(tcCount);
		TableColumn<Reservation, String> tcPick = new TableColumn<>("픽업일");
		tcPick.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
		tcPick.setPrefWidth(100);
		reserveView.getColumns().add(tcPick);

		reserveView.setItems(rDao.getReserveList(name));
	}

	// 예약
	public void reserveLogin() {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAdd.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("Login.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			Button btnLogin = (Button) parent.lookup("#btnLogin");
			btnLogin.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent arg0) {
					String where;
					TextField name = (TextField) parent.lookup("#name");
					TextField phone = (TextField) parent.lookup("#phone");

					if (name.getText() == null || name.getText().equals("")) {
						comn.showPopup("이름을 입력하세요", btnLogin);
						name.requestFocus();
					} else if (phone.getText() == null || phone.getText().equals("")) {
						comn.showPopup("연락처를 입력하세요", btnLogin);
						phone.requestFocus();
					} else {
						if (name.getText().equals("master") && phone.getText().equals("master")) {
							sql = "select * from member";
						} else {
							sql = "select * from member where mname = ? and mphone = ?";
						}

						try {
							pstmt = conn.prepareStatement(sql);
							if (!name.getText().equals("master") && !phone.getText().equals("master")) {
								pstmt.setString(1, name.getText());
								pstmt.setString(2, phone.getText());
							}
							int i = pstmt.executeUpdate();
							System.out.println("i : " + i);
							if (i == 1) {
//								comn.showPopup("로그인 성공", btnLogin);
								stage.close();
								reserveBread(name.getText(), phone.getText());
							} else if (i == 0) {
								comn.showPopup("로그인 실패", btnLogin);
							} else { // 마스터 로그인
								stage.close();
								reserveBread(name.getText(), phone.getText());
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 예약
	@SuppressWarnings("unchecked")
	public void reserveBread(String name, String phone) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(primaryStage);

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("Reservation.fxml"));

			reserveView = (TableView<Reservation>) parent.lookup("#reserveView");

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();
			ReserveTable(name);

			Button rRegBtn = (Button) parent.lookup("#rRegBtn");
			if (name.equals("master")) {
				comn.showPopup("관리자로 로그인되었습니다", rRegBtn);
			} else {
				comn.showPopup(name + "님 예약창입니다.", rRegBtn);
			}
			rRegBtn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					Stage stage = new Stage(StageStyle.UTILITY);
					stage.initModality(Modality.WINDOW_MODAL);
					stage.initOwner(primaryStage);

					try {
						Parent parent = FXMLLoader.load(getClass().getResource("AddReserve.fxml"));

						Scene scene = new Scene(parent);
						stage.setScene(scene);
						stage.show();

						TextField name = (TextField) parent.lookup("#name");
						ComboBox<String> cBread = new ComboBox<String>();
						sql = "select bname from bread order by 1";
						try {
							pstmt = conn.prepareStatement(sql);
							ResultSet rs = pstmt.executeQuery();
							while (rs.next()) {
								System.out.println("blist : " + rs.getString("bname"));
								blist.add(rs.getString("bname"));
							}
							cBread.setItems(blist);
							cBread.getSelectionModel().selectFirst();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						ComboBox<Integer> cCount = new ComboBox<Integer>();
						cCount.setItems(clist);
						cCount.getSelectionModel().selectFirst();

						TextField pickDate = (TextField) parent.lookup("#pickDate");

						Button btnReserve = (Button) parent.lookup("#btnReserve");
						btnReserve.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								System.out.println("예약 확인 버튼");
								if (name.getText() == null || name.getText().equals("")) {
									comn.showPopup("예약할 빵 이름을 입력하세요", btnReserve);
									name.requestFocus();
								} else if (pickDate.getText() == null || pickDate.getText().equals("")) {
									comn.showPopup("픽업 날짜를 입력하세요", btnReserve);
									pickDate.requestFocus();
								} else {
									Reservation reserve = new Reservation(name.getText(), cBread.getValue().toString(),
											cCount.getValue(), pickDate.getText());
									sql = "insert into reserve values(rnum.NEXTVAL, \'" + reserve.getMemName()
											+ "\', \'" + reserve.getBreadName() + "\', " + reserve.getBreadCnt()
											+ ", sysdate, \'" + reserve.getPickUpDate() + "\')";

									try {
										pstmt = conn.prepareStatement(sql);
//										pstmt.executeUpdate();
									} catch (SQLException e) {
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
			});

			Button rDelBtn = (Button) parent.lookup("#rDelBtn");
			// 예약 delete
			rDelBtn.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (reserveView.getSelectionModel().isEmpty()) {
						comn.showPopup(" 목록을 선택 해 주세요 ", rDelBtn);
					} else {
						selectedNum = reserveView.getSelectionModel().getSelectedItem().getRnum();
						deleteReserve(selectedNum);
					}
				}
			});
			Button rCancelBtn = (Button) parent.lookup("#rCancelBtn");
			rCancelBtn.setOnAction(e -> stage.close());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 삭제
	public void deleteReserve(int rnum) {
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
				sql = "delete from reserve where rnum = " + rnum;
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				stage.close();
				breadView.setItems(bDao.getBoardList()); // refresh
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

	// 수정
	private void clickBtnModifyAction(int bnum) {
		String imgUrl;
		if (selected != null) {
			imgUrl = selected.toString();
		} else {
			System.out.println("oldImg : " + oldImg);
			imgUrl = oldImg;
		}
		Bread bread = new Bread(bnum, Integer.parseInt(txtPrice.getText()), imgUrl, txtContent.getText(),
				txtRegDate.getText());
		sql = "update bread set bprice = ?, content = ?, bimg = ? where bnum = ?";
		try {
			System.out.println("try");
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bread.getBPrice());
			pstmt.setString(2, bread.getContent());
			pstmt.setString(3, imgUrl);
			pstmt.setInt(4, bnum);
			pstmt.executeUpdate();

			comn.showPopup("수정 되었습니다!", btnModify);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 삭제
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
				breadView.setItems(bDao.getBoardList()); // refresh
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

	// 수정
	public void updateMember(int mnum) {
		Stage stage = new Stage(StageStyle.UTILITY);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(btnAdd.getScene().getWindow());

		try {
			Parent parent = FXMLLoader.load(getClass().getResource("ModifyMember.fxml"));

			Scene scene = new Scene(parent);
			stage.setScene(scene);
			stage.show();

			TextField name = (TextField) parent.lookup("#name");
			TextField phone = (TextField) parent.lookup("#phone");
			TextField birth = (TextField) parent.lookup("#birth");
			TextField point = (TextField) parent.lookup("#point");
			TextField resYn = (TextField) parent.lookup("#resYn");
			TextField regDate = (TextField) parent.lookup("#regDate");

			List<Member> mlist = mDao.getMList(mnum);

			name.setText(mlist.get(0).getMName().toString());
			phone.setText(mlist.get(0).getMPhone().toString());
			birth.setText(mlist.get(0).getMBirth().toString());
			point.setText(String.valueOf(mlist.get(0).getMPoint()));
			resYn.setText(mlist.get(0).getMResYn().toString());
			regDate.setText(mlist.get(0).getRegDate().toString());

			name.setEditable(false);
			point.setEditable(false);
			resYn.setEditable(false);
			regDate.setEditable(false); // 수정 불가

			Button btnModi = (Button) parent.lookup("#btnModi");
			btnModi.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (phone.getText() == null || phone.getText().equals("")) {
						comn.showPopup("전화번호를 입력하세요", btnModi);
						phone.requestFocus();
					} else if (birth.getText() == null || birth.getText().equals("")) {
						comn.showPopup("생년월일을 입력하세요", btnModi);
						birth.requestFocus();
					} else {
						Member member = new Member(mnum, phone.getText(), birth.getText());

						sql = "update member set mphone = ?, mbirth = ?	where mnum = ?";

						try {
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, member.getMPhone());
							pstmt.setString(2, member.getMBirth());
							pstmt.setInt(3, mnum);
							pstmt.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}

						memView.setItems(mDao.getMemberList()); // refresh
						stage.close();
					}
				}

			});

			Button btnDel = (Button) parent.lookup("#btnDel");
			btnDel.setOnAction(e -> deleteMember(mnum));

			Button btnCancel = (Button) parent.lookup("#btnCancel");
			btnCancel.setOnAction(e -> stage.close());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 삭제
	public void deleteMember(int mnum) {
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
				sql = "delete from member where mnum = " + mnum;
				try {
					pstmt = conn.prepareStatement(sql);
					pstmt.executeUpdate();

				} catch (SQLException e) {
					e.printStackTrace();
				}
				stage.close();
				memView.setItems(mDao.getMemberList()); // refresh
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
		breadView.getSelectionModel().selectNext();
		count = breadView.getSelectionModel().getFocusedIndex();
		sql = "select * from bread";

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
			if (nextCount == count) {
				breadView.getSelectionModel().selectFirst();
			}
			nextCount = count;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void clickBtnPrevAction() {
		breadView.getSelectionModel().selectPrevious();
		num = breadView.getSelectionModel().getFocusedIndex();
		System.out.println("num> " + num);
		System.out.println("prevCount> " + prevCount);
		if (prevCount == num) {
			breadView.getSelectionModel().selectLast();
		}
		prevCount = num;
	}

	public void fileUploader() {
		FileChooser choose = new FileChooser();
		choose.setInitialDirectory(new File("C:/"));

		// 확장자 제한
		ExtensionFilter imgType = new ExtensionFilter("image file", "*.jpg", "*.gif", "*.png");
		choose.getExtensionFilters().add(imgType);

		selected = choose.showOpenDialog(null);
		try {
			if (selected != null) {
				// 2. 스트림 지정
				FileInputStream fis = new FileInputStream(selected);
				BufferedInputStream bis = new BufferedInputStream(fis);

				// 3. 읽어오기
				Image readimg = new Image(bis);
				img.setImage(readimg);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("selected >> " + selected);
	}
}
