package no.ntnu.gruppe47;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;

import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.User;

public class Print {

	public static void printToday(User user){
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		DateFormat dateFormat = null;
		if (cal.get(Calendar.DATE) % 10 == 1)
			dateFormat = new SimpleDateFormat("dd'st of' MMMM yyyy");
		else if (cal.get(Calendar.DATE) % 10 == 2)
			dateFormat = new SimpleDateFormat("dd'nd of' MMMM yyyy");
		else if (cal.get(Calendar.DATE) % 10 == 3)
			dateFormat = new SimpleDateFormat("dd'rd of' MMMM yyyy");
		else 
			dateFormat = new SimpleDateFormat("dd'th of' MMMM yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("Today is the " + dateFormat.format(today));
		System.out.println("It's " + timeFormat.format(today) + " O'Clock");
		System.out.println();
		
		
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		Timestamp start = new Timestamp(cal.getTimeInMillis());
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
		Timestamp end = new Timestamp(cal.getTimeInMillis());
		
		ArrayList<Appointment> appointments = Appointment.getAllBetweenFor(user, start, end);
		for (Appointment a : appointments)
			System.out.println(a);
		
	}
	
	public static void printThisWeek(User user)
	{
		printWeekNum(user, -1);
	}
	
	public static int printWeekNum(User user, int week) {
		DateTime date = new DateTime();
		if (week == -1)
			week = date.getWeekOfWeekyear();

		date = date.withWeekOfWeekyear(week).withDayOfWeek(1).withTimeAtStartOfDay();
		Timestamp start = new Timestamp(date.getMillis());
		date = date.withDayOfWeek(7).plusDays(1).withTimeAtStartOfDay();
		Timestamp end = new Timestamp(date.getMillis());

		ArrayList<Appointment> appointments = Appointment.getAllBetweenFor(user, start, end);
		if (appointments.size() > 0)
			for (Appointment a : appointments)
				System.out.println(a);
		else
			System.out.println("No appointments in week " + week);
		
		return week;
	}

	public static void printThisMonth(User user) {
		printMonthNum(user, -1);
	}
	
	public static int printMonthNum(User user, int month){
		DateTime date = new DateTime();
		if (month < 0)
			month = date.getMonthOfYear();
		
		date = date.withMonthOfYear(month).withDayOfMonth(1).withTimeAtStartOfDay();
		Timestamp start = new Timestamp(date.getMillis());
		int lastDayOfMonth = getLastDayOfMonth(month, date.getYear());
		date = date.withMonthOfYear(month).withDayOfMonth(lastDayOfMonth).withTime(23, 59, 59, 999);
		Timestamp end = new Timestamp(date.getMillis());
		
		ArrayList<Appointment> appointments = Appointment.getAllBetweenFor(user, start, end);
		if (appointments.size() > 0){
			for (Appointment a: appointments)
				System.out.println(a);
		}else
			System.out.println("No appointments for month no. " + month);
		
		return month; 
	}

	public static void printAll(User user) {
		System.out.println("Here is a list of every appointment you have:");
		ArrayList<Appointment> appointments = Appointment.getAllFor(user);
		if (appointments.size() == 0) System.out.println("You have no appointments what so ever.");
		else{
			for (Appointment a : appointments)
				System.out.println(a);
		}
	}
	
	private static int getLastDayOfMonth(int month, int year){
		switch (month){
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 2:
			if (year % 400 == 0 || year % 4 ==0)
				return 29;
			else return 28;
		default:
			return 30;
		}
	}
}
