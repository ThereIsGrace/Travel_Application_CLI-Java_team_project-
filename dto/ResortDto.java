package dto;

import lombok.Data;
/**
 * 호텔 Dto 클래스 
 * 호텔에 관한 정보를 담고 있습니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
@Data
public class ResortDto {
	private String resortPlace;     //호텔 위치
	private String resortName;      //호텔 이름
	private String roomName;        //객실 이름
	private String accomodation;    //숙박 인원
	private String feature;         //기본 제공 사양
	private double customerRating;  //사용자 평점
	private int resortPrice;        //객실 가격
	
	public ResortDto() {
		
	}
}
