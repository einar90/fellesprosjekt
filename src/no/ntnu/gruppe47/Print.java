package no.ntnu.gruppe47;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

	public static void printThisWeek(User user) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	public static void printAll(User user) {
		System.out.println("Here is a list of every appointment you have:");
		ArrayList<Appointment> appointments = Appointment.getAllFor(user);
		for (Appointment a : appointments)
			System.out.println(a);
		
	}
}
