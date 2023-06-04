# 🛫여행 애플리케이션 CLI
>오라클 DB를 이용한 여행 애플리케이션 CLI 만들기 

## 💻개발기간 
>2023.05.21~2023.06.04

## ⛰️목표
>1. JAVA의 기본적인 문법들과 기능들을 복습하고 생성된 객체들의 조화로운 연동 방식을 생각해본다. 
>2. 오라클 데이터베이스와 자바를 연동하여 <bold>데이터베이스의 CRUD 기능<bold>을 애플리케이션 내에서 구현한다.  
>3. 하나의 미니-애플리케이션을 계획부터 설계까지 직접 구상하고 실현한다.  

## 🏖️주제
>여행객들이 항공권과 호텔을 예약할 수 있는 CLI 프로그램을 구현한다.<br>
>주요 기능<br>
>1. 회원가입<br>
>2. 로그인<br>
>     * ID, PW 찾기<br>
>3. 항공권 구매<br>
>    * 항공권 조회<br>
>    * 항공권 결제 및 예약<br>
>4. 호텔 객실 구매<br>
>    * 호텔/리뷰 조회<br>
>    * 호텔 결제 및 예약<br>
>5. 여행 후 리뷰 작성<br>
>    * 호텔 리뷰 작성<br>
>    * 내가 쓴 리뷰 모아보기<br>
>6. 종료

## 요구사항명세
![요구](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/55e6404b-682b-431e-8370-2186176d6944)

## ERD
![ERD](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/9824ce78-3aba-4fc7-9bef-b782ffaf4211)

## 주요 기능 시연
  ### 항공권 선택
  * 항공권을 선택하면 날짜와 좌석수를 선택합니다.
  * 복수의 항공권을 장바구니에 담아놓았다가 호텔과 함께 결제하거나 단독으로 결제할 수 있습니다.
![ezgif com-video-to-gif](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/4efab321-0964-498f-af26-331b819e8e75)<br>
  ### 항공권 결제
  * 항공권을 결제하면 자동으로 주문 정보가 생성되고 예약 내역으로 이동합니다. 
  * 사용자는 항공권과 호텔의 예약내역을 확인할 수 있습니다.
![ezgif com-video-to-gif (4)](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/7c4fc56d-03e9-4bd5-b1a6-32ea41df2eed)<br>
  
  ### 호텔 선택
  * 호텔을 선택하면 날짜와 숙박일수를 선택합니다.
  * 호텔 리뷰를 확인할 수 있습니다.<br>
 ![ezgif com-video-to-gif (3)](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/1ea5c912-077a-4397-b0f1-c1402dceea5e)<br>
  ### 호텔 결제 
  * 장바구니에서 결제하고 싶은 호텔만 선택하고 나머지는 자동으로 삭제합니다. 
  * 결제 방법에는 무통장 입금과, 신용카드 결제가 있으며 둘 다 스레드를 사용해서 구현했습니다.<br>
 ![ezgif com-video-to-gif (1)](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/dedb3696-92cf-493c-a3d2-93093529541a)
  ### 호텔 리뷰 작성
  * 여행을 갔다온 뒤 호텔의 평점, 리뷰를 작성할 수 있습니다. 
  * 내가 쓴 리뷰들만 모아보는 기능이 있습니다.<br>
 ![ezgif com-video-to-gif (2)](https://github.com/ThereIsGrace/Travel_Application_CLI-Java_team_project-/assets/109272327/7494982b-d694-4780-a979-68e1f71721de)
