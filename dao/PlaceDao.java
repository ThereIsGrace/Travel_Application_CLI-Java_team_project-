package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import common.DataBase;
import dto.PlaceDto;
/**
 * 여행지 DAO 클래스
 * 여행지 정보 리스트를 여행지 객체에 담아 반환합니다.
 * 
 * @author 정재은(ThereIsGrace)
 * @version 1.0.0
 * @see common.DataBase
 */
public class PlaceDao extends DataBase{
	Connection conn = null;
	PreparedStatement psmt = null;
	ResultSet rs = null;
	
	/**
	 * DB에서 여행지 정보 3가지(국가명, 도시명, 비행시간)을 가져와 여행지 객체에 담아 리스트로 반환합니다.
	 * 
	 * @return 여행지 리스트를 반환
	 */
	public List<PlaceDto> showPlace() {
		String SQL = "select * from place";  // 여행지 정보를 가져옵니다.
		List<PlaceDto> placeList = new ArrayList<>();
		try{
			//DB를 연결해서 관련 정보를 가져옵니다.
			conn = dbConnection();
			psmt = conn.prepareStatement(SQL);
			rs = psmt.executeQuery();
			while(rs.next()) {
				// DB에서 가져온 정보를 변수명에 맞춰 여행지 객체 PlaceDto에 집어넣습니다.
				PlaceDto pd = new PlaceDto();
				pd.setCountryName(rs.getString("country_name"));
				pd.setPlaceName(rs.getString("place_name"));
				pd.setTravelTime(rs.getString("travel_time"));
				// 여행지 리스트에 여행지 정보를 추가합니다.
				placeList.add(pd);
			}
			// 열어놓은 DB를 닫습니다.
			close(conn, rs, psmt);
		}catch(SQLException e) {
			
		}
		// 여행지 리스트를 반환합니다.
		return placeList;
	}
}
