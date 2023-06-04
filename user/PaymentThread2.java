package user;

/**
 * 신용카드 결제 스레드
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 * 
 */
//어떤 클래스에서 사용할 줄 모르므로 여러 클래스들을 입력받는다.
public class PaymentThread2 implements Runnable{
	PaymentServiceBoth ps = null;    //항공권과 호텔 동시 결제
	PaymentServiceBoth2 ps2 = null;  //항공권 단독 결제
	PaymentServiceBoth3 ps3 = null;  //호텔 단독 결제
	public PaymentThread2(PaymentServiceBoth ps) { //항공권과 호텔을 동시 결제하는 클래스에서 만들어진 경우
		this.ps = ps;  //항곤권, 호텔 동시 결제 서비스를 전역변수로 등록
	}
	public PaymentThread2(PaymentServiceBoth2 ps2) {//항공권을 단독 결제하는 클래스에서 만들어진 경우
		this.ps2 = ps2;//항공권 결제 서비스를 전역변수로 등록
	}
	public PaymentThread2(PaymentServiceBoth3 ps3) {//호텔을 단독 결제하는 클래스에서 만들어진 경우
		this.ps3 = ps3;//호텔 결제 서비스를 전역변수로 등록
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(2500);//스레드 2.5초간 중지
			System.out.println("보안 문자가 도착 했습니다. 아래의 숫자를 결제화면에 입력해주세요");
			int number = (int)(10 + (Math.random()*90)); //10부터 99까지의 보안 숫자를 보냄
			System.out.println(number);//보안문자 출력
			if(ps != null) {
				ps.setSecureNumber(number);// 이 스레드를 만든 원래의 클래스에 보안 숫자 설정  
			}else if(ps2 != null) {
				ps2.setSecureNumber(number);// 이 스레드를 만든 원래의 클래스에 보안 숫자 설정 
			}else if(ps3 != null) {
				ps3.setSecureNumber(number);// 이 스레드를 만든 원래의 클래스에 보안 숫자 설정 
			}
		}catch(Exception e) {
			
		}	
	}
}
