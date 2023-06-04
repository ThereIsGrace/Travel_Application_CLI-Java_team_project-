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
import dto.MembershipDto;
import dto.ResortReservationDto;
import dto.ResortReviewDto;
/**
 * 호텔에 대한 사용자 리뷰를 조회하는 클래스입니다.
 * 기능 (1) 호텔 리뷰 조회 (2) 호텔 리뷰 작성 (3) 사용자가 작성한 리뷰 보기
 * (3) 리뷰 수정 (4) 리뷰 삭제
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.ResortReviewDto 
 */
public class ResortReviewDao extends DataBase{
	Connection conn = null;
	CallableStatement csmt = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	
	/**
	 * (1) 호텔 리뷰 조회
	 * 호텔에 대한 다른 사용자들의 리뷰를 조회합니다.
	 * 호텔 객체를 입력하면 호텔 객체에서 호텔 이름을 찾아 DB의 RESORT_REVIEW 함수에서 인스턴스들을 반환받습니다. 
	 * 
	 * @param rd 호텔 객체 입력
	 * @return 선택한 호텔에 대한 사용자들의 리뷰 리스트를 반환한다. 
	 */
	public List<ResortReviewDto> resortReviewList(String resortName){
		String SQL = "select * from resort_review where resort_name = ?";
		List<ResortReviewDto> reviewList = new ArrayList<>();
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, resortName);
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortReviewDto rrd = new ResortReviewDto();
				rrd.setResortPlace(rs.getString("resort_place"));   	//호텔 위치
				rrd.setResortName(rs.getString("resort_name"));         //호텔 이름 
				rrd.setUserId(rs.getString("user_id"));                 //사용자 Id
				rrd.setUseDate(rs.getString("use_date"));                 //사용 일자
				rrd.setWriteDate(rs.getString("write_date"));             //작성 일자(리뷰)
				rrd.setUserRating(rs.getInt("user_rating"));            //별점(10.0 만점)
				rrd.setUserComment(rs.getString("user_comment"));       //후기
				reviewList.add(rrd);
			}
			close(conn,rs,psmt);
		}catch(SQLException e) {
			
		}
		return reviewList;
	}
	
	/**
	 * (2) 호텔 리뷰 작성
	 * 호텔 리뷰는 사용자 아이디, 호텔 이름을 입력하면 자동으로 RESORT_REVIEW 테이블에 저장하는 프로시저를 통해 저장됩니다.
	 * 
	 * @param md 사용자 아이디 확인
	 * @param rd 호텔 이름 확인 / 호텔 리뷰 작성전 호텔 예약 리스트를 볼 수 있도록 했음.
	 * @param rrd 작성 내용 입력
	 */
	public void createResortReview(LoginDto ld, ResortReservationDto rd, ResortReviewDto rrd) {
		String SQL = "{call create_resort_review(?,?,?,?,?)}";
		try {
			conn = dbConnection();
			csmt = conn.prepareCall(SQL);
			csmt.setString(1, ld.getId());              //사용자 아이디(Id)
			csmt.setString(2, rd.getResortName());      //호텔 이름
			csmt.setString(3, rrd.getWriteDate());      //작성일
			csmt.setDouble(4, rrd.getUserRating());     //별점
			csmt.setString(5, rrd.getUserComment());    //후기
			csmt.execute();
			conn.commit();
			csmt.close();
			conn.close();
		}catch(SQLException e) {
			System.out.println("리뷰를 작성할 수 없습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (3)사용자가 작성한 리뷰 보기
	 * 로그인한 상태에서 자신이 작성한 리뷰들을 모아 볼 수 있습니다.
	 * 
	 * @param md 사용자 아이디(Id)를 확인한다.
	 * @return 사용자가 작성한 모든 리뷰가 담긴 리스트를 반환한다.
	 */
	public List<ResortReviewDto> seeMyReview(LoginDto md){
		String SQL = "select * from resort_review where user_id = ?";
		List<ResortReviewDto> myReviewList = new ArrayList<>(); 
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, md.getId());
			rs = psmt.executeQuery();
			while(rs.next()) {
				ResortReviewDto rrd = new ResortReviewDto();
				rrd.setResortPlace(rs.getString("resort_place"));
				rrd.setResortName(rs.getString("resort_name"));
				rrd.setUserId(rs.getString("user_id"));
				rrd.setUseDate(rs.getString("use_date"));
				rrd.setWriteDate(rs.getString("write_date"));
				rrd.setUserRating(rs.getInt("user_rating"));
				rrd.setUserComment(rs.getString("user_comment"));
				myReviewList.add(rrd);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			System.out.println("사용자가 작성한 리뷰를 불러오지 못했습니다.");
			e.printStackTrace();
		}
		return myReviewList;
	}
	/**
	 * (4) 리뷰 수정
	 * 사용자가 작성한 리뷰 리스트를 보고 고칠 리뷰 객체를 입력하면 별점과 후기를 고칠 수 있습니다. 
	 * 호텔 위치, 사용 일자,작성 일자(최초 작성 일자 유지)등은 변경이 불가능합니다.
	 * 
	 * @param md 사용자 아이디(Id)를 확인한다.
	 * @param rd 사용자가 고칠 리뷰 객체를 입력한다.
	 */
	public void updateResortReview(MembershipDto md, ResortReviewDto rrd) {
		String SQL = "update resort_review set user_rating = ?, "
				+ "user_comment = ? where user_id = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setDouble(1, rrd.getUserRating());
			psmt.setString(2, rrd.getUserComment());
			psmt.setString(3, rrd.getUserId());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자가 작성한 리뷰를 수정하지 못했습니다.");
			e.printStackTrace();
		}
	}
	
	/**
	 * (5) 리뷰 삭제
	 * 사용자가 자신이 작성한 리뷰 리스트를 보고 난 다음 리뷰 객체를 선택해서 삭제할 수 있습니다.
	 * 
	 * @param md 사용자 Id 확인
	 * @param rrd 사용자가 작성한 리뷰의 호텔 이름 확인
	 */
	public void deleteResortReview(MembershipDto md, ResortReviewDto rrd) {
		String SQL = "delete from resort_review where user_id = ? and resort_name = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, md.getId());
			psmt.setString(2, rrd.getResortName());
			psmt.executeUpdate();
			conn.commit();
			close(conn, psmt);
		}catch(SQLException e) {
			System.out.println("사용자가 작성한 리뷰를 삭제하지 못했습니다.");
			e.printStackTrace();
		}
	}

}