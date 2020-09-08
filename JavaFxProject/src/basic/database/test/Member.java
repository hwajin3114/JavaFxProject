package basic.database.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Member {
	private SimpleIntegerProperty mNum;
	private SimpleStringProperty mName;
	private SimpleStringProperty mPhone;
	private SimpleStringProperty mBirth;
	private SimpleIntegerProperty mPoint;
	private SimpleStringProperty mResYn;
	private SimpleStringProperty regDate;

	// 회원 등록
	public Member(String mName, String mPhone, String mBirth, String mResYn, String regDate) {
		super();
		this.mName = new SimpleStringProperty(mName);
		this.mPhone = new SimpleStringProperty(mPhone);
		this.mBirth = new SimpleStringProperty(mBirth);
		this.mResYn = new SimpleStringProperty(mResYn);
		this.regDate = new SimpleStringProperty(regDate);
	}

	// 회원 상세
	public Member(int mNum, String mName, String mPhone, String mBirth, int mPoint, String mResYn, String regDate) {
		super();
		this.mNum = new SimpleIntegerProperty(mNum);
		this.mName = new SimpleStringProperty(mName);
		this.mPhone = new SimpleStringProperty(mPhone);
		this.mBirth = new SimpleStringProperty(mBirth);
		this.mPoint = new SimpleIntegerProperty(mPoint);
		this.mResYn = new SimpleStringProperty(mResYn);
		this.regDate = new SimpleStringProperty(regDate);
	}

	public int getMnum() {
		return this.mNum.get();
	}

	public void setMnum(int mNum) {
		this.mNum.set(mNum);
	}

	public String getMName() {
		return this.mName.get();
	}
	
	public void setMName(String mName) {
		this.mName.set(mName);
	}

	public String getMPhone() {
		return this.mPhone.get();
	}
	
	public void setMPhone(String mPhone) {
		this.mPhone.set(mPhone);
	}

	public String getMBirth() {
		return this.mBirth.get();
	}
	
	public void setMBirth(String mBirth) {
		this.mBirth.set(mBirth);
	}

	public int getMPoint() {
		return this.mPoint.get();
	}
	
	public void setMPoint(int mPoint) {
		this.mPoint.set(mPoint);
	}

	public String getMResYn() {
		return this.mResYn.get();
	}
	
	public void setMResYn(String mResYn) {
		this.mResYn.set(mResYn);
	}

	public String getRegDate() {
		return this.regDate.get();
	}
	
	public void setRegDate(String regDate) {
		this.regDate.set(regDate);
	}
}
