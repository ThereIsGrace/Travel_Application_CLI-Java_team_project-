package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import dto.LoginDto;
import dto.ResortBasketDto;
import dto.ResortReservationDto;
/**
 * 호텔 예약 클래스
 * 장바구니에 있는 호텔을 결제한 후 예약 내역으로 옮겨담는 클래스입니다.
 * 기능 (1) 호텔 예약하기(장바구니에서 예약 내역으로 옮기기) 
 * (2) 호텔 예약 리스트 보여주기
 * (3) 호텔 예약 취소
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.ResortReservationDto
 */
public class ResortReservationDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	CallableStatement csmt = null;
	ResultSet rs = null;
	/**
	 * (1) 호텔 예약하기
	 * 장바구니에서 호텔을 선택하면 예약 내역으로 옮겨담습니다.
	 * 
	 * @param rbd 호텔 장바구니 객체 선택
	 */
	public void moveFromBasketToResort(List<ResortBasketDto> rbdList) {
		String SQL = "{call move_from_basket_to_resort(?,?,?,?,?,?,?)}";
		try {
			conn = dbConnection();
			for(int i = 0; i < rbdList.size(); i++) {
				ResortBasketDto rbd = rbdList.get(i);
				csmt = conn.prepareCall(SQL);
				csmt.setString(1, rbd.getResortPlace());
				csmt.setString(2, rbd.getResortName());
				csmt.setString(3, rbd.getRoomName());
				csmt.setInt(4, rbd.getNights());
				csmt.setInt(5, rbd.getNights());
				csmt.setString(6, rbd.getUserId());
				csmt.setString(7, rbd.getUseDate());
				csmt.executeUpdate();
			}
			conn.commit();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println(e);
		}
	}
	/**
	 * (2) 호텔 예약 리스트 보여주기
	 * 사용자 Id를 확인한 후 사용자가 예약한 호텔 리스트를 반환합니다.
	 * 
	 * @param ld 사용자 Id 확인
	 * @return 호텔 예약 리스트 반환
	 */
	public List<ResortReservationDto> listResortReservation(LoginDto ld) {
		String SQL = "select * from resort_reserv where user_id = ?";
		List<ResortReservationDto> resortReservList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortReservationDto rrd = new ResortReservationDto();
				rrd.setResortPlace(rs.getString("resort_place"));
				rrd.setResortName(rs.getString("resort_name"));
				rrd.setRoomName(rs.getString("room_name"));
				rrd.setNights(rs.getInt("nights"));
				rrd.setResortPrice(rs.getInt("resort_price"));
				rrd.setUserId(rs.getString("user_id"));
				rrd.setUseDate(rs.getString("use_date"));
				resortReservList.add(rrd);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("호텔예약 리스트를 가져오는 데 실패했습니다.");
			e.printStackTrace();
		}
		return resortReservList;
	}
	/**
	 * (3) 호텔 예약 취소
	 * 사용자 Id, 호텔 이름을 확인하면 호텔 객체를 테이블에서 삭제합니다.
	 * 
	 * @param rrd 호텔 예약 객체의 사용자 Id, 호텔 이름 확인
	 */
	public void deleteResortReservation(ResortReservationDto rrd) {
		String SQL = "delete from resort_reserv where user_id = ? and resort_name = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, rrd.getUserId());
			psmt.setString(2, rrd.getResortName());
			psmt.executeUpdate();
			conn.commit();
			close(conn,psmt);
		}catch(SQLException e) {
			System.out.println("호텔 예약 내역을 취소하지 못했습니다.");
			e.printStackTrace();
		}
	}
}