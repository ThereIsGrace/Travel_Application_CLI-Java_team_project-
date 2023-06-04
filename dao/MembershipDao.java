package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import common.DataBase;
import dto.LoginDto;
import dto.MembershipDto;
/**
 * 회원가입 DAO 클래스
 * Id의 중복을 확인한 후 회원정보를 저장하는 회원가입 클래스입니다.
 * 기능 
 * (1) 회원가입 
 * (2) 아이디(Id) 중복 확인
 * (3) 회원 탈퇴
 * (4) 아이디(Id) 찾기
 * (5) 비밀번호(Pw) 찾기
 * (6) 이름 찾기
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0  
 * @see common.DataBase 
 */
public class MembershipDao extends DataBase{
	/**
	 * @param conn : {@link DataBase#dbConnection} 데이터베이스와 연결된 변수. 
	 * @param csmt : 프로시저를 실행할 변수.
	 * @param psmt : 쿼리를 실행할 변수.
	 * @param rs   : 쿼리 결과값을 받아올 변수.
	 */
	Connection conn = null;
	CallableStatement csmt = null;
	PreparedStatement psmt = null; 
	ResultSet rs = null;
	
	/**
	 * (1)회원가입
	 * 사용자로부터 입력받은 개인정보를 회원정보 테이블 MEMBER에 저장합니다.
	 *  
	 * {@link MembershipService}
	 * {@link MembershipDto}
	 * @param : 개인정보(아이디(Id),이름,패스워드(Password),휴대전화 번호)가 들어있는 객체.
	 */
	public void makeMember(MembershipDto md) {
		String sql = "{ call user_make_member(?, ?, ?, ?) }";
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(sql);
			csmt.setString(1, md.getId());
			csmt.setString(2, md.getName());
			csmt.setString(3, md.getPassword());
			csmt.setString(4, md.getPhoneNumber());
			csmt.execute();
			conn.commit();
		}catch(SQLException e) {
			
		}finally {
			try {
				conn.close();
				csmt.close();
			}catch(SQLException e) {
				
			}
		}
	}
	
	/**
	 * (2)아이디(Id) 중복 여부 확인
	 * 중복된 아이디가 MEMBER 테이블에 저장되지 못하도록 방지합니다.
	 * 
	 * @param {@link MembershipService} 사용자가 사용하고 싶은 아이디(Id).
	 * @return 중복된 값이면 1, 아니면 0을 반환.
	 */
	public int checkDuplication(String userid) {
		String SQL = "select count(*) from member where user_id = ?";
		int count = 0;
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, userid);
			rs = psmt.executeQuery();
			if(rs.next()) {
				count = rs.getInt(1);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			
		}
		return count;
	}
	/**
	 * (3) 회원 탈퇴
	 * 사용자 Id를 확인하고 member테이블에서 사용자를 삭제합니다. 
	 * 사용자를 삭제해도 테이블에 자식 테이블 자동 삭제 기능을 넣지 않았기에 login 테이블에 정보가 남습니다.
	 * 따라서 로그인 테이블에서 정보를 지우기 위해  logOut 메서드를 사용합니다.
	 * 
	 * @param ld 사용자 Id 확인
	 */
	public void getOut(LoginDto ld) {
		ExitDao dao = new ExitDao();
		String SQL = "delete from member where user_id = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			psmt.executeUpdate();
			conn.commit();
			close(conn,psmt);
		}catch(SQLException e) {
			System.out.println("회원탈퇴를 할 수가 없습니다.");
			e.printStackTrace();
		}
		dao.logOut(ld);
	}
	
	/**
	 * (4) 아이디(Id) 찾기
	 * 사용자 이름과 핸드폰 번호로 사용자 Id를 검색합니다.
	 * 
	 * @param name 이름을 입력 받음
	 * @param phone 핸드폰 번호를 입력 받음
	 * @return Id를 리턴합니다.
	 */
	public String findId(String name, String phone) {
		String SQL = "select user_id from member where user_name = ? and user_phone = ?";
		String userId = "";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, name);
			psmt.setString(2, phone);
			rs = psmt.executeQuery();
			if(rs.next()){
				userId = rs.getString("user_id");
			}else {
				System.out.println("사용자 Id를 찾지 못했습니다.");
				return null;
			}
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자 Id를 찾지 못했습니다.");
		}
		return userId;
	}
	
	/**
	 * (5) 비밀번호(Pw) 찾기
	 * 사용자가 입력한 Id, 이름, 핸드폰 번호로 비밀번호를 찾습니다.
	 * 
	 * @param id 사용자가 입력한 Id
	 * @param name 사용자가 입력한 이름
	 * @param phone 사용자가 입력한 핸드폰 번호
	 * @return
	 */
	public String findPw(String id, String name, String phone) {
		String SQL = "select user_pw from member where user_id = ? and user_name = ? and user_phone = ?";
		String pw = "";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, id);
			psmt.setString(2, name);
			psmt.setString(3, phone);
			rs = psmt.executeQuery();
			if(rs.next()) {
				pw = rs.getString("user_pw");
			}
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자 Pw를 찾지 못했습니다.");
		}
		return pw;
	}
	/**
	 * (6) 이름 찾기
	 * 
	 * 사용자의 Id는 Primary Key 역할을 하기에 
	 * @param 로그인 객체를 입력하면
	 * @return 사용자의 이름을 반환한다.
	 */
	public String findName(LoginDto ld) {
		String SQL = "select user_name from member where user_id = ?";
		String name = "";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			if(rs.next()) {
				name = rs.getString("user_name");
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println(e);
		}
		return name;
	}
}
