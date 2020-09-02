package basic.control;

import javafx.beans.property.SimpleStringProperty;

public class Phone {
	// 문자를 감싸고 속성값을 가질수 있는 것
	SimpleStringProperty smartPhone;
	SimpleStringProperty image;

	public Phone(String smartPhone, String image) {
		// String 값을 받아서 SimpleStringProperty로 생성해서 넣어야함
		this.smartPhone = new SimpleStringProperty(smartPhone);
		this.image = new SimpleStringProperty(image);
	}

	public void setSmartPhone(String smartPhone) {
		this.smartPhone.set(smartPhone);
	}

	public String getSmartPhone() {
		return this.smartPhone.get();
	}

	public void setImage(String image) {
		this.image.set(image);
	}

	public String getImage() {
		return this.image.get();
	}
}
