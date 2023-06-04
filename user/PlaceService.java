package user;

import java.util.List;
import java.util.Scanner;

import dao.PlaceDao;
import dto.LoginDto;
import dto.PlaceDto;
/**
 * 지역 선택 클래스
 * 여행을 갈 여행지를 선택합니다.
 * 
 * @author 정재은(gitHub.com/ThereIsGrace)
 *
 */
public class PlaceService {
	PlaceDao pd = new PlaceDao();              //여행지 테이블(PLACE)을 관리하는 DAO
	LoginDto ld = new LoginDto();              //로그인한 상태를 저장(Id, Pw)
	Scanner stdIn = new Scanner(System.in);
	public PlaceService(LoginDto ld) {        //로그인 객체를 입력받음
		this.ld.setId(ld.getId());            //전역 변수 ld에 Id 설정
		this.ld.setPassword(ld.getPassword());//전역 변수 ld에 Pw 설정
		init();
	}
	
	private void init() {
		System.out.println();
		System.out.println();
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println();
		System.out.println("                                ** 여행지 선택 **                             ");
		System.out.println("                          여행하고 싶은 국가를 선택해주세요               ");
		System.out.println();
		List<PlaceDto> placeList = pd.showPlace();//DAO에서 여행지리스트를 받아옴
		int i = 0;
		System.out.println(String.format("%s\t%-20s\t%-30s\t%-20s", "번호","국가명","도시명","비행시간"));
		System.out.println("------------------------------------------------------------------------------------");
		//여행지의 번호(i), 국가명, 도시명, 총 비행시간을 출력
		for(i = 0; i < placeList.size(); i++) {
			PlaceDto pd =  placeList.get(i);
			String countryName = pd.getCountryName();//국가명
			String placeName = pd.getPlaceName();//도시명
			String travelTime = pd.getTravelTime();//총 비행시간
			System.out.print(String.format("%d\t%-20s\t%-30s\t%-20s",i, countryName, placeName, travelTime));
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.print("＆번호 입력: ");
		//번호를 입력 받음 번호로 DAO가 가져온 리스트에서 여행지를 찾아서 PlaceDto 객체에 저장
		int place = stdIn.nextInt();
		PlaceDto pd2 = placeList.get(place);
		FlightService fs = new FlightService(ld); // 다음 화면인 항공권 조회/예약 클래스로 이동
		fs.selectDate(pd2.getPlaceName()); //선택된 국가명으로 향하는 항공편만 볼 수 있도록 함.
	}
}
