package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
	private static final String USER = "travel";
	private static final String PWD = "12345";
	
	public DataBase() {
		init();
	}
	
	private void init() {
		try {
			Class.forName(DRIVER);
		}catch(ClassNotFoundException e) {
			System.out.println(this.getClass().getName()+"클래스에서 드라이버를 찾을 수 없습니다.");
			e.printStackTrace();
		}
	}
	
	public Connection dbConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(URL,USER,PWD);
		return conn;
	}
	
	public void close(Connection conn, ResultSet rs, PreparedStatement psmt) {
		if(rs!=null) {
			try {
				rs.close();
			}catch(Exception e) {
				System.out.println("ResultSet을 종료하던 중 오류가 발생했습니다.");
			}
		}
		if(psmt!=null) {
			try {
				psmt.close();
			}catch(SQLException e) {
				System.out.println("PreparedStatement을 종료하던 중 오류가 발생했습니다.");
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			}catch(SQLException e) {
				System.out.println("DB를 정상적으로 종료하던 중 오류가 발생했습니다.");
			}
		}
	}
	
	public void close(Connection conn, PreparedStatement psmt) {
		if(psmt!=null) {
			try {
				psmt.close();
			}catch(SQLException e) {
				System.out.println("PreparedStatement을 종료하던 중 오류가 발생했습니다.");
			}
		}
		if(conn!=null) {
			try {
				conn.close();
			}catch(SQLException e) {
				System.out.println("DB를 정상적으로 종료하던 중 오류가 발생했습니다.");
			}
		}
	}
}
