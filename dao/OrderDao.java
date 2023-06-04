package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import common.DateConverter;
import dto.FlightBasketDto;
import dto.LoginDto;
import dto.OrderDto;
import dto.ResortBasketDto;
/**
 * 주문 DAO 클래스
 * 장바구니에 있는 항목을 결제하기 전 주문 정보를 확인하는 클래스입니다.
 * 기능
 * (1) 항공권 주문 정보 생성
 * (2) 호텔 주문 정보 생성
 * (3) 주문 리스트 보기
 * (4) 주문 삭제
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.OrderDto
 */
public class OrderDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	
	/**
	 * (1) 항공권 주문 정보 생성
	 * 장바구니에 들어있는 항공편 정보 중에서 직접적으로는 가격, 사용자 Id를 사용했습니다.
	 * 그리고 간접적으로는 항공사 이름과 비행 일자, 도착지 정보를 사용하여 상품명을 만들었습니다.
	 * 사용자 Id, 상품명, 가격을 주문정보를 담는 테이블인 order_list에 저장합니다.
	 * 
	 * @param fbd 항공편 장바구니에 들어있는 항공편 정보 사용
	 */
	public void makeFlightOrder(FlightBasketDto fbd) {
		String SQL = "insert into order_list values(?,?,?)";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, fbd.getUserId());
			String prodName = "[" + fbd.getAirlineName() + "] " + 
			                fbd.getDepDate() + 
			fbd.getDestination() +"행 항공권 티겟";
			psmt.setString(2, prodName);
			psmt.setInt(3, fbd.getPrice());
			psmt.executeUpdate();
			conn.commit();
			psmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("항공권 주문 정보를 저장하지 못했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (2) 호텔 주문 정보 생성
	 * 호텔 장바구니에 들어있는 호텔 중 사용자가 선택한 호텔의 호텔 정보를 사용합니다.
	 * 사용자 Id, 호텔 위치 + 호텔명으로 이루어진 상품명, 총 숙박료(객실 가격(resortPrice)*숙박 일수(nights))
	 * 를 저장합니다.
	 * 
	 * @param rbd 호텔 장바구니의 호텔 정보 사용
	 */
	public void makeResortOrder(ResortBasketDto rbd) {
		String SQL = "insert into order_list values(?,?,?)";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, rbd.getUserId());
			String prodName =  rbd.getResortPlace() + " " + rbd.getResortName();
			psmt.setString(2, prodName);
			psmt.setInt(3, rbd.getResortPrice()*rbd.getNights());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("호텔 주문 정보를 저장하지 못했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (3) 주문 리스트 보기
	 * 사용자 아이디로 사용자가 주문한 상품들을 반환합니다.
	 * 항공편과 호텔이 동시에 포함되어 반환됩니다.
	 * 
	 * @param ld 로그인 객체가 가진 사용자 Id를 사용
	 * @return 사용자 정보 리스트(항공편, 호텔 등 복수 개 존재) 반환
	 */
	public List<OrderDto> listOrder(LoginDto ld) {
		String SQL = "select * from order_list where user_id = ?";
		List<OrderDto> orderList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, ld.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				OrderDto od = new OrderDto();
				od.setUserId(rs.getString("user_id"));
				od.setProdName(rs.getString("prod_name"));
				od.setProdPrice(rs.getInt("prod_price"));
				orderList.add(od);
			}
			conn.commit();
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("주문 정보를 불러오지 못했습니다.");
			e.printStackTrace();
		}
		return orderList;
	}
	
	/**
	 * (4) 주문 삭제
	 * 사용자 Id로 주문 정보를 전체 삭제합니다.
	 * 
	 * @param ld 로그인 객체의 사용자 Id 사용
	 */
	public void deleteOrder(LoginDto ld) {
		String SQL = "delete from order_list where user_id = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1,ld.getId());
			psmt.execute();
		}catch(SQLException e) {
			System.out.println("주문 정보를 전체 삭제하지 못했습니다.");
			e.printStackTrace();
		}
	}
}
