package dto;

import lombok.Data;

/**
 * 로그인 Dto 클래스
 * 로그인 정보를 저장하는 클래스입니다. 
 * 아이디(Id)로 예약 내역(항공,숙박), 결제 정보를 확인합니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see None
 */
@Data
public class LoginDto {
	private String id;              //사용자 Id
	private String password;        //사용자 pw
	public LoginDto() {
		
	}
	public LoginDto(String id, String password) {
		this.id = id;
		this.password = password;
	}
}
