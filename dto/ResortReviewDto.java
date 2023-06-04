package dto;

import java.sql.Date;

import lombok.Data;
@Data
/**
 * 호텔 리뷰 Dto 클래스
 * 호텔에 대한 사용자들의 리뷰 정보를 담고 있습니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see dto.ResortReservDto, dto.LoginDto
 */
public class ResortReviewDto {
	/*
	 * 호텔 리뷰는 호텔을 결제, 예약하고 실제로 숙박일이 지난 다음에 작성할 수 있다. 
	 * 따라서 호텔 위치, 호텔 이름, 숙박일은 호텔 예약 객체에서 가져오고, 사용자 아이디는 로그인한
	 * 상태에서 저장되는 로그인 정보 객체에서 가져올 것이다.
	 */
	private String resortPlace;    //호텔 위치(ResortReservDto)
	private String resortName;     //호텔 이름(ResortDto)
	private String userId;         //사용자 아이디(Id)(MembershipDto)
	private String useDate;        //숙박일(YYYY년 MM월)(ResortReservDto)
	private String writeDate;      //작성일(YY/MM/DD)(사용자입력) 
	private double userRating;     //사용자 평점(1~10)(사용자입력)
	private String userComment;    //사용자 후기(사용자입력)
	
	public ResortReviewDto() {
		
	}
}
