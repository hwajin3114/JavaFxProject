package basic.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ViewController implements Initializable {
	@FXML
	private ListView<String> listView;
	@FXML
	private TableView<Phone> tableView;
	@FXML
	private ImageView imageView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// ObservableList값을 매개값으로 받는다 -> 변경되는 속성값을 받는다.
		// observableArrayList : FXCollections의 정적 타입의 메소드라서 new ~ 이렇게 선언 안해줘도 된다.
		ObservableList<String> list = FXCollections.observableArrayList();
		list.add("갤럭시S1");
		list.add("갤럭시S2");
		list.add("갤럭시S3");
		list.add("갤럭시S4");
		list.add("갤럭시S5");
		list.add("갤럭시S6");
		list.add("갤럭시S7");
		listView.setItems(list);

		// 값이 하나씩 바뀔때 마다 수행
		// 리스너 등록. 선택된 listView에 따른 tableView
		listView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			// observable : index 값
			// 선택될 때마다 newValue, oldValue 바뀐다.
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				// 선택된 newValue 위치를 선택. number 값을 int로 변환
				// 이거는 선택된 값에 따라 리스트도 선택되게 하는것
				tableView.getSelectionModel().select(newValue.intValue());

				// 선택될때 스크롤도 함께 움직이도록
				tableView.scrollTo(newValue.intValue());
			}
		});

		ObservableList<Phone> phoneList = FXCollections.observableArrayList();
		phoneList.add(new Phone("갤럭시S1", "phone01.png"));
		phoneList.add(new Phone("갤럭시S2", "phone02.png"));
		phoneList.add(new Phone("갤럭시S3", "phone03.png"));
		phoneList.add(new Phone("갤럭시S4", "phone04.png"));
		phoneList.add(new Phone("갤럭시S5", "phone05.png"));
		phoneList.add(new Phone("갤럭시S6", "phone06.png"));
		phoneList.add(new Phone("갤럭시S7", "phone07.png"));

		// fxml에 생성된 tableView의 어느 칼럼과 연결할지 안해줬음
		// 리스트에 있는 값 == TableView의 칼럼과 매칭 시켜주자
		// <Phone, ?> : Phone 클래스의 모든 타입을 가져오기 위해 ?해줌
		// + fxml 파일 말고 Controller에서 컬럼을 추가할 수 있다.
		TableColumn<Phone, ?> tcSmartPhone = tableView.getColumns().get(0);
		// PropertyValueFactory라는 속성값을 가지고 있는 요소
		// setCellValueFactory 타입을 맞춰주기 위해 위에 tcSmartPhone 형변환 해줌
		tcSmartPhone.setCellValueFactory(new PropertyValueFactory<>("smartPhone"));

		TableColumn<Phone, ?> tcImage = tableView.getColumns().get(1);
		tcImage.setCellValueFactory(new PropertyValueFactory<>("image")); // 필드명 입력 "xx"

		tcSmartPhone.setStyle("-fx-alignment: CENTER;");
		tcImage.setStyle("-fx-alignment: CENTER;");

		tableView.setItems(phoneList);

		// 리스너 등록. 선택된 tableView에 따른 이미지
		// getSelectionModel : 선택한 Phone 타입의 ROW 값을 가져오겠다. <- TableViw<Phone>
		// selectedItemProperty : 내가 선택한 값의 item 값을 가져오겠다.
		// selectedIndexProperty : 선택한 값의 인덱스값을 가져오겠다.
		tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Phone>() {

			@Override
			public void changed(ObservableValue<? extends Phone> observable, Phone oldValue, Phone newValue) {
				imageView.setImage(new Image("images/" + newValue.getImage().toString()));
			}
		});
	}

	public void handleBtnOkAction(ActionEvent e) {
		String item = listView.getSelectionModel().getSelectedItem(); // 선택된 행 데이터 얻기
		System.out.println("ListView 스마트폰 : " + item);
		Phone phone = tableView.getSelectionModel().getSelectedItem();
		System.out.println("TableView 스마트폰 : " + phone.getSmartPhone());
	}

	public void handleBtnCancelAction(ActionEvent e) {
		Platform.exit();
	}
}
