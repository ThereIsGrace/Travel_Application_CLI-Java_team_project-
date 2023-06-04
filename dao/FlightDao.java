package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import dto.FlightDto;
/**
 * 항공편 조회 DAO 클래스
 * 여행지에 따라 항공편을 조회하는 클래스입니다.
 * 기능 
 * (1) 여행지 별 항공편 조회
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase, dto.FlightDto
 */
public class FlightDao extends DataBase {
	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	
	/**
	 * 여행지를 가는 항공편 전부를 조회,반환하는 함수입니다.
	 * 
	 * @param 여행지 이름 입력
	 * @return 여행지를 가는 항공편 리스트 반환
	 */
	public List<FlightDto> listFlight(String place) {
		List<FlightDto> airList = new ArrayList<>();
		//모든 여행지의 항공편 정보가 담긴 FLIGHT 테이블에서 도착지를 입력 받으면 해당 여행지로 가는 모든 항공편 리스트를 반환하는 쿼리 작성
		String SQL = "select * from flight where destination = ?";
		try {
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			psmt.setString(1, place);
			rs = psmt.executeQuery();
			while(rs.next()) {
				//FlightDto 객체 생성 후 Dep_Date 항목을 제외한 나머지 항목에 DB에서 받아온 컬럼 이름들을 대입
				FlightDto ad = new FlightDto();
				ad.setAirCode(rs.getInt("air_code"));             // 항공 코드 - 항공편 식별
				ad.setDestination(rs.getString("destination"));   // 도착지
				ad.setAirlineName(rs.getString("airline_name"));  // 항송사 이름
				ad.setDepTime(rs.getString("dep_time"));          // 출발 시간
				ad.setArrivalTime(rs.getString("arrival_time"));  // 도착 시간
				ad.setTotalTime(rs.getString("total_time"));      // 총 비행 시간
				ad.setDirectFlight(rs.getString("direct_flight"));// 직항 여부
				ad.setPrice(rs.getInt("price"));                  // 가격
				//모두 대입했으면 항공편 리스트에 항공편 대입
				airList.add(ad);
			}
			close(conn, rs, psmt);
		}catch(SQLException e) {
			
		}
		//항공편 리스트 반환
		return airList;
	}
}
