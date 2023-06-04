package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DataBase;
import dto.LoginDto;
/**
 * 로그아웃 DAO 클래스
 * 기능
 * (1) 로그아웃
 * 사용자 Id명과 일치하는 인스턴스를 찾아서 로그인 테이블에서 삭제합니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @see common.DataBase 
 */
public class ExitDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	/**
	 * (1) 로그아웃
	 * 사용자 객체가 담고 있는 사용자 Id로 로그인 테이블에서 사용자를 삭제합니다.
	 * 
	 * @param ld 사용자 Id를 확인합니다.
	 */
	public void logOut(LoginDto ld) {
		String SQL = "delete from login where user_id = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("로그아웃 하는 도중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}
}
