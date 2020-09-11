package basic.database.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import basic.common.CommonCode;
import basic.common.ConnectionDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MemberDao {
	CommonCode comn = new CommonCode();

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

	// 추가
	public void insertMember(Member member) {
		sql = "insert into member(mnum, mname, mphone, mbirth) values(mnum.NEXTVAL, \'" + member.getMName() + "\', "
				+ member.getMPhone() + ", \'" + member.getMBirth() + "\')";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 수정시 값 출력
	public ObservableList<Member> getMList(int mnum) {
		sql = "select * from member where mnum = " + mnum;
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

	public void updateMember(Member member) {
		sql = "update member set mphone = ?, mbirth = ?	where mnum = ?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getMPhone());
			pstmt.setString(2, member.getMBirth());
			pstmt.setInt(3, member.getMnum());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
