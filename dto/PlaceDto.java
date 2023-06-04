package dto;

import lombok.Data;

@Data
/**
 * 여행지 Dto 클래스
 * 간단한 여행지 정보를 담는 클래스입니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
public class PlaceDto {
	private String countryName;  //국가명
	private String placeName;    //도시명
	private String travelTime;   //비행 시간
}
