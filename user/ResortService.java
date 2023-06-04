package user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import common.CalendarView;
import common.DateConverter;
import dao.FlightBasketDao;
import dao.ResortBasketDao;
import dao.ResortDao;
import dao.ResortReviewDao;
import dto.FlightBasketDto;
import dto.LoginDto;
import dto.ResortBasketDto;
import dto.ResortDto;
import dto.ResortReviewDto;
/**
 * 호텔 예약/조회 클래스
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
public class ResortService {
	Scanner stdIn = new Scanner(System.in);
	ResortDao dao = new ResortDao();       //호텔 테이블(RESORT)을 관리하는 DAO
	LoginDto ld = new LoginDto();          //로그인 객체 생성- 전역변수로서 로그인 정보를 저장
	List<FlightBasketDto> fbdListAll = new ArrayList<>();//앞선 단계에서 사용자가 선택한 항공권 리스트 저장
	List<ResortBasketDto> rbdListAll = new ArrayList<>();//호텔 예약/조회 단계에서 사용자가 선택한 호텔 리스트 저장 
	String destination = "";  //도착지 설정 - 도착지에 맞는 호텔 리스트만 불러옴
	
	// 로그인 객체 복사
	public void setLoginDto(LoginDto ld) {
		this.ld.setId(ld.getId());//Id 복사
		this.ld.setPassword(ld.getPassword());//Pw 복사
	}
	//사용자 로그인 객체만 입력받는 생성자
	//로그인 객체를 입력받아 호텔을 예약할 지역을 선택, 이 때 항공권 리스트는 들어오지 않으므로 항공권을 앞선 단계에서 결제했거나
	//또는 호텔만 단독 선택한 경우이다. 
	public ResortService(LoginDto ld) {
		setLoginDto(ld);
		//지역명이 들어있는 리스트 생성
		String[] areaList = {"베이징","뉴욕","런던","호치민"};
		//맵으로 지역과 번호를 집어넣음
		Map<Integer, String> areaMap = new HashMap<>();
		for(int i=0;i<4;i++){
			//areaMap을 출력
	        areaMap.put(i, areaList[i]);
	    }
		System.out.println("호텔을 예약하실 지역을 선택해주세요.");
		System.out.println();
		System.out.println("번호         도시");
		System.out.println("---------------------");
		//맵으로 번호와 지역 출력
		for(int i : areaMap.keySet()) {
			System.out.println(i + "          " + areaMap.get(i));
			System.out.println();
		}
		System.out.print("＆번호 입력: ");
		int n = stdIn.nextInt();
		//번호를 입력받으면 클래스 전역변수의 지역을 설정하게 된다.
		switch(n) {
		case 0 : this.destination = "베이징"; break;
		case 1 : this.destination = "뉴욕"; break;
		case 2 : this.destination = "런던"; break;
		case 3 : this.destination = "호치민"; break;
		}
		//지역에 맞는 호텔들만 보여줌
		showResort(destination);
	}
	//호텔 리스트와 사용자 로그인 객체를 입력받는 생성자
	public ResortService(List<FlightBasketDto> abdList, LoginDto ld) {
		setLoginDto(ld); //로그인 객체 복사
		//항공권 리스트 복사
		for(int i = 0;i < abdList.size(); i++) {
			FlightBasketDto fbd = new FlightBasketDto();
			FlightBasketDto fbd2 = abdList.get(i);
			fbd.setAirCode(fbd2.getAirCode());
			fbd.setAirlineName(fbd2.getAirlineName());
			fbd.setArrivalTime(fbd2.getArrivalTime());
			fbd.setDepDate(fbd2.getDepDate());
			fbd.setDepTime(fbd2.getDepTime());
			fbd.setDestination(fbd2.getDestination());
			fbd.setDirectFlight(fbd2.getDirectFlight());
			fbd.setPrice(fbd2.getPrice());
			fbd.setSeatNo(fbd2.getSeatNo());
			fbd.setTotalTime(fbd2.getTotalTime());
			fbd.setUserId(fbd2.getUserId());
			fbdListAll.add(fbd);
		}
		//지역 선택
		String[] areaList = {"베이징","뉴욕","런던","호치민"};//지역명이 담긴 리스트
		Map<Integer, String> areaMap = new HashMap<>();
		//맵으로 번호와 함께 저장
		for(int i=0;i<4;i++){
	        areaMap.put(i, areaList[i]);
	    }
		System.out.println("호텔을 예약하실 지역을 선택해주세요.");
		System.out.println();
		System.out.println("번호         도시");
		System.out.println("---------------------");
		//번호와 지역을 출력
		for(int i : areaMap.keySet()) {
			System.out.println(i + "          " + areaMap.get(i));
			System.out.println();
		}
		System.out.print("＆번호 입력: ");
		int n = stdIn.nextInt();
		switch(n) {//번호를 입력하면 지역 설정
		case 0 : this.destination = "베이징"; break;
		case 1 : this.destination = "뉴욕"; break;
		case 2 : this.destination = "런던"; break;
		case 3 : this.destination = "호치민"; break;
		}
		showResort(destination);//도착지에 맞는 호텔만 보여줌
	}
	
	//도착지(destination)을 인자로 받아 호텔을 출력하는 함수
	public void showResort(String destination) {
		System.out.printf("%s%30s%30s%30s%30s%25s%20s\n","번호","호텔명","룸타입","숙박인원","특징","가격","평점");
		System.out.println("-----------------------------------------------------------------------"
				+"-------------------------------------------------------------------------"
				+"---------------------------");
		System.out.println();
		List<ResortDto> resortList = dao.resortList(destination); //호텔 리스트를 받아옴
		//양식에 맞게 출력
		for(int i = 0; i < resortList.size();i++) {
			ResortDto rd = resortList.get(i);
			int price = rd.getResortPrice();
			String name = rd.getResortName();
			String roomName = rd.getRoomName();
			String accomodation = rd.getAccomodation();
			String feature = rd.getFeature();
			double customerRating = rd.getCustomerRating();
			System.out.printf("%d  ",i);
			System.out.printf("                  %-30s\t%-30s\t%-20s\t%-25s\t%-20s\t%-20s",name,
					roomName,accomodation,feature,price+"",customerRating+"");
			System.out.println();
			System.out.println();
		}
		System.out.println("-----------------------------------------------------------------------"
				+"-------------------------------------------------------------------------"
				+"---------------------------");	
		System.out.println("메뉴 1. 리뷰 확인하기 2. 호텔 장바구니에 담기 3. 메인 화면으로 돌아가기");
		System.out.print("＆메뉴 입력: ");
		int n = stdIn.nextInt();
		switch(n) {
		case 1: watchReview(); break;
		case 2: putBasketResort(); break;
		case 3: UserMain us = new UserMain(); us.userMain(ld); break;
		}
	}
	// 호텔 리뷰 보기
	public void watchReview() {
		ResortReviewDao rrdao = new ResortReviewDao();  //호텔 리뷰 리스트를 가져올 DAO - 호텔 리뷰(RESORT) 테이블 관리
		ResortDao rdao = new ResortDao();  //호텔 리스트를 가져올 DAO - 호텔 테이블(RESORT) 관리
		System.out.println("리뷰를 확인할 호텔을 선택해주세요");
		System.out.print("＆번호 입력: ");
		int n = stdIn.nextInt();
		List<ResortDto> resortList = rdao.resortList(destination); //호텔 리스트 가져옴
		//사용자가 선택한 객체
		ResortDto hotel = resortList.get(n);//사용자가 선택한 호텔 지정
		String hotelName = hotel.getResortName();//사용자가 선택한 호텔 이름 받기

		//ResortReviewDao를 통해 호텔 이름으로 호텔 리뷰 객체를 받아옴
		List<ResortReviewDto> hotelReviewList = rrdao.resortReviewList(hotelName);
		System.out.println();
		System.out.println("호텔명: "+hotel.getResortName());
		System.out.println(hotelReviewList.size() + "개의 후기가 있습니다.");
		//리뷰 별로 양식에 맞춰 출력
		for(int i = 0; i < hotelReviewList.size(); i++) {
			ResortReviewDto rrd = hotelReviewList.get(i);
			String resortPlace = rrd.getResortPlace();
			String resortName = rrd.getResortName();
			String userId = rrd.getUserId();
			String useDate = rrd.getUseDate();
			double userRating = rrd.getUserRating(); 
			String userComment = rrd.getUserComment();
			System.out.printf("[" + (i + 1) + "]");
			System.out.println("호텔 위치: " + resortPlace);
			System.out.println("호텔명: " + resortName);
			System.out.println("사용자 Id: " +userId + " 사용 일자:" + useDate);
			System.out.println("사용자 평가: " + userRating);
			System.out.println("후기: " + userComment);
			System.out.println();
		}
		System.out.println("메뉴 1. 다른 후기 보기 2. 호텔 장바구니에 담기");
		System.out.print("메뉴 입력: ");
		n = stdIn.nextInt();
		if(n==1) {
			showResort(destination);//다른 후기를 보기 위해서 먼저 도착지의 호텔 리스트로 이동
		}else {
			putBasketResort();
		}
	}
	
	// 호텔 장바구니에 호텔 담기
	public void putBasketResort() {
		ResortDao rdao = new ResortDao();
		System.out.println("마음에 드는 호텔을 선택해주세요");
		System.out.print("＆번호 입력: ");
		int num = stdIn.nextInt();
		//지역명으로 호텔 리스트 가져오기
		List<ResortDto> resortList = rdao.resortList(destination);
		//사용자가 선택한 객체
		ResortDto hotel = resortList.get(num);
		//날짜 입력받기
		System.out.println("예약하시고 싶은 날짜를 골라주세요");
		CalendarView.makeCalendar();
		System.out.println("마음에 드는 날짜를 입력해 주세요 예)05-06");
		System.out.print("＆날짜 입력: ");
		System.out.println("날짜를 선택해 주세요 예)05-06");
		System.out.print("＆날짜 입력: ");
		String date = stdIn.next();
		//날짜가 앞으로 60일 내에 포함되지 않으면 다시 입력받게 함
		boolean result = DateConverter.checkDate(date);
		while(!result) {
			System.out.println("날짜가 올바르지 않습니다. 다시 입력해주세요.");
			System.out.print("날짜 입력:");
			date = stdIn.next();
			result = DateConverter.checkDate(date);//날짜 양식 다시 체크
		}
		//숙박 일수 설정
		System.out.println("몇 일동안 묵으실 예정인가요? 숙박 일수를 선택해주세요.");
		System.out.print("＆숙박 일수: ");
		int nights = stdIn.nextInt();
		//호텔 장바구니 객체 생성
		ResortBasketDao rbd = new ResortBasketDao(); //호텔 장바구니(RESORT_BASKET) 테이블을 관리하는 DAO
		rbd.toResortBasket(ld, hotel, nights, date); //로그인 객체와 호텔 객체, 숙박일자와 날짜를 설정해서 
		//호텔 장바구니 테이블에 입력
		System.out.println("장바구니에 성공적으로 저장되었습니다.");
		System.out.println("장바구니로 이동하시겠습니까?");
		System.out.println("메뉴 1. 장바구니로 이동 2. 추가 객실 선택");
		System.out.print("＆메뉴 선택: ");
		int n = stdIn.nextInt();
		switch(n) {
		case 1: pickResortBasket(); break;
		case 2: ResortService rd = new ResortService(ld); break; //초기화면으로 이동
		}
		
	}
	
	//호텔 장바구니 리스트를 출력하는 메서드
	//호텔 장바구니 리스트를 입력하면 자동으로 호텔의 정보를 출력한다. 
	public void basketPrint(List<ResortBasketDto> rbd) {
		System.out.println("                         ** 장바구니 리스트 **                        ");
		System.out.println();
		System.out.println();
		System.out.printf("%-10s\t%-10s\t%-10s%-15s\t%s\t%-10s\t%-20s\t%-20s\n","번호","호텔 위치", "호텔 이름", "객실 이름",
				"숙박일수", "호텔 가격", "사용자 Id", "체크인 날짜");
		for(int i = 0; i < rbd.size(); i++) {
			ResortBasketDto basketD = rbd.get(i);  //호텔 장바구니 객체를 순서대로 받음
			String resortPlace = basketD.getResortPlace(); //호텔 위치(국가명)
			String resortName = basketD.getResortName();   //호텔 이름
			String roomName = basketD.getRoomName();       //객실 이름
			int nights = basketD.getNights();              //숙박 일수
			int resortPrice = basketD.getResortPrice();    //객실 가격
			String userId = basketD.getUserId();           //사용자 아이디
			String useDate = basketD.getUseDate();         //사용 일자 
			//양식대로 출력
			System.out.printf("%-10d\t%-10s\t%-10s%-15s\t%d\t%-10d\t%-20s\t%-20s\n",i, resortPlace, resortName, roomName,
					nights, resortPrice, userId, useDate);
		}
	}
	
	//결제할 호텔 장바구니 객체를 선택하는 메서드 - 번호로 선택
	public void pickResortBasket() {
		ResortBasketDao rbdd = new ResortBasketDao();  //호텔 장바구니(RESORT_BASKET) 테이블을 관리하는 DAO
		List<ResortBasketDto> rbList = rbdd.resortBasketList(ld); //호텔 장바구니 리스트 가져오기
		System.out.println("                          ** 결제할 품목 선택 **                        ");
		basketPrint(rbList);//호텔 장바구니에 담긴 호텔 리스트 출력
		System.out.println("몇 번 상품을 결제페이지에 담을까요?");
		System.out.print("＆상품 번호:");
		int n = stdIn.nextInt();
		ResortBasketDto rbd = rbList.get(n);//결제 페이지에 담을 호텔 선택
		rbdListAll.add(rbd);//전역변수(결제할 호텔들만 모아놓음)에 선택한 호텔 저장
		System.out.println("종료하시려면 -1을 입력해주세요");
		rbdd.deleteFromBasket(rbd);//선택한 호텔은 이미 결제할 호텔 변수에 담았으므로 삭제
		while(true) {//호텔 번호 복수 선택
			rbList = rbdd.resortBasketList(ld); //호텔 장바구니 리스트 다시 가져오기(사용자가 선택한 객체를 삭제했으므로 다시
			//가져옴)
			System.out.println("                             ** 장바구니 리스트 **                            ");
			System.out.printf("%-10s\t%-10s\t%-10s%-15s\t%s\t%-10s\t%-20s\t%-20s\n","번호","호텔 위치", "호텔 이름", "객실 이름",
					"숙박일수", "호텔 가격", "사용자 Id", "체크인 날짜");
			for(int i = 0; i < rbList.size(); i++) {//호텔 장바구니 리스트 중에서
				ResortBasketDto basketD = rbList.get(i); //호텔 정보를 가져오기
				String resortPlace = basketD.getResortPlace();
				String resortName = basketD.getResortName();
				String roomName = basketD.getRoomName();
				int nights = basketD.getNights();
				int resortPrice = basketD.getResortPrice();
				String userId = basketD.getUserId();
				String useDate = basketD.getUseDate();
				//호텔 정보를 양식대로 출력
				System.out.printf("%-10d\t%-10s\t%-10s%-15s\t%d\t%-10d\t%-20s\t%-20s\n",i, resortPlace, resortName, roomName,
						nights, resortPrice, userId, useDate);
			}
			System.out.print("＆상품 번호: ");
			n = stdIn.nextInt();
			if(n == -1 || rbList.size()==0 ) break;//만약 사용자가 -1을 입력하거나 중간에 호텔 장바구니리스트의 크기가 0이되면 자동 종료
			ResortBasketDto basket = rbList.get(n);
			rbdListAll.add(basket);                     //전역 변수에 저장
			rbdd.deleteFromBasket(basket);              //장바구니 객체를 삭제
		}
		System.out.println("메뉴 1. 항공권과 같이 결제하기 2. 호텔만 결제하기 3. 메인화면으로 이동하기 4. 종료하기");
		System.out.print("＆메뉴 선택: ");
		int menu = stdIn.nextInt();
		switch(menu) {
		case 1: makePayment(); break; //항공권과 같이 결제하기
		case 2: PaymentServiceBoth3 b3 = new PaymentServiceBoth3(rbdListAll, ld); break;
		//호텔만 담아서 결제 페이지에 전달
		case 3: UserMain us = new UserMain(); us.userMain(ld); break;
		case 4: Exit exit = new Exit(ld); break;
		}
	}
	
	//결제화면으로 이동하는 클래스
	public void makePayment() {
		//항공권 정보를 확인. 만약 항공권 리스트가 선택된 채로 이 클래스로 들어왔다면 
		if(fbdListAll.size()!=0) {
			//항공권 리스트와 호텔리스트를 결제 화면으로 이동시킴
			PaymentServiceBoth ps = new PaymentServiceBoth(fbdListAll, rbdListAll, ld);  //자동으로 결제화면으로 이동
		}else {//항공권 리스트가 저장되어 있지 않은 경우. 즉 항공권을 선택하지 않은 채로 이 클래스로 들어왔다면
			System.out.println("선택하신 항공 상품이 없습니다.");
			System.out.println("호텔만 결제가 진행됩니다.");
			//호텔 리스트만 결제 화면으로 이동시킴
			PaymentServiceBoth3 ps3 = new PaymentServiceBoth3(rbdListAll,ld);
		}
	}
}