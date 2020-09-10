package basic.database.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ReserveDao {
	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	ResultSet rs;
	String sql = "";
	
	ObservableList<Reservation> rlist;

	// 예약 조회
	public ObservableList<Reservation> getReserveList(String name) {
		System.out.println("name : " + name);
		if (name.equals("master")) {
			sql = "select * from reserve order by 1";
		} else {
			sql = "select * from reserve where mname = ? order by 1";
		}
		rlist = FXCollections.observableArrayList();

		try {
			pstmt = conn.prepareStatement(sql);
			if (!name.equals("master")) {
				pstmt.setString(1, name);
			}
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Reservation reserve = new Reservation(rs.getInt("rnum"), rs.getString("mname"), rs.getString("bname"),
						rs.getInt("count"), rs.getString("pickdate").substring(0, 10));
				rlist.add(reserve);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rlist;
	}

}
