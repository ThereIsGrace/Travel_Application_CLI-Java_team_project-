package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import dto.LoginDto;
import dto.ResortBasketDto;
import dto.ResortDto;
/**
 * 호텔 장바구니 클래스
 * 사용자가 선택한 호텔을 장바구니에 옮겨 담습니다.
 * 기능 (1) 호텔 장바구니 담기 (2) 장바구니에 담긴 호텔 리스트 보여주기
 * (3) 장바구니에서 호텔 삭제하기 (4) 장바구니 비우기
 * 
 * @author 정재은
 * @version 1.0.0
 * @see common.DataBase, dto.ResortBasketDto 
 */

public class ResortBasketDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	CallableStatement csmt = null;
	ResultSet rs = null;
	/**
	 * (1) 호텔 장바구니 담기
	 * 사용자가 선택한 호텔 객체와 사용자가 입력한 숙박 일수, 체크인 일자로 호텔 정보를 장바구니에 담습니다/
	 * 
	 * @param ld 사용자 Id 확인하기
	 * @param rd 호텔 정보 객체
	 * @param nights 사용자가 입력한 숙박 일수
	 * @param useDate 사용자가 입력한 체크인 일자
	 */
	public void toResortBasket(LoginDto ld, ResortDto rd, int nights, String useDate) {
		String SQL = "{call to_resort_basket(?,?,?,?)}";
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(SQL);
			csmt.setString(1, ld.getId());
			csmt.setString(2, rd.getResortName());
			csmt.setInt(3, nights);
			csmt.setString(4, useDate);
			csmt.execute();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("장바구니에 호텔 정보를 담는 데 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (2) 장바구니에 담긴 호텔 리스트 보여주기
	 * 사용자 Id로 장바구니에 담긴 호텔 리스트를 봅니다.
	 * 
	 * @param ld 사용자 Id 확인하기
	 * @return 장바구니 리스트를 반환합니다.
	 */
	public List<ResortBasketDto> resortBasketList(LoginDto ld) {
		String SQL = "select * from resort_basket where user_id = ?";
		List<ResortBasketDto> resortBasketList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortBasketDto rbd = new ResortBasketDto();
				rbd.setResortPlace(rs.getString("resort_place"));
				rbd.setResortName(rs.getString("resort_name"));
				rbd.setRoomName(rs.getString("room_name"));
				rbd.setNights(rs.getInt("nights"));
				rbd.setResortPrice(rs.getInt("resort_price"));
				rbd.setUserId(rs.getString("user_id"));
				rbd.setUseDate(rs.getString("use_date"));
				resortBasketList.add(rbd);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("장바구니 리스트를 불러오는 데 실패했습니다.");
			e.printStackTrace();
		}
		return resortBasketList;
	}
	
	/**
	 * (3) 장바구니에서 호텔 삭제하기
	 * 장바구니에서 선택한 호텔 객체를 삭제합니다.
	 * 
	 * @param rbd 호텔 장바구니 객체에서 사용자 Id, 호텔 이름 확인
	 */
	public void deleteFromBasket(ResortBasketDto rbd) {
		String SQL = "delete from resort_basket where user_id = ? and resort_name = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, rbd.getUserId());
			psmt.setString(2, rbd.getResortName());
			psmt.executeUpdate();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("장바구니에서 호텔을 삭제하는 데 실패했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (4) 장바구니 비우기
	 * 사용자의 Id를 확인해서 장바구니에 담긴 모든 내용을 지웁니다.
	 * 
	 * @param ld 사용자 Id 확인
	 */
	public void deleteAllBasket(LoginDto ld) {
		String SQL = "delete from resort_basket where user_id = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			psmt.executeUpdate();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("장바구니를 비우는 데 실패했습니다.");
			e.printStackTrace();
		}
	}
}