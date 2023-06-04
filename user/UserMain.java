package user;

import java.util.Scanner;

import art.AsciiArt;
import dto.LoginDto;

public class UserMain {
	LoginDto ld = new LoginDto();
	Scanner stdIn = new Scanner(System.in);
	public static void main(String[] args) {
		UserMain userMain = new UserMain();
		userMain.userMain();
	}
	
	//로그인을 하지 않은 상태의 메인 화면
	public void userMain() {
		System.out.println();
		AsciiArt.logo();
		AsciiArt.cuteRabbit();
		System.out.println("환영합니다. 당신의 여행메이트 Threevago입니다. 회원가입을 하시면 다양한 항공편,호텔을 즉석에서 검색하고 결제하실 수 있습니다.");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("  메뉴 선택 1. 회원가입 2. 로그인  3. 종료");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("＆번호 입력:");
		int n = stdIn.nextInt();
		switch(n) {
		case 1: Membership ms = new Membership(); ms.membership(); break;
		case 2: Login ls = new Login(); ls.login(); break; 
		case 3: Exit exit = new Exit();  
		}
	}
	
	//로그인을 한 상태의 메인 화면
	public void userMain(LoginDto ld) {
		this.ld.setId(ld.getId());
		this.ld.setPassword(ld.getPassword());
		System.out.println();
		AsciiArt.logo();
		AsciiArt.cuteRabbit();
		System.out.println("다시 메인 화면으로 돌아오셨습니다. 원하는 메뉴를 선택해 주세요.");
		System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
		System.out.println("  메뉴 선택 1. 항공권 조회/예약 2. 호텔 조회/예약  3. 호텔 리뷰 작성하기 4. 종료");
		System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.print("＆번호 입력:");
		int n = stdIn.nextInt();
		switch(n) {
		case 1: PlaceService ps = new PlaceService(ld); break;
		case 2: ResortService rs = new ResortService(ld); break;
		case 3: ReservationService resService = new ReservationService(ld); resService.createReview(); break; 
		case 4: Exit exit = new Exit(ld);
		}
	}
}