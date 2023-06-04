package user;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import dao.FlightReservationDao;
import dao.ResortReservationDao;
import dao.ResortReviewDao;
import dto.FlightReservationDto;
import dto.LoginDto;
import dto.ResortReservationDto;
import dto.ResortReviewDto;

/**
 * 예약 클래스
 * 결제가 완료되면 예약 클래스에서 결제된 항공권과 호텔들을 예약 테이블로 옮깁니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 *
 */
public class ReservationService {
	Scanner stdIn = new Scanner(System.in);
	LoginDto ld = new LoginDto();  //로그인 객체들을 저장
	ResortReservationDao rrdao = new ResortReservationDao(); //호텔 예약(RESORT_RESERV) 테이블을 관리하는 DAO
	FlightReservationDao frdao = new FlightReservationDao(); //항공권 예약(FLIGHT_RESERV) 테이블을 관리하는 DAO
	public ReservationService(LoginDto ld) {//입력받은 로그인 객체를 전역변수에 복사
		this.ld.setId(ld.getId()); //Id 복사
		this.ld.setPassword(ld.getPassword()); //Pw 복사
	}
	
	//예약 내역 보기(호텔, 항공권 모두)
	public void seeReservation() {
		System.out.println("                        ** 예약 내역 확인 **                       ");
		System.out.println();
		//항공권 예약 DAO에서 로그인 객체를 이용하여 예약 내역을 가져옴
		List<FlightReservationDto> flightReservList = frdao.listFlightReservation(ld);
		//항공권 예약 내역이 존재하는 경우
		if(flightReservList.size() != 0) 
			printFlightReserv(flightReservList); //항공권 예약 리스트를 출력
		else//그렇지 않은 경우
			System.out.println("항공권 예약 내역이 존재하지 않습니다.");
		//호텔 예약 DAO에서 로그인 객체를 이용하여 호텔 예약 내역을 가져옴
		List<ResortReservationDto> resortReservList = rrdao.listResortReservation(ld);
		//호텔 예약 내역이 존재하는 경우
		if(resortReservList.size() != 0) {
			//호텔 예약 내역 출력
			printResortReserv(resortReservList);
			
			System.out.println("메뉴 선택 1. 리뷰 작성 2. 리뷰 모아보기 3. 종료하기");
			System.out.print("메뉴 입력:");
			int m = stdIn.nextInt();
			switch(m) {
			case 1:createReview(); break;  //리뷰 작성 메서드로 이동
			case 2:seeMyReview(ld); break; //로그인 객체로 내가 쓴 리뷰만 확인
			case 3:Exit exit = new Exit(ld); break;//로그인 객체로 종료
			}
		}else {//호텔 예약 내역이 존재하지 않는 경우
			System.out.println("호텔 예약 내역이 존재하지 않습니다.");
			System.out.println("메뉴 선택 1. 호텔 조회/예약하기 2. 종료하기");
			System.out.print("메뉴 입력:");
			int m = stdIn.nextInt();
			switch(m) {
			case 1:ResortService rs = new ResortService(ld); break;//호텔 다시 조회/예약하러 가기
			case 2:Exit exit = new Exit(ld); break;
			}
		}
	}
	//예약 내역 보기(항공권만)
	public void seeReservation2() {
		System.out.println("                        ** 예약 내역 확인 **                       ");
		System.out.println();
		List<FlightReservationDto> flightReservList = frdao.listFlightReservation(ld);
		if(flightReservList.size() != 0) {
			printFlightReserv(flightReservList);
			System.out.println("메뉴 선택 1. 호텔/예약 조회 2. 메인 화면으로 돌아가기 3. 종료하기");
			int n = stdIn.nextInt();
			switch(n) {
			case 1: ResortService rs = new ResortService(ld); break;
			case 2: UserMain us = new UserMain(); us.userMain(ld); break;
			case 3: Exit exit = new Exit(ld);
			}
		}else {
			System.out.println("항공권 예약 내역이 존재하지 않습니다.");		
			System.out.println("메뉴 선택 1. 리뷰 작성 2. 리뷰 모아보기 3. 종료하기");
			System.out.print("＆메뉴 입력:");
			int m = stdIn.nextInt();
			switch(m) {
			case 1:createReview(); break;
			case 2:seeMyReview(ld); break;
			case 3:Exit exit = new Exit(ld); break;
			}
		}
	}
	//항공권 예약 정보를 출력하는 메서드(예약한 항공권 리스트를 인자로 설정)
	public void printFlightReserv(List<FlightReservationDto> flightReservList) {
		System.out.println(ld.getId() + "고객님이 예약하신 항공권 리스트입니다.");
		for(FlightReservationDto frd: flightReservList) {
			int airCode = frd.getAirCode();
			String destination = frd.getDestination();
			String airlineName = frd.getAirlineName();
			String depTime = frd.getDepTime();
			String arrivalTime = frd.getArrivalTime();
			String totalTime = frd.getTotalTime();
			String directFlight = frd.getDirectFlight();
			int price = frd.getPrice();
			String userId = frd.getUserId();
			String depDate = frd.getDepDate();
			String seatNo = frd.getSeatNo();
			System.out.println("1. 항공 번호 : " + airCode + " 2. 도착지 : " + destination);
			System.out.println("3. 항공사 이름 : " + airlineName + " 4. 출발 시간 : " + depTime);
			System.out.println("5. 도착 시간 : " + arrivalTime + " 6. 총 시간 : " + totalTime);
			System.out.println("7. 직항 여부 : " + directFlight + " 8. 가격 : " + price);
			System.out.println("9. 사용자  Id: " +  userId + "10. 출발 일자 : " + depDate);
			System.out.println("10. 좌석 번호: " + seatNo);
			System.out.println("-------------------------------------------------------");
		}
	}
	//호텔 예약 정보를 출력하는 메서드(예약한 호텔 리스트를 인자로 설정)
	public void printResortReserv(List<ResortReservationDto> resortReservList) {
		int i = 0;
		System.out.println(ld.getId() + "고객님이 예약하신 호텔 리스트입니다.");
		System.out.printf("\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s",
				"번호","호텔 위치","호텔 이름","객실 이름","숙박 일수","객실 가격","사용자  Id","체크인 날짜");	
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------------"
				+"--------------------------------------------------");
		for(ResortReservationDto rrd: resortReservList) {
			String resortPlace = rrd.getResortPlace();
			String resortName = rrd.getResortName();
			String roomName = rrd.getRoomName();
			int nights = rrd.getNights();
			int resortPrice = rrd.getResortPrice();
			String userId = rrd.getUserId();
			String useDate = rrd.getUseDate();
			System.out.println();
			System.out.printf("\t%-10d\t%-10s\t%-10s\t%-10s\t%-10d\t%-10d\t%-10s\t%-10s",
					i++,resortPlace,resortName,roomName,nights,resortPrice,
					userId,useDate);
			System.out.println("");
			System.out.println("---------------------------------------------------------------------------------------"
					+"--------------------------------------------------");
		}
	}
	
	//리뷰 만들기
	public void createReview() {
		ResortReviewDao reviewDao = new ResortReviewDao(); //리뷰 클래스(RESORT_REVIEW)를 관리하는 DAO
		//날짜 설정(자동)
		Date dd = new Date(); //오늘
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		String writeDate = sdf.format(dd);//오늘을 yyyy-MM-dd로 변환해서 작성일자로 만듦
		//로그인 객체로 호텔 예약 내역 가져옴
		List<ResortReservationDto> resortReservList = rrdao.listResortReservation(ld);
		printResortReserv(resortReservList);//호텔 예약 내역 출력
		System.out.println("리뷰를 작성하실 호텔을 선택해주세요");
		System.out.print("＆번호 입력: ");
		int hotelNum = stdIn.nextInt();
		//선택받은 호텔 객체를 resort에 저장
		ResortReservationDto resort = resortReservList.get(hotelNum);
		System.out.println(resort.getResortName() + " 호텔의 점수는 몇 점인가요? ");
		System.out.println("0점(정말 별로예요)<--------------------------------->10점(너무 좋아요)");
		//점수 입력
		System.out.print("＆점수 입력: ");
		double grade = stdIn.nextDouble();
		//후기 입력
		System.out.println("후기를 입력해주세요.");
		System.out.println("q를 누르시면 자동으로 후기 입력이 종료되고 후기가 저장됩니다.");
		String comment = "";
		String result = "";
		while(!(comment = stdIn.nextLine()).equals("q")) {
			result += "\n" + comment;
		}
		//ResortReviewDto에 작성 일자, 점수, 후기 설정
		ResortReviewDto review = new ResortReviewDto();
		review.setWriteDate(writeDate);
		review.setUserRating(grade);
		review.setUserComment(result.trim());
		//만들어진 후기 객체를 RESORT_REVIEW 테이블에 저장
		reviewDao.createResortReview(ld, resort, review);
		System.out.println();
		System.out.println("리뷰를 작성해 주셔서 감사합니다.");
		System.out.println("메뉴 입력 1. 내가 작성한 리뷰 보기 2. 다른 호텔 리뷰 작성하기 3. 종료하기");
		System.out.print("＆메뉴 입력:");
		int m = stdIn.nextInt();
		switch(m) {
		case 1: seeMyReview(ld); break;  //로그인 객체로 내가 쓴 리뷰만 출력
		case 2: createReview(); break;   //다시 리뷰 작성하기
		case 3: Exit exit = new Exit(ld); break;//로그인 객체로 종료
		}
	}
	//내 리뷰만 보기
	public void seeMyReview(LoginDto ld) {//로그인 객체의 사용자정보로 내가 쓴 리뷰만 찾는다. 
		ResortReviewDao reviewDao = new ResortReviewDao();
		List<ResortReviewDto> rrdList = reviewDao.seeMyReview(ld); //리뷰 리스트 불러오기
		int i = 0;
		for(ResortReviewDto rrd : rrdList) {
			//리뷰 객체의 정보들을 하나씩 입력받은 후 
			String resortPlace = rrd.getResortPlace();
			String resortName = rrd.getResortName();
			String userId = rrd.getUserId();
			String useDate = rrd.getUseDate();
			String writeDate = rrd.getWriteDate();
			double userRating = rrd.getUserRating();
			String userComment = rrd.getUserComment();
			//양식에 맞춰 출력
			System.out.printf("[" + (i + 1) + "]");
			System.out.println("호텔 위치: " + resortPlace);
			System.out.println("호텔명: " + resortName);
			System.out.println("사용자 Id: " +userId + " 사용 일자:" + useDate);
			System.out.println("작성 일자: " + writeDate);
			System.out.println("사용자 평가: " + userRating);
			System.out.println("후기: " + userComment);
			System.out.println();
			i++;
		}
		System.out.println(" 메뉴 선택 1. 메인 화면으로 가기 2. 종료하기");
		System.out.print("＆메뉴 입력: ");
		int n = stdIn.nextInt();
		switch(n) {
		case 1: UserMain us = new UserMain(); us.userMain(ld); break;
		case 2: Exit exit = new Exit(ld); break;
		}
	}
}
