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
public class PaymentServiceBoth3 {
	OrderDao od = new OrderDao();
	Scanner stdIn = new Scanner(System.in);
	ResortReservationDao rrd =new ResortReservationDao();
	Scanner scanner = new Scanner(System.in);
	List<ResortBasketDto> rbdList = new ArrayList<>();
	LoginDto ld = new LoginDto();
	int payMoney = 0;
	int secureNumber = 0;
	static int totalMoney = 0;
	MembershipDao mdao = new MembershipDao();
	
	public int getTotalMoney() {
		return totalMoney;
	}

	public PaymentServiceBoth3(List<ResortBasketDto> abdList, LoginDto ld) {
		setLoginDto(ld);                   //로그인 객체를 복사
		for(ResortBasketDto rd : abdList) {
			ResortBasketDto rbd = new ResortBasketDto();
			rbd.setNights(rd.getNights());
			rbd.setResortName(rd.getResortName());
			rbd.setResortPlace(rd.getResortPlace());
			rbd.setResortPrice(rd.getResortPrice());
			rbd.setRoomName(rd.getRoomName());
			rbd.setUseDate(rd.getUseDate());
			rbd.setUserId(rd.getUserId());
			rbdList.add(rbd);
		}
		System.out.println(rbdList.size());
		makeOrderFromBasket2();
		init();
	}
	
	
	public void setLoginDto(LoginDto ld) {   //로그인 객체를 복사
		this.ld.setId(ld.getId());
		this.ld.setPassword(ld.getPassword());
	}
	

	public int getPayMoney() {
		return this.payMoney;
	}
	
	public void setSecureNumber(int secureNumber) {
		this.secureNumber = secureNumber;
	}
	
	//호텔 객체만 주문 객체로 만들기
	public void makeOrderFromBasket2() {
		for(int i = 0; i < rbdList.size(); i++) {
			OrderDao od = new OrderDao();
			od.makeResortOrder(rbdList.get(i));
		}
	}
	
	private void init() {
		System.out.println("----------------------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.println("                        ** 결제 **                          ");
		System.out.println();
		System.out.println("결제 방식을 선택해주세요.");
		System.out.println("▶1.무통장 입금 ▶2.신용카드 결제 ▶3.종료");
		System.out.print("＆번호 입력: ");
		String number = scanner.nextLine();
		while(!Pattern.matches("^[1-3]", number)||number.length()!=1) {
			System.out.println("결제 방식을 다시 선택해주세요.");
			System.out.println("메뉴 1.무통장 입금 ▶2.신용카드 결제");
			System.out.print("＆번호 입력: ");
			number = scanner.nextLine();
		}
		int number2 = Integer.parseInt(number);
		
		switch(number2) {
		case 1: deposit(); break;
		case 2: creditPay(); break;
		case 3: Exit exit = new Exit(ld);
		}	
	}
	public void creditPay() {
		System.out.println("               ** 신용카드 결제 **                ");
		System.out.println(" 신용카드 결제는 익월 청구됩니다. ");
		orderInfo();
		System.out.println("신용카드를 선택해 주세요");
		System.out.println("1. 신한카드    2. 롯데카드    3. 국민카드   4. 농협카드");
		System.out.println("5. 현대카드    6. 삼성카드    7. 하나카드   8. 우리카드");
		System.out.print("카드 선택: ");
		int cardType = stdIn.nextInt();
		System.out.println("카드 비밀번호(4자리)를 입력해주세요");
		System.out.print("카드 비밀번호: ");
		String cardPw = scanner.next();
		
		if(cardPw.length()!=4) {
			System.out.println("잘못 입력하셨습니다. 다시 입력해주세요");
			System.out.print("카드 비밀번호: ");
			cardPw = scanner.nextLine();
		}
		System.out.println("개인정보 보호를 위해 " +  mdao.findName(ld)+ "님께 번호를 발송했습니다. 번호 2자리를 입력해주세요.");
		PaymentThread2 pt2 = new PaymentThread2(this);
		pt2.run();
		System.out.println("보안 문자를 입력해주세요");
		System.out.print("번호(2자리) 입력:");
		int number = scanner.nextInt();
		if(number == secureNumber) {
			System.out.println("결제가 성공적으로 완료되었습니다.");
			if(this.rbdList != null) {
				rrd.moveFromBasketToResort(this.rbdList);
			}
			od.deleteOrder(ld);
			System.out.println("메뉴 1. 예약 내역 보기 2. 메인 화면으로 돌아가기.");
			System.out.print("＆메뉴 입력:");
			int num = stdIn.nextInt();
			switch(num) {
			case 1: ReservationService rs = new ReservationService(ld); rs.seeReservation(); break;
			case 2: PlaceService ps = new PlaceService(ld); break;
			}
		}else {
			System.out.println("인증에 실패했습니다. 초기화면으로 돌아갑니다.");
			init();
		}
	}
		
	public void deposit() {
		System.out.println("                ** 무통장 입금 **                  ");
		System.out.println("아래의 계좌로 기한 내 입금해주세요.");
		System.out.println("-------------------------------------------------");
		System.out.print("입금 기한: ");
		System.out.print(DateConverter.untilDate());
		System.out.println("               입금 계좌                  ");
		depositInfo();
		PaymentThread t = new PaymentThread(mdao.findName(ld), totalMoney, ld);
		t.run();
		rrd.moveFromBasketToResort(this.rbdList);  // 결제가 성공한 후에 예약 내역으로 옮김
		od.deleteOrder(ld);
		System.out.println("메뉴 1. 예약 내역 보기 2. 메인 화면으로 돌아가기.");
		System.out.print("＆메뉴 입력:");
		int num = stdIn.nextInt();
		switch(num) {
		case 1: ReservationService rs = new ReservationService(ld); rs.seeReservation(); break;
		case 2: PlaceService ps = new PlaceService(ld); break;
		}
	}
	
	public void orderInfo() {
		MembershipDao md = new MembershipDao();
		OrderDao od = new OrderDao();
		List<OrderDto> orderList = od.listOrder(ld);
		String name = md.findName(ld);
		
		System.out.println();
		System.out.println("                    ** 결제 정보 확인 **                     ");
		int i = 0;
		totalMoney = 0;
		for(OrderDto odt : orderList) {
			System.out.println("주문 번호: " + "[" + (++i) + "]");
			System.out.print("입금자명: " + name);                  //입금자명
			System.out.println("상품명: " +  odt.getProdName());    //상품이름
			System.out.println("상품 가격: " + odt.getProdPrice()); //상품가격
			totalMoney += odt.getProdPrice();                     //금액은 총액으로 결제
		}
	}
	
	public void depositInfo() {
		System.out.println("하나은행 248-12-144541 예금주: (주)삼조");
		System.out.println("우리은행 1002-637-966138 예금주: (주)삼조");
		System.out.println("국민은행 302-1577-2235-91   예금주: (주)삼조");
	}
}