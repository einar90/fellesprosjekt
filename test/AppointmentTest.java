import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;

import org.junit.Test;


public class AppointmentTest {

	@Test
	public void appointmentCreateTest()
	{
		Database.reset();
		
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		
		assertNotNull(user);
		assertEquals(0, Appointment.getAll().size());
		
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		assertNotNull(a);
		assertEquals(1, Appointment.getAll().size());
	}
	
	@Test
	public void appointmentParticipateTest()
	{
		Database.reset();

		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Group group = Group.create("AwesomeGroup");
		Group group2 = Group.create("BoringGroup");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		
		assertEquals(true, group.addMember(user));
		assertEquals(true, group2.addMember(user));
		assertEquals(true, a.addParticipant(group));
		assertEquals(true, a.addParticipant(group2));
		
		assertEquals(2, a.getParticipants().size());
		assertEquals(1, group.getAppointments().size());
		assertEquals(1, group2.getAppointments().size());
		
		assertEquals(1, user.getAppointments().size());
	}
}
