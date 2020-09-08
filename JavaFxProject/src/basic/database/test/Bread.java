package basic.database.test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Bread {
	private SimpleIntegerProperty bnum;
	private SimpleStringProperty bName;
	private SimpleIntegerProperty bPrice;
	private SimpleStringProperty bImg;
	private SimpleStringProperty content;
	private SimpleStringProperty regDate;
	
	// 등록
	public Bread(String bName, int bPrice, String bImg, String content) {
		super();
		this.bName = new SimpleStringProperty(bName);
		this.bPrice = new SimpleIntegerProperty(bPrice);
		this.bImg = new SimpleStringProperty(bImg);
		this.content = new SimpleStringProperty(content);
	}

	// 조회
	public Bread(int bnum, String bName, int bPrice, String bImg, String content, String regDate) {
		super();
		this.bnum = new SimpleIntegerProperty(bnum);
		this.bName = new SimpleStringProperty(bName);
		this.bPrice = new SimpleIntegerProperty(bPrice);
		this.bImg = new SimpleStringProperty(bImg);
		this.content = new SimpleStringProperty(content);
		this.regDate = new SimpleStringProperty(regDate);
	}

	// 수정
	public Bread(int bnum, int bPrice, String bImg, String content, String regDate) {
		super();
		this.bnum = new SimpleIntegerProperty(bnum);
		this.bPrice = new SimpleIntegerProperty(bPrice);
		this.bImg = new SimpleStringProperty(bImg);
		this.content = new SimpleStringProperty(content);
		this.regDate = new SimpleStringProperty(regDate);
	}

	public int getBnum() {
		return this.bnum.get();
	}

	public void setBnum(int bnum) {
		this.bnum.set(bnum);
	}

	public String getBName() {
		return this.bName.get();
	}

	public void setBName(String bName) {
		this.bName.set(bName);
	}

	public int getBPrice() {
		return this.bPrice.get();
	}

	public void setBPrice(int bPrice) {
		this.bPrice.set(bPrice);
	}

	public String getBImg() {
		return this.bImg.get();
	}

	public void setBImg(String bImg) {
		this.bImg.set(bImg);
	}

	public String getContent() {
		return this.content.get();
	}

	public void setContent(String content) {
		this.content.set(content);
	}

	public String getRegDate() {
		return this.regDate.get();
	}

	public void setRegDate(String regDate) {
		this.regDate.set(regDate);
	}
}
