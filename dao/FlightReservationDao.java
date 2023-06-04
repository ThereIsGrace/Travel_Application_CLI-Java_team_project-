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
import dto.FlightReservationDto;
import dto.LoginDto;
/**
 * 항공권 예약 DAO 클래스
 * 항공권을 예약하는 클래스입니다.
 * 프로그램으로부터 사용자가 선택한 항공권 정보를 장바구니에서 받아서 예약 테이블에 저장합니다.
 * 기능 (1) 항공권 예약하기(장바구니에서 예약 내역으로 옮기기)
 * (2) 항공권 예약 리스트 보여주기
 * (3) 항공권 예약 취소
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.FlightReservationDto, dto.FlightBasketDto
 */
public class FlightReservationDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	CallableStatement csmt = null;
	ResultSet rs = null;
	
	/**
	 * (1) 항공권 예약하기
	 * 장바구니에 여러 항공권이 들어있다면 그 중 사용자가 선택한 항목을 예약 내역으로 옮기는 메서드입니다.
	 * 장바구니에 항공권이 여러 개 존재할 수 있으므로, 사용자 Id와 항공 코드를 확인합니다.
	 * 마지막으로 사용자 Id와 항공 코드가 동일할 수 있지만 비행 일자가 다른 항공편이 복수 개 포함될 수 있으므로
	 * 비행 일자까지 확입합니다. 
	 * 
	 * @param fbd 사용자 id, 항공 번호, 비행 일자 입력
	 */
	public void moveFromBasketToFlight(List<FlightBasketDto> fbdList) {
		String SQL = "{call move_from_basket_to_flight(?,?,?,?,?,?,?,?,?,?,?)}";
		try {
			conn = dbConnection();
			for(int i = 0; i < fbdList.size(); i++) {
				csmt = conn.prepareCall(SQL);
				FlightBasketDto fbd = fbdList.get(i);
				csmt.setInt(1,fbd.getAirCode());
				csmt.setString(2, fbd.getDestination());
				csmt.setString(3, fbd.getAirlineName());
				csmt.setString(4, fbd.getDepTime());
				csmt.setString(5, fbd.getArrivalTime());
				csmt.setString(6, fbd.getTotalTime());
				csmt.setString(7, fbd.getDirectFlight());
				csmt.setInt(8, fbd.getPrice());
				csmt.setString(9, fbd.getUserId());
				csmt.setString(10, fbd.getDepDate());
				csmt.setString(11, fbd.getSeatNo());
				csmt.executeUpdate();
			}
			conn.commit();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("항공권 예약 리스트를 추가하지 못했습니다.");
			e.printStackTrace();
		}
	}
	/**
	 * (2) 항공권 예약 리스트 보여주기
	 * 사용자가 예약한 항공권 리스트를 보여줍니다.
	 * 
	 * @param md 사용자 Id를 확인
	 * @return 항공권 예약 리스트를 반환
	 */
	public List<FlightReservationDto> listFlightReservation(LoginDto ld){
		String SQL = "select * from flight_reserv where userid=?";
		List<FlightReservationDto> flightReservList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				FlightReservationDto frd = new FlightReservationDto();
				frd.setAirCode(rs.getInt("air_code"));
				frd.setDestination(rs.getString("destination"));
				frd.setAirlineName(rs.getString("airline_name"));
				frd.setDepTime(rs.getString("dep_time"));
				frd.setArrivalTime(rs.getString("arrival_time"));
				frd.setTotalTime(rs.getString("total_time"));
				frd.setDirectFlight(rs.getString("direct_flight"));
				frd.setPrice(rs.getInt("price"));
				frd.setUserId(rs.getString("userid"));
				frd.setDepDate(rs.getString("dep_date"));
				frd.setSeatNo(rs.getString("seat_no"));
				flightReservList.add(frd);
			}
			close(conn,rs,psmt);
		}catch(SQLException e) {
			System.out.println("항공권 예약 내역을 불러오지 못했습니다.");
			e.printStackTrace();
		}
		return flightReservList;
	}
	/**
	 * (3) 항공권 예약 취소
	 * 사용자가 선택한 항공권을 취소합니다. 예약 리스트에서 여러 항공권 중 특정 항목만 삭제하기 위해서
	 * 사용자 Id와 항공 번호를 사용합니다. 
	 * 
	 * @param frd 사용자가 선택한 항공권 예약 객체
	 */
	public void deleteFlightReservation(FlightReservationDto frd) {
		String SQL = "{call delete_flight_reserv(?,?)}";
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(SQL);
			csmt.setString(1, frd.getUserId());
			csmt.setInt(2, frd.getAirCode());
			csmt.execute();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("항공권 예약을 삭제하지 못했습니다.");
			e.printStackTrace();
		}
	}
}