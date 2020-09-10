package basic.database.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberDao {
	Connection conn = ConnectionDB.getDB();
	PreparedStatement pstmt;
	ResultSet rs;
	String sql = "";

	ObservableList<Member> mlist;

	// 회원 조회
	public ObservableList<Member> getMemberList() {
		sql = "select * from member order by 1";
		mlist = FXCollections.observableArrayList();

		try {
			pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Member member = new Member(rs.getInt("mnum"), rs.getString("mname"), rs.getString("mphone"),
						rs.getString("mbirth"), rs.getInt("mpoint"), rs.getString("mresyn"), rs.getString("regdate"));
				mlist.add(member);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return mlist;
	}
}
