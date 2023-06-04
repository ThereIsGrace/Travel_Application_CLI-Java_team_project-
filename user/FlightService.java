package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import art.FlightSeat;
import common.CalendarView;
import common.DateConverter;
import dao.FlightBasketDao;
import dao.FlightDao;
import dao.MembershipDao;
import dto.FlightBasketDto;
import dto.FlightDto;
import dto.LoginDto;
/**
 * 호텔을 예약/조회 클래스 
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
// 1. 도착지 정하기(PlaceService) -> 2. 출발 날짜 정하기 -> 3. 항공편 정하기 -> 4. 좌석 정하기 -> 5. 항공권 구매
public class FlightService {
	Scanner stdIn = new Scanner(System.in);      //사용자로부터 입력을 받을 스캐너
	FlightDao dao = new FlightDao();             //항공편에 대한 정보를 출력할 DAO
	FlightBasketDao fbd = new FlightBasketDao(); //항공편 선택을 항공편 장바구니에 입력할 DAO
	LoginDto ld = new LoginDto();                //로그인한 사용자에 대한 정보를 담는 DTO
	/*
	 * 사용자가 선택한 항공편을 저장할 리스트. 전역변수로 설정하여 모든 메서드가 이용가능하게 함.
	 */
	List<FlightBasketDto> fbdList = new ArrayList<>();  //사용자가 구매를 선택한 항공편들 모아놓음

	
	/*각 클래스들이 사용자의 개인정보를 가지고 있다.
	* 사용자 정보(LoginDto(Id, Password) 저장하기)
	*/
	public FlightService(LoginDto ld) {
		setLoginDto(ld); // 사용자 정보를 저장하는 메서드
	}
	//Id와 Pw 저장하기 
	public void setLoginDto(LoginDto ld) {
		this.ld.setId(ld.getId());  //Id 저장
		this.ld.setPassword(ld.getPassword());  //Pw 저장
	}
	
	/*2. 출발 날짜 정하기
	*출발 날짜를 정하는 메서드
	*PlaceService에서 destination(도착지)를 입력받음
	*/
	public void selectDate(String destination) {
		System.out.println();
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("                                          ** 날짜 선택 **                                ");
		System.out.println();
		System.out.println("현재 날짜: " + DateConverter.today());   //오늘 날짜 출력
		System.out.println("항공권 예약은 두 달 전부터 60일간 가능합니다.");
		System.out.println("오늘부터 예약 가능한 날짜들입니다.");
		CalendarView.makeCalendar();   //달력을 보는 CalendarView 클래스에서 오늘부터 두 달간의 날짜를 출력
		System.out.println("날짜를 선택해 주세요 예)05-06");  
		System.out.print("＆날짜 입력: ");  
		String date = stdIn.next();
		boolean result = DateConverter.checkDate(date); 
		//날짜가 앞으로 60일 내에 포함되지 않으면 다시 입력받게 함
		while(!result) {
			System.out.println("날짜가 올바르지 않습니다. 다시 입력해주세요.");
			System.out.print("＆날짜 입력: ");
			date = stdIn.next();
			result = DateConverter.checkDate(date);
		}
			
		System.out.println(date.substring(0,2) + "월" + date.substring(2) +"일을 선택하셨습니다.");
		System.out.println(date.substring(0,2) + "월" + date.substring(2) +"일의 항공권 검색");
		System.out.println();
		selectFlight(destination,date); //
	}

	//3. 항공편 정하기
	public void selectFlight(String destination, String depDate) {
		/*
		*도착지와 날짜를 받아와 항공권을 보여주는 메서드
		*도착지는 PlaceService에서 받음
		*날짜는 SelectDate에서 받음
		*/
		System.out.println();
		System.out.println("----------------------------------------------------------------------");
		System.out.println("                          ** 항공권 조회 **                              ");
		List<FlightDto> flightList = dao.listFlight(destination); // 도착지로 가는 항공 전부를 받아옴
		//항공권 전부 출력
		System.out.println("2023년 " + depDate + "         " + "인천(ICN)->" + destination);
		System.out.printf("%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s","항공번호","항공사","출발시간","도착시간","총시간",
				"직항여부","가격");
		System.out.println();
		//도착지로 가는 항공편 리스트의 수에 맞게 항공편 출력
		System.out.println("-----------------------------------------------------------------------");
		for(int i = 0; i < flightList.size(); i++) {
			FlightDto fd = flightList.get(i);
			int airCode = fd.getAirCode();             //항공 번호 받기
			String airlineName = fd.getAirlineName();  //항공사 이름 받기
			String depTime = fd.getDepTime();          //출발 시간 받기
			String arrivalTime = fd.getArrivalTime();  //도착 시간 받기
			String totalTime = fd.getTotalTime();      //총 비행 시간 받기
			String directFlight = fd.getDirectFlight();//직항 여부 받기
			int price = fd.getPrice();                 //가격 받기
			//항공 번호, 항공사 이름, 출발 시간, 도착 시간, 총 비행 시간, 직항 여부, 가격을 형식에 맞춰 출력
			System.out.printf("%-10d\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10d",airCode,airlineName,
					depTime,arrivalTime,totalTime,directFlight,price);
			System.out.println();
		}
		System.out.println("------------------------------------------------------------------------");
		System.out.println("메뉴 1. 날짜 다시 선택 2. 항공권 구매 3. 메인화면으로 돌아가기");
		System.out.print("＆번호 입력: ");
		int num = stdIn.nextInt();
		if(num == 2) {//항공권 구매
			System.out.println("구매하시려는 항공권의 항공번호를 입력해주세요.");
			System.out.print("＆항공번호: ");
			int airNum = stdIn.nextInt();//항공번호 입력 받음
			int i = 0;
			FlightDto selectedFlight = new FlightDto();
			while(i < flightList.size()) { //전체 항공리스트 중에서
				selectedFlight = flightList.get(i); //차례대로 하나씩 항공편을 선택하고
				if(selectedFlight.getAirCode() == airNum) break; //만약 선택된 항공편의 항공번호가 사용자가 입력한 번호와 같다면 
				//selectedFlight에 사용자가 선택한 항공편 저장한 채 while문 빠져나옴
				i++; //다음 항공편 객체로 이동
			}
			selectedFlight.setDepDate(depDate); //선택한 항공편에 출발 일자를 사용자가 선택한 출발일자로 설정
			selectSeat(selectedFlight); //그후 출발 날짜가 설정된 항공편 객체를 통째로 좌석 선택 함수로 보냄  
		}else if(num == 1) { //날짜를 다시 선택하러 전단계로 돌아감. 
			selectDate(destination); //도착지는 동일한데 날짜만 다시 설정
		}else {//메인화면으로 돌아가기
			UserMain us = new UserMain(); us.userMain(ld);  //메인화면으로 돌아가되 로그인 객체를 유지한 상태로 돌아감
		}
	}
	
	//4. 좌석 정하기
	public void selectSeat(FlightDto fd) {  
		System.out.println();
		System.out.println("                  ** 좌석 번호 확인 **                    ");
		System.out.println();
		FlightSeat fs = new FlightSeat(); //비행기 좌석을 보여주는 클래스
		fs.showBusinessSeats(); //비즈니스석을 보여준다.
		fs.showEconomySeats();  //이코노미석을 보여준다.
		System.out.println("마음에 드는 좌석을 선택해 주세요. 비즈니스석이시면 B숫자, 이코노미석이시면 E숫자를 입력해주세요.");
		System.out.print("＆좌석 번호 입력: ");
		String seatNo = stdIn.next();
		System.out.println();
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("   메뉴 1. 여행지 다시 선택 2. 항공권 구매 3. 항공권 다시 선택 4. 메인화면으로 돌아가기");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("＆메뉴 선택: ");
		int menu = stdIn.nextInt();
		if(menu==1) {//여행지 다시 선택
			selectDate(fd.getDestination());
		}else if(menu == 2) {//항공권 구매
			buyInfo(fd, seatNo);//구매하려는 항공권 정보 출력
		}else if(menu == 3){//항공권 다시 선택
			selectFlight(fd.getDestination(), fd.getDepDate()); //도착지와 출발날짜는 이미 선택된 그대로 진행
		}else {
			UserMain us = new UserMain(); us.userMain(ld); //메인 화면으로 로그인 된 상태로 이동
		}
	}
	//구매 정보 확인 메소드, 사용자가 선택한 항공편 객체와 좌석 정보를 받아옴
	//장바구니에 넣을 것인지 여부 확인
	//예를 선택하면 장바구니에 넣는다. 
	public void buyInfo(FlightDto fd, String seatNo) {
		System.out.println("                   **구매 정보 확인**                     ");
		System.out.println();
		System.out.println();
		/*
		 * MembershipDao는 사용자 아이디를 입력하면 MEMBER(회원 정보 저장) 테이블에서 사용자 Id에 맞는 이름을 찾아옴
		 */
		MembershipDao md = new MembershipDao();
		System.out.println("주문자 이름: " + md.findName(ld));
		System.out.println("항공 번호: " + fd.getAirCode());
		System.out.println("항공사 이름: " + fd.getAirlineName());
		System.out.println("총 소요 시간: " + fd.getTotalTime());
		System.out.println("날짜: " + fd.getDepDate());
		System.out.println("출발-도착: " + fd.getDepTime()+"-" + fd.getArrivalTime());
		System.out.println("좌석 번호: " + seatNo);
		System.out.println("장바구니에 넣을까요?");
		System.out.println("1. 예 2. 아니오");
		System.out.print("＆대답: ");
		int consent = stdIn.nextInt();
		if(consent == 1) {
			FlightBasketDao fbd = new FlightBasketDao(); //장바구니 테이블을 관리하는 DAO
			fbd.toFlightBasket(ld, fd, seatNo);  //장바구니에 입력
			System.out.println("바로 결제를 진행하시겠습니까? 1. 예 2. 아니오(호텔과 같이 결제할게요)");
			System.out.print("＆대답: ");
			int pay = stdIn.nextInt();
			if(pay == 1) {//사용자가 바로 결제 선택
				pickFlightBasket(); //결제할 항목을 fbdList에 저장시키는 함수
				PaymentServiceBoth2 ps = new PaymentServiceBoth2(fbdList, ld);
			}else {//사용자가 호텔과 합산 결제 선택
				System.out.println("호텔과 같이 결제될 항공권을 선택해주세요.");
				System.out.println();
				pickFlightBasket(); //호텔과 합산해서 결제될 항목을 fbdList에 저장시키는 함수
				System.out.println("메뉴 1. 장바구니 비우기 2. 날짜 다시 선택하기 3. 항공편 추가 선택하기 4. 호텔 예약");
				System.out.print("＆대답: ");
				int menu = stdIn.nextInt();
				switch(menu) {
				case 1: fbd.deleteAllBasket(ld); newPage(); break;  //장바구니를 비우고 추가 메뉴를 선택
				case 2: selectDate(fd.getDestination()); break;     
				case 3: selectFlight(fd.getDestination(), fd.getDepDate()); break;
				case 4: ResortService rs = new ResortService(fbdList, ld);
				}
			}
		}else {//장바구니에 넣기를 선택 안함. 항공권 구매 의사X인 경우
			System.out.println("메뉴 1. 메인화면으로 돌아가기 2. 날짜 다시 선택하기 3. 항공편 추가 선택하기 4. 호텔 예약");
			System.out.print("＆대답: ");
			int menu = stdIn.nextInt();
			switch(menu) {
			case 1: UserMain us = new UserMain(); us.userMain(); newPage(); break; //메인화면으로 가기
			case 2: selectDate(fd.getDestination()); break;
			case 3: selectFlight(fd.getDestination(), fd.getDepDate()); break;
			case 4: ResortService rs = new ResortService(ld);//항공권 리스트에는 아무것도 저장되 있지 않음 바로 호텔 예약으로만 감
			}
		}
	}
	//항공권을 전부 비운 후 새로운 메뉴 출력
	public void newPage() {
		System.out.println("항공권 장바구니를 전부 비웠습니다.");
		System.out.println("메뉴 1. 선택한 항공권과 함께 호텔 예약하러 가기 2. 메인화면으로 돌아가기 3. 종료");
		System.out.print("＆대답: ");
		int n =stdIn.nextInt();
		switch(n) {
		case 1: ResortService rs = new ResortService(fbdList,ld); break; //전역변수에 저장되어 있는 항공편 리스트와 사용자 객체를 전달
		case 2: UserMain us = new UserMain(); us.userMain(ld); break; //사용자 객체만 메인 화면에 전달
		case 3: Exit exit = new Exit(ld); //사용자 객체를 지닌 채로 로그아웃 - 로그아웃 테이블에서 사용자 객체 사라짐
		}
	}
	/*
	 * 5. 항공권 구매(복수 선택 가능)
	 * 결제할 항공권을 전역변수 fbdList에 담는다. 
	 */
	public void pickFlightBasket() {
		FlightBasketDao fbd = new FlightBasketDao(); //결제할 항공편을 사용자 장바구니에 담을 DAO
		System.out.println("                          ** 결제할 품목 선택 **                        ");
		System.out.println("몇 번 상품을 결제 페이지에 담을까요?");
		System.out.println("종료하시려면 -1을 입력해주세요");
		System.out.println("");
		

		int n= 0;
		while(n != -1){ //종료 -1, 상품 번호에 -1입력하면 빠져나옴 
			watchBasket();
			System.out.print("＆상품 번호(종료:-1):");
			n = stdIn.nextInt();
			if(n == -1) break;
			FlightBasketDao dao = new FlightBasketDao();
			List<FlightBasketDto> flightBasketList = dao.flightBasketList(ld); //사용자 아이디(ld에 저장)로 장바구니에 있는 항공편 불러오기
			//항공편 앞에는 번호가 있으므로 번호를 입력하면  
			FlightBasketDto nth = flightBasketList.get(n); //선택한 번호를 항공편 장바구니 리스트에서 불러옴
			fbdList.add(nth);                    //전역 변수에 저장
			fbd.deleteFlightBasket(nth);         //이미 선택한 항공편은 장바구니에서 삭제
		}
	}

	//장바구니 보기
	public void watchBasket() {
		FlightBasketDao dao = new FlightBasketDao();  //장바구니 테이블(FLIGHT_BASKET)을 관리하는 DAO
		List<FlightBasketDto> flightBasketList = dao.flightBasketList(ld); //DAO에서 항공권 장바구니 리스트 가져옴
		System.out.printf("%-5s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s",
				"번호","항공 번호","도착지","항공사 이름","출발 시간",
				"도착 시간","총 비행 시간","직항 여부","가격","사용자 Id","출발 일자","좌석 번호");  //항목들을 포맷에 맞춰 출력
		System.out.println();
		int i = 0;
		for(FlightBasketDto fbd: flightBasketList) {  //DAO가 가져온 항공권 장바구니 리스트를 순서대로 1개씩
			int airCode = fbd.getAirCode();           //항공번호
			String destination = fbd.getDestination();//도착지
			String airlineName = fbd.getAirlineName();//항공사 이름
			String depTime = fbd.getDepTime();        //출발 시간
			String arrivalTime = fbd.getArrivalTime();//도착 시간
			String totalTime = fbd.getTotalTime();    //총 비행 시간
			String directFlight = fbd.getDirectFlight();//직항 여부
			int price = fbd.getPrice();               //가격
			String userId = fbd.getUserId();          //사용자 아이디
			String depDate = fbd.getDepDate();        //출발 일자
			String seatNo = fbd.getSeatNo();          //좌석 번호
			System.out.printf("%-5s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\t%-10s\n",
					i++,airCode, destination, airlineName, depTime, arrivalTime, totalTime, directFlight, price, userId,
					depDate, seatNo);//양식에 맞춰 출력
		}
	}
}
