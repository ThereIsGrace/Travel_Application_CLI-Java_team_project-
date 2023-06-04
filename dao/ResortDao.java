package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.DataBase;
import dto.ResortDto;
/**
 * 숙박 가능한 호텔 객실을 반환하는 클래스입니다.
 * 기능 (1) (사용자용) 호텔 리스트 조회(특정 지역) (2) (관리자용) 호텔 리스트 조회(전체 지역)
 * (3) (관리자용) 호텔 정보 수정 (3) (관리자용) 호텔 정보 삭제
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.ResortDto
 */
public class ResortDao extends DataBase{
	Scanner stdIn = new Scanner(System.in);
	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	
	/**
	 * (1) (사용자용) 호텔 리스트 조회
	 * 도착지를 입력하면 DB에 존재하는 RESORT 테이블에서 호텔 정보를 조회합니다.
	 * 조회한 정보를 리스트로 만들어서 반환합니다.
	 * 
	 * @param destination 도착지를 입력하면
	 * @return 도착지에 존재하는 숙박업체(호텔) 리스트를 반환
	 */
	public List<ResortDto> resortList(String destination) {
		String SQL = "select * from resort where resort_place = ?";
		List<ResortDto> resortList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, destination);
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortDto rd = new ResortDto();
				rd.setAccomodation(rs.getString("accomodation"));
				rd.setCustomerRating(rs.getDouble("customer_rating"));
				rd.setFeature(rs.getString("feature"));
				rd.setResortName(rs.getString("resort_name"));
				rd.setResortPlace(rs.getString("resort_place"));
				rd.setResortPrice(rs.getInt("resort_price"));
				rd.setRoomName(rs.getString("room_name"));
				resortList.add(rd);
			}
			close(conn,rs,psmt);
		}catch(SQLException e) {
			System.out.println(e);
		}
		return resortList;
	}
	
	/**
	 * (2) 전체 호텔 리스트 조회 
	 * 
	 * @return 전체 호텔 리스트 반환
	 */
	public List<ResortDto> resortListAll(){
		String SQL = "select * from resort";
		List<ResortDto> resortList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortDto rd = new ResortDto();
				rd.setResortPlace(rs.getString("resort_place"));
				rd.setResortName(rs.getString("resort_name"));
				rd.setRoomName(rs.getString("room_name"));
				rd.setAccomodation(rs.getString("accomodation"));
				rd.setFeature(rs.getString("feature"));
				rd.setCustomerRating(rs.getDouble("customer_rating"));
				rd.setResortPrice(rs.getInt("resort_price"));
				resortList.add(rd);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("호텔리스트를 반환할 수 없습니다.");
		}
		return resortList;
	}
	
	/**
	 * (3) (관리자용) 호텔 정보 수정
	 * 호텔의 기본정보를 수정합니다.
	 *
	 * @param rd1 변경하기 전 호텔 객체
	 * @param rd2 변경하고 싶은 호텔 객체
	 * rd2의 필드가 기본형이면 rd1의 필드를 그대로 사용. 그렇지 않으면 rd2 필드를 사용하여 호텔 정보 업데이트 
	 */
	public void updateResort(ResortDto rd1,ResortDto rd2) {
		//resort_place(호텔 위치)와 customer_rating(사용자 평점)은 수정할 수 없습니다.
		String SQL = "update resort set resort_name = ?, room_name = ?,"
				+ " feature = ?, accomodation = ?, resort_price = ? "
				+ "where resort_name = ?";
		/*
		 * resort_name(호텔 이름), room_name(객실 이름), feature(기본 제공 사양), 
		 * accomodation(숙박 인원), resort_price(객실 가격)
		 */
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			if(rd2.getResortPrice() == 0) {
				psmt.setInt(1, rd1.getResortPrice());
			}else {
				psmt.setInt(1, rd2.getResortPrice());
			}
			
			if(rd2.getResortName() == null) {
				psmt.setString(2, rd1.getResortName());
			}else {
				psmt.setString(2, rd2.getResortName());
			}
			
			if(rd2.getRoomName() == null) {
				psmt.setString(3, rd1.getRoomName());
			}else {
				psmt.setString(3, rd2.getRoomName());
			}
			
			if(rd2.getFeature() == null) {
				psmt.setString(4, rd1.getFeature());
			}else {
				psmt.setString(4, rd2.getFeature());
			}
			
			if(rd2.getCustomerRating() == 0.0) {
				psmt.setDouble(5, rd1.getCustomerRating());
			}else {
				psmt.setDouble(5, rd2.getCustomerRating());
			}
			
			psmt.executeUpdate();
		}catch(SQLException e) {
			System.out.println("resort 테이블을 업데이트하던 중 오류가 발생했습니다.");
			e.printStackTrace();
		}finally {
			close(conn, psmt);
		}
	}
	
	/**
	 * (4) (관리자용) 호텔 정보 삭제
	 * 호텔의 기본정보를 삭제합니다.
	 * 
	 * @param rd (ResortDto) 호텔 객체를 입력하면 그 안에 들어 있는 호텔 이름으로 인스턴스를 검색 후 삭제
	 * 호텔이름으로 호텔을 검색해서 인스턴스를 삭제합니다.
	 */
	public void deleteResort(ResortDto rd) {
		String SQL = "delete from resort where resort_name = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, rd.getResortName());
			psmt.executeUpdate();
			close(conn,psmt);
		}catch(SQLException e) {
			System.out.println("resort 테이블에서 인스턴스를 삭제 하던 중 오류가 발생했습니다.");
			e.printStackTrace();
		}
	}
}
