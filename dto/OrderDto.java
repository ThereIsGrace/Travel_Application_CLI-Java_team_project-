package dto;

import lombok.Data;
@Data
/**
 * 주문 확인 Dto 클래스
 * 고객이 선택한 상품의 이름과 가격을 설명하기 위해 사용합니다.
 * 
 * @author 정재은
 * @version 1.0.0
 */
public class OrderDto {
	private String userId;       //사용자 아이디(Id)
	private String prodName;     //상품 이름
	private int prodPrice;       //상품 가격
	public OrderDto() {
		
	}
}
