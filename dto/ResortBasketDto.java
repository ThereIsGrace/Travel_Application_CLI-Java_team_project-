package dto;

import java.sql.Date;

import lombok.Data;
@Data
/**
 * 호텔 장바구니 Dto 클래스
 * 마음에 드는 호텔을 장바구니에 옮길 때 사용합니다. 
 * 호텔에 관한 정보를 옮길 때 사용자 정보(사용자 아이디), 예약 정보(숙박 일수, 체크인 일자)를 같이 운반합니다.
 * ResortDto에 사용자 아이디(userId), 숙박 일수(nights), 체크인 일자(usdDate)가 추가되었습니다.
 * ResortReservationDto와 내용이 동일하므로 호환이 가능합니다. 
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see dto.ResortDto
 *
 */
public class ResortBasketDto {
	private String resortPlace;  //호텔 위치
	private String resortName;   //호텔 이름
	private String roomName;     //객실 이름
	private int nights;          //숙박 일수
	private int resortPrice;     //객실 가격
	private String userId;       //사용자 아이디(Id)
	private String useDate;      //체크인 일자
	
	public ResortBasketDto() {
		
	}
}
