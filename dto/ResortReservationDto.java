package dto;

import java.sql.Date;

import lombok.Data;
@Data
/**
 * 호텔 예약 Dto 클래스 
 * 장바구니에 있는 호텔을 결제한 뒤 예약 내역으로 옮길 때 사용합니다.
 * ResotBasketDto와 내용이 동일하므로 호환이 가능합니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see dto.ResortBasketDto
 */
public class ResortReservationDto {
	private String resortPlace;  //호텔 위치
	private String resortName;   //호텔 이름
	private String roomName;     //객실 이름
	private int nights;          //숙박 일수
	private int resortPrice;     //객실 가격
	private String userId;       //사용자 아이디(Id)
	private String useDate;      //체크인 일자
	
	public ResortReservationDto() {
		
	}
}
