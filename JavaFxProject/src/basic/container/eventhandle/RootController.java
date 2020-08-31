package basic.container.eventhandle;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;

// Initializable 인터페이스를 구현(implements)하도록 해준다.
public class RootController implements Initializable {
	// FXML 파일에서 지정해준 id 값
	@FXML Label label;
	@FXML Slider slider;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// valueProperty : slider 값이 변할때마다 속성값 리턴
		// addListener(xx) : valueProperty 값이 바뀔때마다 xx를 해달라
		// 매개값(xx)으로 ChangeListener를 구현하는 익명 객체 구현
		slider.valueProperty().addListener(new ChangeListener<Number>() {

			// ObservableValue(인터페이스) : 이 값이 바뀔때마다 해당하는 값을 읽어올수 있도록하는?
			// observable : 감시가 가능한 변수
			// 슬라이더가 움직일때 마다 label의 폰트 크기를 변경(startValue ~ endValue)
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number startValue, Number endValue) {
				label.setFont(new Font(endValue.doubleValue()));
			}
		});
	}
}
