package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import common.DateConverter;
import dao.FlightReservationDao;
import dao.MembershipDao;
import dao.OrderDao;
import dao.ResortReservationDao;
import dto.FlightBasketDto;
import dto.LoginDto;
import dto.OrderDto;
import dto.ResortBasketDto;
/**
 * 항공권과 호텔 두 가지 결제를 동시에 담당하는 클래스
 * 호텔 서비스(ResortService)에서 호텔과 항공권 결제를 같이 선택했을 때 가동됨
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
public class PaymentServiceBoth {
	OrderDao od = new OrderDao();            //주문 테이블(ORDER_LIST)를 관리하는 DAO
	Scanner stdIn = new Scanner(System.in);  
	FlightReservationDao frd =new FlightReservationDao();  //항공권 예약(FLIGHT_RESERV) 테이블을 관리하는 DAO
	ResortReservationDao rrd = new ResortReservationDao(); //호텔 예약(RESORT_RESERV) 테이블을 관리하는 DAO
	
	/*
	 * 호텔 서비스(ResortService)에서 호텔과 항공권 결제를 같이 선택했을 때
	 * 호텔 서비스객체가 항공권 리스트(사용자가 구매하기로 선택한 항공권들-복수)와 호텔 리스트(사용자가 구매하기로 선택한 호텔들-복수)
	 * 두 가지를 호텔 서비스 객체로부터 받아와서 전역변수로 설정한다. (전역변수로 설정하면 모든 메서드가 사용할 수 있음)
	 */
	List<FlightBasketDto> fbdList = new ArrayList<>();     
	List<ResortBasketDto> rbdList = new ArrayList<>();
	//로그인 객체도 ResortService로부터 받아옴
	LoginDto ld = new LoginDto();
	int secureNumber = 0;      //보안번호 설정, PaymentThread2(신용카드 결제를 해주는 스레드)에서 이 보안번호와 자신이 보낸 번호의 
	//일치 여부 확인
	static int totalMoney = 0; //결제해야 할 총 금액
	MembershipDao mdao = new MembershipDao();  //회원 테이블(MEMBER)를 관리하는 DAO
	
	public int getTotalMoney() {//사용자가 무통장 입금을 선택했을 때 PaymenthThread1(무통장 입금 결제를 해주는 스레드)에서 이 금액과 사용자가 
		//입력한 금액이 동일한 지 확인
		return totalMoney;
	}
	public PaymentServiceBoth(List<FlightBasketDto> abdList,List<ResortBasketDto> bbdList, LoginDto ld) {
		setLoginDto(ld);                   //로그인 객체를 복사
		for(FlightBasketDto fd : abdList) {//항공권 리스트 복사
			FlightBasketDto fbd = new FlightBasketDto();
			fbd.setAirCode(fd.getAirCode());
			fbd.setAirlineName(fd.getAirlineName());
			fbd.setArrivalTime(fd.getArrivalTime());
			fbd.setDepDate(fd.getDepDate());
			fbd.setDepTime(fd.getDepTime());
			fbd.setDestination(fd.getDestination());
			fbd.setDirectFlight(fd.getDirectFlight());
			fbd.setPrice(fd.getPrice());
			fbd.setSeatNo(fd.getSeatNo());
			fbd.setTotalTime(fd.getTotalTime());
			fbd.setUserId(fd.getUserId());
			fbdList.add(fbd);//전역변수 항공권 리스트에 담는다.
		}
		for(ResortBasketDto  rd : bbdList) {//호텔 리스트 복사
			ResortBasketDto rbd = new ResortBasketDto();
			rbd.setResortPlace(rd.getResortPlace());
			rbd.setResortName(rd.getResortName());
			rbd.setResortPrice(rd.getResortPrice());
			rbd.setRoomName(rd.getRoomName());
			rbd.setUseDate(rd.getUseDate());
			rbd.setNights(rd.getNights());
			rbd.setUserId(rd.getUserId());
			rbdList.add(rbd);//전역변수 호텔 리스트에 담는다.
		}
		makeOrderFromBasket();//주문 dao로 장바구니에 있는 객체들을 주문 리스트로 만들어서 주문 테이블(ORDER_LIST)에 저장
		//기본 설정 - 로그인 객체 복사, 결제해야 할 항공권, 호텔 리스트 전역 변수로 복사
		//후 초기 화면을 구동한다. 
		init();
		
	}
	
	public void setLoginDto(LoginDto ld) {   //로그인 객체를 복사
		this.ld.setId(ld.getId());
		this.ld.setPassword(ld.getPassword());
	}
	
	public void setSecureNumber(int secureNumber) {//클래스의 보안번호 설정
		this.secureNumber = secureNumber;
	}
	
	//장바구니 객체를 주문 객체로 만들기
	public void makeOrderFromBasket() {
		for(int i = 0; i < rbdList.size(); i++) {
			od.makeResortOrder(rbdList.get(i));   //전역변수에 저장된 호텔 객체들을 주문 리스트에 올림
		}
		for(int i = 0; i < fbdList.size(); i++) {
			od.makeFlightOrder(fbdList.get(i));   //전역변수에 저장된 항공권 객체들을 주문 리스트에 올림
		}
	}
	
	//초기 화면 - 결제 방식 1. 무통장 입금 2. 신용카드 결제 선택
	private void init() {
		System.out.println("----------------------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.println("                        ** 결제 **                          ");
		System.out.println();
		System.out.println("결제 방식을 선택해주세요.");
		System.out.println("1. 무통장 입금 2. 신용카드 결제 3. 종료");
		System.out.print("＆번호 입력: ");
		//1번이나 2번을 선택했는지 정규식으로 확인
		String number = stdIn.nextLine();
		//1번이나 2번을 선택하지 않았다면 다시 선택받음
		while(!Pattern.matches("^[1-2]", number)||number.length()!=1) {
			System.out.println("결제 방식을 다시 선택해주세요.");
			System.out.println("메뉴 1.무통장 입금 ▶2.신용카드 결제");
			System.out.print("＆번호 입력: ");
			number = stdIn.nextLine();
		}
		//1번이나 2번을 선택받은 후 숫자로 변환
		int number2 = Integer.parseInt(number);
		
		switch(number2) {
		case 1: deposit(); break;  //무통장 입금 화면
		case 2: creditPay(); break;//신용카드 결제 화면
		case 3: Exit exit = new Exit(ld);//종료
		}	
	}
	//신용카드 결제 메서드
	public void creditPay() {
		System.out.println("               ** 신용카드 결제 **                ");
		System.out.println(" 신용카드 결제는 익월 청구됩니다. ");
		orderInfo();
		System.out.println("신용카드를 선택해 주세요");
		System.out.println("1. 신한카드    2. 롯데카드    3. 국민카드   4. 농협카드");
		System.out.println("5. 현대카드    6. 삼성카드    7. 하나카드   8. 우리카드");
		int cartType;
		System.out.print("카드 선택: ");
		cartType = stdIn.nextInt();
		System.out.println("카드 비밀번호(4자리)를 입력해주세요");
		System.out.print("카드 비밀번호: ");
		String cardPw = stdIn.next();
		//카드 비밀번호가 잘못된 경우 다시 입력 받음
		while(cardPw.length()!=4) {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요");
			System.out.print("카드 비밀번호: ");
			cardPw = stdIn.nextLine();
		}
		System.out.println("개인정보 보호를 위해 " +  
		mdao.findName(ld)+ "님께 번호를 발송했습니다. 번호 2자리를 입력해주세요.");
		//카드 결제 스레드. 보안 번호를 랜덤 클래스를 이용해서 보내준다. 
		//또한 카드 결제 스레드는 이 클래스에 보안 번호를 전역변수로 입력시킴 
		//아예 클래스 자체를 전역변수로 줘서 this.setSecureNumber()를 사용할 수 있게 함 
		PaymentThread2 pt2 = new PaymentThread2(this);
		pt2.run(); //스레드 시작
		System.out.println("보안 문자를 입력해주세요");
		System.out.print("번호(2자리) 입력:");//위에 pt2가 보내준 보안번호를 입력
		int number = stdIn.nextInt();
		if(number == secureNumber) {//만약 사용자가 보안번호를 정상적으로 입력했으면
			System.out.println("결제가 성공적으로 완료되었습니다.");
			if(fbdList != null) {//항공권 리스트가 존재하는 경우
				frd.moveFromBasketToFlight(fbdList);  // 결제가 성공한 후에 항공권들을 예약 내역으로 옮김
			}
			if(rbdList != null) {
				rrd.moveFromBasketToResort(rbdList);  // 결제가 성공한 후에 호텔들을 예약 내역으로 옮김
			}
			od.deleteOrder(ld);//결제가 완료되었으므로 주문 내역은 삭제
			System.out.println("메뉴 1. 예약 내역 보기 2. 메인 화면으로 돌아가기.");
			System.out.print("＆메뉴 입력:");
			int num = stdIn.nextInt();
			switch(num) {
			//예약 내역 보기
			case 1: ReservationService rs = new ReservationService(ld); rs.seeReservation(); break;
			//로그인 된 상태에서 메인화면으로 돌아가기
			case 2: UserMain us = new UserMain(); us.userMain(ld); break;
			}
		}else {//실패한 경우 초기 화면 - 결제 방식 선택으로 되돌아감
			System.out.println("인증에 실패했습니다. 초기화면으로 돌아갑니다.");
			init();
		}
	}
	
	//무통장 입금 메서드
	public void deposit() {
		System.out.println("                ** 무통장 입금 **                  ");
		System.out.println("아래의 계좌로 기한 내 입금해주세요.");
		System.out.println("-------------------------------------------------");
		System.out.print("입금 기한: ");
		System.out.print(DateConverter.untilDate()); //DateConverter의 utilDate함수는 
		//오늘로부터 이틀 후의 날짜를 출력
		System.out.println("               입금 계좌                  ");
		depositInfo(); // 입금할 계좌와 예금주 명을 출력
		//스레드에 사용자 이름과 총 금액(호텔과 항공권 합산 금액), 사용자 정보를 전달
		PaymentThread t = new PaymentThread(mdao.findName(ld), totalMoney, ld);
		t.run();//스레드 실행
		frd.moveFromBasketToFlight(fbdList);  // 결제가 성공한 후에 항공권 리스트를 예약 내역으로 옮김
		rrd.moveFromBasketToResort(rbdList);  // 결제가 성공한 후에 호텔 리스트를 예약 내역으로 옮김
		od.deleteOrder(ld);//결제가 끝났으므로 주문 내역 삭제
		System.out.println("메뉴 1. 예약 내역 보기 2. 메인 화면으로 돌아가기.");
		System.out.print("＆메뉴 입력:");
		int num = stdIn.nextInt();
		switch(num) {
		case 1: ReservationService rs = new ReservationService(ld); rs.seeReservation(); break;
		case 2: PlaceService ps = new PlaceService(ld); break;
		}
	}
	//결제 정보 확인
	public void orderInfo() {
		MembershipDao md = new MembershipDao();   //로그인 객체에는 Id,Pw만 저장되어 있으므로 이름을 찾으려면 회원
		//Member 테이블을 관리하는 DAO가 필요
		List<OrderDto> orderList = od.listOrder(ld);
		String name = md.findName(ld);
		//주문 정보는 생성자가 실행되었을 때 이미 만들어짐
		//주문 정보는 항공권과 호텔 객체에서 중요한 정보들을 편집해서 만들어진다. 
		System.out.println();
		System.out.println("                    ** 결제 정보 확인 **                     ");
		int i = 0;
		for(OrderDto odt : orderList) {
			//주문 리스트에서 주문 객체를 하나씩 받아옴
			System.out.println("주문 번호: " + "[" + (++i) + "]");
			System.out.print("입금자명: " + name);                  //입금자명
			System.out.println("상품명: " +  odt.getProdName());    //상품이름
			System.out.println("상품 가격: " + odt.getProdPrice()); //상품가격
			totalMoney += odt.getProdPrice();                     //금액은 총액으로 결제하면 된다. totalMoney는 
			//static으로 설정되어 있어서 이 정보를 계속 기억함.
		}
	}
	
	//예금주 명과 계좌번호를 출력
	public void depositInfo() {
		System.out.println("하나은행 248-12-144541 예금주: (주)삼조");
		System.out.println("우리은행 1002-637-966138 예금주: (주)삼조");
		System.out.println("국민은행 302-1577-2235-91   예금주: (주)삼조");
	}
}