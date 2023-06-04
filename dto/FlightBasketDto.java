package dto;

import java.sql.Date;

import lombok.Data;
@Data
/**
 * 항공편 장바구니 Dto 클래스
 * 장바구니에 들어있는 항공편의 정보를 담는 클래스입니다.
 * FlightDto(항공권 정보 클래스)에서 사용자 Id(userId)와 좌석 번호(seatNo)가 추가되었습니다.
 * FlightReservationDto와 내용이 동일해서 쉽게 호환됩니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see dto.FlightDto, dto.FlightReservationDto
 */
public class FlightBasketDto {
	/*
	 * 항공 번호는 항공편을 쉽게 식별하기 위해 만들어졌습니다.
	 * DB에서 PRIMARY KEY 역할을 합니다.
	 */
	private int airCode;         //항공 번호
	private String destination;  //도착지
	private String airlineName;  //항공사 이름
	private String depTime;      //출발 시간
	private String arrivalTime;  //도착 시간
	private String totalTime;    //총 비행 시간
	private String directFlight; //직항 여부
	private int price;           //항공권 가격
	private String userId;       //사용자 아이디(Id)
	private String depDate;      //출발 일자
	private String seatNo;       //좌석 번호
	
	public FlightBasketDto() {
		
	}
}
