package basic.database.test;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basic.common.CommonCode;
import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BreadDao {
	CommonCode comn = new CommonCode();

	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	ResultSet rs;
	String sql = "";

	String checkInt = "^[0-9]+$";
	File selected;

	ObservableList<Bread> list;

	// 조회
	public ObservableList<Bread> getBoardList() {
		sql = "select * from bread order by 1";
		list = FXCollections.observableArrayList();
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Bread bread = new Bread(rs.getInt("bnum"), rs.getString("bname"), rs.getInt("bprice"),
						rs.getString("bimg"), rs.getString("content"), rs.getString("regdate"));
				list.add(bread);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 추가
	public void insertBread(Bread bread) {

		sql = "insert into bread values(bnum.NEXTVAL, \'" + bread.getBName() + "\', "
				+ bread.getBPrice() + ", \'" + bread.getBImg() + "\', \'" + bread.getContent()
				+ "\', sysdate)";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}
