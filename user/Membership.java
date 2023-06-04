package user;

import java.util.Scanner;

import dao.MembershipDao;
import dto.MembershipDto;
/**
 * 회원가입 클래스
 * 사용자가 입력한 정보를 토대로 회원(MEMBER)테이블에 집어넣습니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 *
 */
public class Membership {
	Scanner stdIn = new Scanner(System.in);
	MembershipDao dao = new MembershipDao();  //회원(MEMBER) 테이블을 관리하는 DAO
	public void membership() {
		System.out.println("---------------------------------------------------------------------");
		System.out.println("                                ** 회원가입 **                        ");
		System.out.println("✔ 이름을 입력해주세요.");
		System.out.print("이름 입력: ");
		String name = stdIn.nextLine();
		System.out.println();
		
		System.out.println("✔ 아이디를 입력해주세요.");
		System.out.print("아이디 입력: ");
		String id = stdIn.nextLine();
		/*
		 * 사용자 id의 중복 여부를 검사하는 메서드
		 * 만약 id가 Member 테이블에 존재하지 않는다면 0 존재한다면 1을 반환한다. 
		 */
		int count = dao.checkDuplication(id);  
		if(count == 0) {//사용자 id가 중복되지 않으면 회원가입 성공적으로 진행
			System.out.println("-사용할 수 있는 아이디입니다.");
			System.out.println();
			System.out.println("✔ 비밀번호를 입력해주세요.");
			System.out.print("비밀번호 입력: ");
			String pw = stdIn.nextLine();
			System.out.println();
			System.out.println("✔ 전화번호를 입력해주세요");
			System.out.print("전화번호 입력: ");
			String phone = stdIn.nextLine();
			MembershipDto md = new MembershipDto(id, name, pw, phone);
			dao.makeMember(md);
			System.out.println();
			System.out.println("★회원가입이 완료되었습니다.");
		}else {//사용자 아이디가 중복되면
			while(count!=0) {//계속 사용자 id가 이미 존재할 경우
				System.out.println("-사용할 수 없는 아이디입니다.");
				System.out.println("✔ 아이디를 다시 입력해주세요.");
				System.out.print("아이디 입력: ");
				id = stdIn.nextLine();
				count = dao.checkDuplication(id);
			}//드디어 사용자 id가 다르면 회원가입 성공적으로 진행
			System.out.println("-사용할 수 있는 아이디입니다.");
			System.out.println();
			System.out.println("✔ 비밀번호를 입력해주세요.");
			System.out.print("비밀번호 입력: ");
			String pw = stdIn.nextLine();
			System.out.println();
			System.out.println("✔ 전화번호를 입력해주세요");
			System.out.print("전화번호 입력: ");
			String phone = stdIn.nextLine();
			MembershipDto md = new MembershipDto(id, name, pw,phone);
			dao.makeMember(md);
			System.out.println();
			System.out.println("★회원가입이 완료되었습니다.");	
		}
		//회원가입이 끝나면 로그인으로 이동
		System.out.println();
		System.out.println("--------------------------------------------------------------------");
		System.out.println();
		System.out.println("                           로그인을 새로이 해주세요.                      ");
		System.out.println();
		//로그인 클래스 생성 - 로그인 메서드로 바로 이동
		Login lg = new Login();
		lg.login();
	}
}