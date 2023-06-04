package user;

import java.util.Scanner;

import art.AsciiArt;
import dao.LoginDao;
import dao.MembershipDao;
import dto.LoginDto;

public class Login {
	Scanner stdIn = new Scanner(System.in);
	LoginDao dao = new LoginDao();            //로그인 테이블(LOGIN)을 관리하는 DAO - id, pw 일치 여부 확인
	MembershipDao mdao = new MembershipDao(); //회원 테이블(MEMBER)을 관리하는 DAO - member 테이블에서 사용자 이름 등 다른 정보 입력하면
	// 사용자 id, pw 찾아줌
	/*
	 * 사용자로부터 id, pw 입력받아 member테이블과 비교. 일치하는 경우 다음 메뉴로 들어감
	 * 반대로 로그인 실패 횟수가 3회가 되는 경우 자동으로 Id, Pw 찾기에 들어감
	 */
	public void login() {
		int count = 1;    //로그인 실패 횟수
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("----------------------------------------------------------------------");
		System.out.println();
		System.out.println();
		System.out.println("                                ** 로그인 **                          ");
		System.out.print("＆아이디 입력: ");
		String id = stdIn.nextLine();
		System.out.println();
		System.out.print("＆비밀번호 입력: ");
		String pw = stdIn.nextLine();
		int result = dao.loginCheck(id, pw); //사용자의 Id,Pw가 MEMBER테이블의 id, pw와 일치하는지 확인 일치하면 0,
		//id는 있는데 일치하지 않으면 1, 아예 id가 존재하지 않으면 2를 반환하는 함수를 DB에 심어두었다. 
		
		if(result == 0) {//사용자 정보가 MEMBER 테이블과 일치
			dao.login(id, pw); //로그인 테이블에 사용자 id, pw를 저장
			System.out.println();
			AsciiArt.loginSuccess(); //로그인에 성공했을 때 나오는 화면
			System.out.println("반갑습니다. " +  id + "님");
			System.out.println();
			System.out.println();
			System.out.println(" 메뉴 1. 항공권 조회/예약 2. 호텔 조회/예약 3. 종료하기");
			System.out.print("＆메뉴 입력: ");
			int num = stdIn.nextInt();
			//로그인을 성공하면 항공권이나 호텔을 선택해야 하므로 입력받은 Id와 Pw를 토대로 사용자 정보를 담은 로그인 객체를 만들어서
			//다음 클래스들에게 넘긴다.
			LoginDto ld = new LoginDto(); // 로그인 객체 만들기
			ld.setId(id);       //아이디 설정
			ld.setPassword(pw); //비밀번호 설정
			switch(num) {
			//사용자 로그인 정보를 성공적으로 넘겨줌
			case 1: PlaceService ps = new PlaceService(ld);  break;
			case 2: ResortService rs1 = new ResortService(ld); break;
			case 3: Exit exit = new Exit(ld); 
			}
		}else {//Id가 Pw와 일치하지 않거나 Id가 아예 존재하지 않는 경우
			while(true) {//3번까지 Id와 Pw를 계속 입력받게 함
				if(result == 0) {//3번 안에 성공했을 때 로그인 객체 만들어 다음 메뉴로 진행
					System.out.println();
					AsciiArt.loginSuccess();
					System.out.println("     반갑습니다. " +  id + "님");
					System.out.println();
					System.out.println();
					System.out.println(" 메뉴 1. 항공권 조회/예약 2. 호텔 조회/예약 3. 종료하기");
					System.out.print("＆메뉴 입력: ");
					int num = stdIn.nextInt();
					LoginDto ld = new LoginDto();
					ld.setId(id);
					ld.setPassword(pw);
					switch(num) {
					case 1: PlaceService ps = new PlaceService(ld);  break;
					case 2: ResortService rs1 = new ResortService(ld); break;
					case 3: Exit exit = new Exit(ld); 
					}
					break;
				}else if(result == 1) {//pw가 틀렸다고 알려줌, 비밀번호만 다시 입력받아서 login DAO의 loginCheck함수로 Member테이블과 비교.
					System.out.println();
					System.out.println("로그인 실패 횟수: " + count + "회");
					System.out.println("※비밀번호가 틀렸습니다. 비밀번호를 다시 입력해 주세요.");
					System.out.println();
					System.out.print("＆비밀번호 입력: ");
					pw = stdIn.next();
					result = dao.loginCheck(id, pw);
				}else if(result == 2){//아예 데이터 자체가 존재하지 않을 때 - 아이디도 다시 입력받아야 한다. 
					System.out.println();
					System.out.println("로그인 실패 횟수: " +  count + "회");
					System.out.println("※사용자 정보가 존재하지 않습니다. 아이디를 확인하시거나 회원가입을 해주세요.");
					System.out.println();
					System.out.println();
					//사용자에게 아이디를 다시 입력할 것인지 회원가입을 할 건지 물음
					System.out.println("메뉴 1. 아이디 다시 입력 2. 회원가입");
					System.out.print("＆메뉴 입력: ");
					int num = stdIn.nextInt();
					System.out.println("------------------------------------------------------------");
						
					if(num == 1) {//Id 다시 입력 선택 - 아이디를 다시 입력받는다. break를 하지 않으므로 역시 3번 카운트 안에 포함됨 
						System.out.println();
						System.out.print("＆아이디 입력: ");
						id = stdIn.next();
							
						System.out.println();
						System.out.print("＆비밀번호 입력: ");
						pw = stdIn.next();
						result = dao.loginCheck(id, pw); //로그인 결과 확인
						System.out.println(result);
					}else {//회원가입 - 아예 로그인 페이지를 나온다. 회원가입 화면으로 감
						Membership ms = new Membership();
						ms.membership();
						break;
					}
				}
				count++;//사용자가 틀릴 때마다 1씩 증가
				if(count==3) {//3번 틀렸을 때
					//로그인 실패 횟수를 알려주고
					System.out.println("로그인 실패 횟수: " +  count + "회");
					System.out.println();
					//Id나 Pw를 찾게 한다. 
					System.out.println("메뉴 1. 아이디 찾기 2. 비밀번호 찾기");
					System.out.print("＆메뉴 입력:");
					int num = stdIn.nextInt();
					if(num == 1) {//Id 찾기 선택하는 경우
						findId();
						break;
					}else {  //Pw 찾기 선택하는 경우
						findPw();
						break;
					}
				}
			}
		}
	}
	//Id 찾는 메서드 - 이름과 휴대전화 번호로 찾음
	public void findId() {
		System.out.println("                        ** 아이디 찾기 **                     ");
		System.out.print("＆이름 입력: ");
		String name = stdIn.next();
		System.out.print("&휴대전화 번호 입력: ");
		String phone = stdIn.next();
		//mdao는 전역변수로 설정된 회원 테이블(MEMBER)을 관리하는 DAO
		String userId = mdao.findId(name, phone); //사용자 이름과 폰번호로 사용자 id를 찾음 
		System.out.println(userId); //사용자 id를 출력
		if(!userId.trim().equals("")) {//사용자 id가 존재한다면
			System.out.println("사용자님의  Id는 " + userId + "입니다.");
			System.out.println("다시 로그인 해 주세요.");
			//로그인 페이지로 이동
			Login lg = new Login();
			lg.login();
		}else {//존재하지 않는다면 바로 회원가입으로 진행
			System.out.println("아이디가 존재하지 않습니다. 새로이 회원가입해 주세요.");
			System.out.println();
			Membership ms = new Membership();
			//회원가입 페이지로 이동
			ms.membership();
		}
	}
	
	//Pw를 찾는 메서드 - 아이디와, 이름, 휴대폰 번호로 찾음
	public void findPw() {
		System.out.println("                        ** 비밀번호 찾기 **                         ");
		System.out.print("＆아이디를 입력: ");
		String id = stdIn.next();
		System.out.print("&이름 입력: ");
		String name = stdIn.next();
		System.out.print("&휴대전화 번호 입력: ");
		String phone = stdIn.next();
		//mdao는 전역변수로 설정된 회원 테이블(MEMBER)을 관리하는 DAO
		String userPw = mdao.findPw(id, name, phone); //사용자 Id, 이름과 휴대전화 번호로 사용자 id를 찾음
		if(!userPw.trim().equals("")) {//비밀번호가 존재하면 로그인 페이지로 이동
			System.out.println("사용자님의 Pw는 " + userPw + "입니다.");
			System.out.println("다시 로그인 해 주세요.");
			//로그인 페이지로 이동
			Login lg = new Login();
			lg.login();
		}else {//존재하지 않는다면 회원가입으로 진행
			System.out.println("패스워드를 찾을 수 없습니다. 새로이 회원가입해 주세요.");
			//회원가입 페이지로 이동
			Membership ms = new Membership();
			ms.membership();
		}
	}
}