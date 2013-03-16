package no.ntnu.gruppe47;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.User;

public class Print {

	private static Database db = new Database();
	
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
		for (int i = 0; i < appointments.size(); i++){
			printAppointment(appointments.get(i));
		}
		
		
		
	}

	public static void printThisWeek(User user) {
		// TODO Auto-generated method stub
		
	}

	public static void printThisMonth(User user) {
		// TODO Auto-generated method stub
		
	}

	public static void prntAll(User user) {
		System.out.println("Here is a list of every appointment you have:");
		ArrayList<Appointment> appointments = Appointment.getAllFor(user);
		for (int i = 0; i < appointments.size(); i++){
			printAppointment(appointments.get(i));
		}
		
	}
	
	private static void printAppointment(Appointment appointment){
		int appointmentId = appointment.getAppointmentId();
		Date startTime = appointment.getStartTime();
		Date endTime = appointment.getEndTime();
		String createdBy = appointment.getNameOfCreator();
		String out ="ID: " + appointmentId + "\t" + "From " + startTime + " to " + 
				endTime + ", created by " + createdBy + 
				"\nHaving this description:\n" + appointment.getDescription();
		System.out.println(out);
	}
}
