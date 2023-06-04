package dto;

import lombok.Data;
@Data
/**
 * 회원가입 Dto 클래스
 * 회원정보를 저장하는 클래스입니다.
 * @author 정재은(gitHub.com/ThereIsGrace)
 * @version 1.0.0
 * @see None
 */
public class MembershipDto {
	/**
	 * @param id : 아이디
	 * @param password : 비밀번호
	 * @param name : 이름
	 * @param phoneNumber : 휴대전화 번호
	 */
	private String id;
	private String password;
	private String name;
	private String phoneNumber;
	public MembershipDto(String id, String password, String name, String phoneNumber) {
		this.id = id;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
}
