package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import dto.FlightBasketDto;
import dto.FlightDto;
import dto.LoginDto;
import dto.MembershipDto;
/**
 * 항공권 장바구니 DAO 클래스
 * 결제전 항공권 선택을 장바구니에 임시 저장합니다. 
 * 결제가 완료되면 장바구니에서 예약 내역으로 옮겨 갑니다.
 * 기능 
 * (1) 항공권 장바구니에 넣기 
 * (2) 장바구니 리스트(항공권) 보기 
 * (3) 항공권 장바구니에서 삭제하기 
 * (4) 장바구니 비우기
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.FlightBasketDto 
 */
public class FlightBasketDao extends DataBase {
	Connection conn = null;
	PreparedStatement psmt = null;
	CallableStatement csmt = null;
	ResultSet rs = null;
	
	/**
	 * (1) 항공권 장바구니에 넣기
	 * 사용자가 로그인한 상태에서 항공권과 좌석을 선택하면 항공권 객체와 좌석번호를 FLIGHT_BASKET테이블에 입력하는
	 * 프로시저를 통해 항공권 장바구니 정보를 장바구니에 저장합니다.
	 * 
	 * @param md 사용자의 Id를 확인
	 * @param fd 선택한 항공권 입력
	 * @param seatNo 사용자의 좌석번호 입력
	 */
	public void toFlightBasket(LoginDto ld, FlightDto fd, String seatNo) {
		String SQL = "{call to_flight_basket(?,?,?,?)}";
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(SQL);
			csmt.setString(1, ld.getId());
			csmt.setInt(2, fd.getAirCode());
			csmt.setString(3, fd.getDepDate());
			csmt.setString(4, seatNo);
			csmt.execute();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("사용사 선택 항공권을 장바구니로 옮기는데 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (2) 장바구니 리스트(항공권) 보기
	 * 사용자의 Id로 장바구니 테이블에서 장바구니 리스트를 가져옵니다.
	 * 
	 * @param md 사용자의 Id를 확인
	 * @return 장바구니 리스트를 반환
	 */
	public List<FlightBasketDto> flightBasketList(LoginDto ld){
		String SQL = "select * from flight_basket where userid = ?";
		List<FlightBasketDto> flightBasketList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				FlightBasketDto fbd = new FlightBasketDto();
				fbd.setAirCode(rs.getInt("air_code"));
				fbd.setDestination(rs.getString("destination"));
				fbd.setAirlineName(rs.getString("airline_name"));
				fbd.setDepTime(rs.getString("dep_time"));
				fbd.setArrivalTime(rs.getString("arrival_time"));
				fbd.setTotalTime(rs.getString("total_time"));
				fbd.setDirectFlight(rs.getString("direct_flight"));
				fbd.setPrice(rs.getInt("price"));
				fbd.setUserId(rs.getString("userid"));
				fbd.setDepDate(rs.getString("dep_date"));
				fbd.setSeatNo(rs.getString("seat_no"));
				flightBasketList.add(fbd);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("항공권 장바구니를 불러오는 데 실패했습니다.");
			e.printStackTrace();
		}
		return flightBasketList;
	}
	
	/**
	 * (3) 항공권 장바구니에서 삭제하기
	 * 원하는 장바구니 객체를 선택하면 장바구니에서 삭제합니다.
	 * 
	 * @param fbd 장바구니 리스트에서 장바구니 객체 선택
	 */
	public void deleteFlightBasket(FlightBasketDto fbd) {
		String SQL = "delete from flight_basket where userid = ? and air_code = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, fbd.getUserId());
			psmt.setInt(2, fbd.getAirCode());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("항공권을 장바구니에서 삭제하는 데 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (4) 장바구니 비우기
	 * 로그인 객체에 담겨있는 사용자 아이디를 입력하면 사용자의 장바구니 목록을 전체 삭제합니다.
	 *  
	 * @param ld 사용자의 아이디(Id) 입력
	 */
	public void deleteAllBasket(LoginDto ld) {
		String SQL = "delete from flight_basket where userid = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자의 장바구니를 전체 삭제하지 못했습니다.");
			e.printStackTrace();
		}
	}
}
