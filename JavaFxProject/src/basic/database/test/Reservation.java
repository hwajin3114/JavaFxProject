package basic.database.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reservation {
	private SimpleStringProperty memName;
	private SimpleStringProperty breadName;
	private SimpleIntegerProperty breadCnt;
	private SimpleStringProperty pickUpDate;

	public Reservation(String memName, String breadName, int breadCnt,
			String pickUpDate) {
		super();
		this.memName = new SimpleStringProperty(memName);
		this.breadName = new SimpleStringProperty(breadName);
		this.breadCnt = new SimpleIntegerProperty(breadCnt);
		this.pickUpDate = new SimpleStringProperty(pickUpDate);
	}

	public String getMemName() {
		return this.memName.get();
	}
	
	public void setMemName(String memName) {
		this.memName.set(memName);
	}

	public String getBreadName() {
		return this.breadName.get();
	}
	
	public void setBreadName(String breadName) {
		this.breadName.set(breadName);
	}

	public int getBreadCnt() {
		return this.breadCnt.get();
	}
	
	public void setBreadCnt(int breadCnt) {
		this.breadCnt.set(breadCnt);
	}

	public String getPickUpDate() {
		return this.pickUpDate.get();
	}
	
	public void setPickUpDate(String pickUpDate) {
		this.pickUpDate.set(pickUpDate);
	}
}
