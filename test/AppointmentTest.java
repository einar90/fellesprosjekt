import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.Room;
import no.ntnu.gruppe47.db.entities.User;

import org.junit.Test;


public class AppointmentTest {

	@Test
	public void appointmentCreateTest()
	{
		Database.reset();

		Room room = Room.create("Rom 1", 200);
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

		Room room = Room.create("Rom 1", 200);
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Group group = Group.create("AwesomeGroup");
		Group group2 = Group.create("BoringGroup");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		
		assertEquals(true, group.addMember(user));
		assertEquals(true, group2.addMember(user));
		assertEquals(true, a.addParticipant(group));
		assertEquals(true, a.addParticipant(group2));
		
		assertEquals(2, a.getGroupParticipants().size());
		assertEquals(1, group.getAppointments().size());
		assertEquals(1, group2.getAppointments().size());
		
		assertEquals(1, user.getAppointments().size());
	}
	
	@Test
	public void roomChangeTest()
	{
		Database.reset();

		Room room = Room.create("Rom 1", 2);
		Room room2 = Room.create("Rom 2", 5);
		Room room3 = Room.create("Rom 3", 50);
		
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		
		for (int i = 1; i < 10; i++)
		{
			User u = User.create("User" + i, "pass", "kljh", "kljhlkj");
			a.addParticipant(u.getPrivateGroup());
			if (i <= 2)
				assertEquals(1, a.getRoomId());
			else if (i <= 5)
				assertEquals(2, a.getRoomId());
			else
				assertEquals(3, a.getRoomId());
		}
	}
}
