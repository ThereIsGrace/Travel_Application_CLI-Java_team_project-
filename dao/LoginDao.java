package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import common.DataBase;
import dto.LoginDto;
/**
 * 로그인 DAO 클래스
 * 로그인 기능을 수행하는 클래스입니다. 
 * 기능
 * (1) 로그인 
 * (1) 아이디, 비밀번호 일치 여부 확인 
 * (2) 로그인 객체 반환
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase 데이터베이스를 열고 닫는 기능을 상속받습니다.
 */
public class LoginDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	CallableStatement csmt = null;
	ResultSet rs = null;

	/**
	 * (1) 로그인
	 * 사용자 Id와 비밀번호를 로그인 테이블에 저장합니다.
	 * 
	 * @param id 사용자 입력 Id
	 * @param pw 사용자 입력 비밀번호
	 */
	public void login(String id, String pw) {
		String SQL = "insert into login values(?,?)";
		try {
			conn = dbConnection();
			psmt = conn.prepareCall(SQL);
			psmt.setString(1, id);
			psmt.setString(2, pw);
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자 로그인에 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (2) 아이디, 비밀번호 일치 여부 확인
	 * 로그인 성공 여부를 반환합니다.
	 * 
	 * @param id 사용자의 Id
	 * @param pw 사용자의 Password
	 * @return 로그인이 성공하면 0, 비밀번호가 틀리면 1, 아이디 자체가 존재하지 않으면 2를 반환합니다.
	 */
	public int loginCheck(String id, String pw) {
		String SQL = "{? = call member_login(?,?)}";
		int result = 0;
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(SQL);
			csmt.registerOutParameter(1, Types.INTEGER);
			csmt.setString(2, id);
			csmt.setString(3, pw);
			csmt.execute();
			result = csmt.getInt(1);
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * (3) 로그인 객체 반환
	 * 로그인에 성공했을 때 생성됩니다.
	 * 페이지마다 로그인이 필요한 곳에 자동으로 전달됩니다. 
	 * 
	 * @param id 사용자 Id
	 * @param pw 사용자 Password
	 * @return 사용자 로그인 객체 반환
	 */
	public LoginDto loginInfo(String id, String pw) {
		int result = loginCheck(id, pw);
		LoginDto ld = new LoginDto();
		if(result == 0) {
			ld.setId(id);
			ld.setPassword(pw);
		}
		return ld;
	}
	
}
