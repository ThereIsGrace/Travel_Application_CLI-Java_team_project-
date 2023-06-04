package dto;

import java.sql.Date;

import lombok.Data;
@Data
/**
 * 항공편 Dto 클래스
 * 일반 항공편/사용자가 선택한 항공편 2가지를 저장하는 클래스입니다.
 * 단 사용자가 항공편을 선택했을 때는 아직 좌석을 선택하지 않았기에 사용자 Id와
 * 좌석 번호(seatNo)가 빠져 있습니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0 
 */
public class FlightDto {
	/*
	 * 항공 번호는 항공편을 쉽게 식별하기 위해 만들어졌습니다.
	 * DB에서 PRIMARY KEY 역할을 합니다.
	 */
	private int airCode;          //항공 번호
	private String destination;   //도착지
	private String airlineName;   //항공사 이름
	private String depTime;       //출발 시간
	private String arrivalTime;   //도착 시간
	private String totalTime;     //총 비행 시간
	private String directFlight;  //직항 여부
	private int price;            //가격	
	private String depDate;       //비행 일자
	
	public FlightDto() {

	}
}
