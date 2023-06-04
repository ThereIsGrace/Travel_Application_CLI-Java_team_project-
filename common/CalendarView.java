package common;

import java.util.Calendar;

public class CalendarView {
	public static void makeCalendar() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		Calendar cal2 = Calendar.getInstance();
		cal2.add(Calendar.DAY_OF_YEAR, 62);
		int month2 = cal2.get(Calendar.MONTH);
		int day2 = cal2.get(Calendar.DATE);
		for(int m = month; m <= month2; m++) {
			System.out.printf("\n------------%d년 %d월 항공권 예약-----------\n", year, m+1);
			cal.set(year, m, 1);
			System.out.println("    일    월    화    수    목    금    토");
			int wd = cal.get(Calendar.DAY_OF_WEEK);
			for(int i = 1; i < wd; i++) {
				System.out.print("     ");
			}
			for(int i = 1; i <= dayOfMonth(year,m); i++) {
				if(i < day && m == month) {
					System.out.printf("%5s","X");
				}else if(i >= day && m == month) {
					System.out.printf("%5d",i);
				}else if(i < day2 && m == month2) {
					System.out.printf("%5d",i);	
				}else if(i >= day2 && m == month2) {
					System.out.printf("%5s","X");
				}else {
					System.out.printf("%5d",i);
				}
				
				if(wd++%7 == 0) 
					System.out.println();
			}
			System.out.println("\n---------------------------------------");
		}
	}
	public static boolean isLeap(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}
	
	public static int dayOfMonth(int year,int month) {
		int[] daysOfMonth = {31,28,31,30,31,30,31,31,30,31,30,31};
		if(month!=1) {
			return daysOfMonth[month];
		}else {
			return daysOfMonth[1] + (isLeap(year)?1:0);
		}
	}
}
