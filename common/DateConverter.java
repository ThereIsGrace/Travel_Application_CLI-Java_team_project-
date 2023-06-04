package common;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class DateConverter {
	//오늘의 날짜를 출력
	public static String today() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일",Locale.KOREAN);
		return sdf.format(new java.util.Date());
	}
	
	//예약 날짜를 출력
	public static Date reservationDay(String ss) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date dd = new java.util.Date();
		try {
			dd = sdf.parse(ss);
		}catch(ParseException e) {
			
		}
		return new Date(dd.getTime());
	}

	public static Date reservationDay2(String request) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy월 mm일 E");
		java.util.Date date = new java.util.Date();
		try {
			date = sdf.parse(request);
		}catch(ParseException e) {
			
		}
		return new Date(date.getTime());
	}

	public static String untilDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 2);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(cal.getTime());
	}

	public static String fromCaltoS(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		return sdf.format(cal.getTime());
	}
	
	public static boolean checkDate(String inputDate) {
		boolean result = false;
		Calendar cal = new GregorianCalendar();
		String[] date = new String[62];
		for(int i = 0; i < 62; i++) {
			cal.add(Calendar.DAY_OF_WEEK,1);
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
			java.util.Date dd = new java.util.Date();
			dd = cal.getTime();
			String ss = sdf.format(dd);
			date[i] = ss;
		}
		for(int i = 0; i < 62; i++) {
			if(inputDate.equals(date[i])) 
				result = true;
		}
		return result;
	}
}