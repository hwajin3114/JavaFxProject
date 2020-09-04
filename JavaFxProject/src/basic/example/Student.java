package basic.example;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
	private SimpleStringProperty name;
	private SimpleIntegerProperty korean;
	private SimpleIntegerProperty math;
	private SimpleIntegerProperty english;
	private SimpleIntegerProperty snum;

	public Student(String name, int korean, int math, int english) {
		this.name = new SimpleStringProperty(name);
		this.korean = new SimpleIntegerProperty(korean);
		this.math = new SimpleIntegerProperty(math);
		this.english = new SimpleIntegerProperty(english);
	}


	public Student(int snum, String name, int korean, int math, int english) {
		this.snum = new SimpleIntegerProperty(snum);
		this.name = new SimpleStringProperty(name);
		this.korean = new SimpleIntegerProperty(korean);
		this.math = new SimpleIntegerProperty(math);
		this.english = new SimpleIntegerProperty(english);
	}

	public int getSnum() {
		return this.snum.get();
	}

	public void setSnum(int snum) {
		this.snum.set(snum);
	}

	public String getName() {
		return this.name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public int getKorean() {
		return this.korean.get();
	}

	public void setKorean(int korean) {
		this.korean.set(korean);
	}

	public int getMath() {
		return this.math.get();
	}

	public void setMath(int math) {
		this.math.set(math);
	}

	public int getEnglish() {
		return this.english.get();
	}

	public void setEnglish(int english) {
		this.english.set(english);
	}
}
