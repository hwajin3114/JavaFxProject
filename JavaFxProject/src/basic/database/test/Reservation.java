package basic.database.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reservation {
	private SimpleIntegerProperty rnum;
	private SimpleStringProperty memName;
	private SimpleStringProperty breadName;
	private SimpleIntegerProperty breadCnt;
	private SimpleStringProperty pickUpDate;
	private SimpleStringProperty regDate;

	public Reservation(int rnum, String memName, String breadName, int breadCnt,
			String pickUpDate, String regDate) {
		super();
		this.rnum = new SimpleIntegerProperty(rnum);
		this.memName = new SimpleStringProperty(memName);
		this.breadName = new SimpleStringProperty(breadName);
		this.breadCnt = new SimpleIntegerProperty(breadCnt);
		this.pickUpDate = new SimpleStringProperty(pickUpDate);
		this.regDate = new SimpleStringProperty(regDate);
	}

	public Reservation(String memName, String breadName, int breadCnt,
			String pickUpDate) {
		super();
		this.memName = new SimpleStringProperty(memName);
		this.breadName = new SimpleStringProperty(breadName);
		this.breadCnt = new SimpleIntegerProperty(breadCnt);
		this.pickUpDate = new SimpleStringProperty(pickUpDate);
	}

	public int getRnum() {
		return this.rnum.get();
	}
	
	public void setRnum(int rnum) {
		this.rnum.set(rnum);
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

	public String getRegDate() {
		return this.regDate.get();
	}
	
	public void setRegDate(String regDate) {
		this.regDate.set(regDate);
	}
}
