import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import no.ntnu.gruppe47.Print;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.Room;
import no.ntnu.gruppe47.db.entities.User;

import org.joda.time.DateTime;
import org.junit.Test;


public class PrintTest {

	@Test
	public void printTest()
	{
		Database.reset();

		Room r1 = Room.create("Rom 1", 200);
		Room r2 = Room.create("Rom 1", 200);
		Room r3 = Room.create("Rom 1", 200);
		
		User user = User.create("Username", "Password", "Name", "Email");
		
		DateTime date = new DateTime();
		Timestamp start = new Timestamp(date.getMillis());
		Timestamp end = new Timestamp(date.getMillis() + 3600*1000);

		Appointment a = Appointment.create(user, start, end, "avtale", "planlagt");
		a.addParticipant(user);
		Appointment a2 = Appointment.create(user, start, end, "avtale", "planlagt");
		a2.addParticipant(user);
		Appointment a3 = Appointment.create(user, start, end, "avtale", "planlagt");
		a3.addParticipant(user);

		DateTime searchDate = new DateTime();
		int week = searchDate.getWeekOfWeekyear();

		date = searchDate.withWeekOfWeekyear(week).withDayOfWeek(1).withTimeAtStartOfDay();
		Timestamp startWeek = new Timestamp(searchDate.getMillis());
		date = searchDate.withDayOfWeek(7).plusDays(1).withTimeAtStartOfDay();
		Timestamp endWeek = new Timestamp(searchDate.getMillis());
		
		assertEquals(3, Appointment.getAllBetweenFor(user, startWeek, endWeek).size());
		
		Print.printWeekNum(user, -1);
	}
}
