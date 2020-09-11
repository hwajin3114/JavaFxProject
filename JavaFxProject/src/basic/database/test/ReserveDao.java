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
						rs.getInt("count"), rs.getString("pickdate").substring(0, 10),
						rs.getString("resdate").substring(0, 10));
				rlist.add(reserve);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rlist;
	}

	// 예약 추가
	public void insertReserve(Reservation reserve) {
		sql = "insert into reserve values(rnum.NEXTVAL, \'" + reserve.getMemName() + "\', \'" + reserve.getBreadName()
				+ "\', " + reserve.getBreadCnt() + ", sysdate, \'" + reserve.getPickUpDate() + "\')";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateMReserve(String name) {
		sql = "update member set mresyn = 'Y' where mname = \'" + name + "\'";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 예약 삭제
	public void deleteReserve(int rnum) {
		sql = "delete from reserve where rnum = " + rnum;
		try {
			pstmt = conn.prepareStatement(sql);
			int i = pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void delUpdateMReserve(String name) {
		sql = "update member set mresyn = 'N' where mname = \'" + name + "\'";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 예약 유무 확인
	public int checkReserve(String name) {
		int result = 0;
		sql = "select * from reserve r " + "join member m on r.mname = m.mname " + "where r.mname = \'" + name
				+ "\'and m.mresyn = 'Y'";

		try {
			pstmt = conn.prepareStatement(sql);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// comboBox
	public ObservableList<String> comboList() {
		ObservableList<String> blist = FXCollections.observableArrayList();
		sql = "select bname from bread order by 1";
		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				blist.add(rs.getString("bname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return blist;
	}
}
