package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dao.MembershipDao;
import dao.OrderDao;
import dto.LoginDto;
import dto.OrderDto;
/**
 * 무통장 입금 결제 스레드
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
public class PaymentThread implements Runnable{
	String userName = "";        // 사용자 이름
	int totalMoney = 0;          // 총액 확인용
	LoginDto ld = new LoginDto();// 로그인 객체
	OrderDao od = new OrderDao();// 주문 테이블(ORDER_LIST)을 관리하는 DAO
	//사용자 이름과 총액, 로그인 객체를 입력 받는 생성자
	public PaymentThread(String userName, int totalMoney, LoginDto ld) {
		this.userName = userName;      //사용자 이름 설정
		this.totalMoney = totalMoney;  //총액 설정
		setLoginDto(ld);               //로그인 정보 설정
	}
	
	public void setLoginDto(LoginDto ld) {
		this.ld.setId(ld.getId());               //Id 설정
		this.ld.setPassword(ld.getPassword());   //Pw 설정
	}

	// 결제 정보 출력
	public void orderInfo() {
		OrderDao od = new OrderDao(); //주문 테이블(ORDER_LIST)을 관리하는 DAO
		List<OrderDto> orderList = od.listOrder(ld);//주문 테이블에서 정보들을 가져옴
		
		System.out.println();
		System.out.println("                    ** 결제 정보 확인 **                     ");
		int i = 0;
		totalMoney = 0;
		for(OrderDto odt : orderList) {
			System.out.println("주문 번호: " + "[" + (i++) + "]");
			System.out.print("입금자명: " + userName);               //입금자명
			System.out.println("상품명: " +  odt.getProdName());    //상품이름
			System.out.println("상품 가격: " + odt.getProdPrice()); //상품가격
			totalMoney += odt.getProdPrice();                     //금액은 총액으로 결제
		}
	}
	
	
	Scanner stdIn = new Scanner(System.in);
	@Override
	// 결제 스레드
	// 사용자로부터 입금액을 입력받고 입금액이 결제 총액과 일치하는 경우에만 결제 성공 - 완료후 PaymentServiceBoth로 돌아감 
	public void run() {
		System.out.println("무통장 입금을 선택하셨습니다.");
		System.out.println("지금 바로 결제를 하시겠습니까? 1. 예 2. 아니오");
		System.out.println("지금 바로 결제를 하지 않으시면 장바구니에 있는 품목은 다시 선택하셔야 합니다.");
		System.out.print("＆메뉴 선택: ");
		int n = stdIn.nextInt();
		if(n == 1) {//바로 결제
			try {
				System.out.println("잠시만 기다려 주세요. 무통장 입금 모듈을 가동하고 있습니다.");
				Thread.sleep(2500);
				System.out.println(">>>>>>>>>>>>>>>>>  로딩중 >>>>>>>>>>>>>>>>>");
				System.out.println("결제 정보를 가져옵니다.");
				Thread.sleep(1000);
				orderInfo();
				System.out.println("입금하실 은행을 선택해주세요");
				System.out.println("1.하나은행 2. 우리은행  3.국민은행");
				int m = stdIn.nextInt();
				System.out.print("입금계좌: ");
				switch(m) {
				//사용자가 은행을 선택하면 자동으로 입금계좌 입력됨
				case 1: System.out.println("248-12-144541"); break;
				case 2: System.out.println("1002-637-966138"); break;
				case 3: System.out.println("302-1577-2235-91"); break;
				}
				//PaymentService에서 받은 총 결제 금액 고지
				System.out.println("총 결제 금액은 " + totalMoney +"입니다.");
				System.out.print("금액:");
				int money = stdIn.nextInt();
				while(true) {//금액 일치하지 않으면 계속 반복
					if(money == totalMoney) {  //금액 일치시
						System.out.println("결제가 성공적으로 완료되었습니다.");
						break;//원래의 클래스로 돌아감
					}else {
						System.out.println("금액이 일치하지 않습니다. 정확한 금액을 입력해주세요.");
						System.out.println("＆금액:");
						money = stdIn.nextInt();
					}
				}
			} catch(Exception e) {
				
			}
		}else {//바로 결제 실패시
			System.out.println("결제가 완료되지 않았습니다. 결제를 하지 않았을 경우, 예약은 되지 않습니다.");
			try{
				OrderDao od = new OrderDao();
				od.deleteOrder(ld);//차후에 사용자가 다시 선택하도록 주문 내역 지움
			}catch(Exception e) {
				
			}
			return;//PaymentServiceBoth로 돌아감
		}
	}
}
