package user;

import dao.ExitDao;
import dto.LoginDto;
/**
 * 로그아웃을 진행하는 클래스입니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 */
public class Exit {
	ExitDao eda = new ExitDao();   //ExitDao는 LOGIN테이블에서 CRUD를 진행하는 DAO.
	LoginDto ld = new LoginDto();  //LoginDto 객체(Id, Password를 담을 예정)
	
	//로그인한 사람이 종료할 때(로그인 정보 삭제도 필요)
	public Exit(LoginDto ld) {    
		setLd(ld);                 //로그인 객체 복사
		eda.logOut(ld);            //로그인 객체에서 Id를 찾아 LOGIN테이블에서 삭제
		System.out.println(ld.getId()+"님 로그아웃하였습니다.");
		System.out.println("프로그램을 종료합니다.");
		System.exit(1);            //프로그램 종료
	}
	//로그인하지 않은 사람이 종료할 때 
	public Exit() {
		System.out.println("프로그램을 종료합니다.");
		System.exit(1);            //프로그램 종료
	}
	//로그인 객체 복사
	public void setLd(LoginDto ld) {  //다른 클래스(이미 로그인 객체가 있는 클래스)에서 로그인 객체를 받아옴
		this.ld.setId(ld.getId()); //로그인 객체 Id 설정
		this.ld.setPassword(ld.getPassword()); //로그인 객체 Pw 설정
	}
}