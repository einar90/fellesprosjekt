import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;

import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Alert;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Room;
import no.ntnu.gruppe47.db.entities.User;

import org.junit.Before;
import org.junit.Test;


public class AppointmentTest {

	@Before
	public void setUp()
	{
		Database.reset();
	}
	
	@Test
	public void appointmentCreateTest()
	{

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

		Room room = Room.create("Rom 1", 200);
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		
		assertEquals(1, a.getParticipants().size());
		
		assertEquals(1, user.getAppointments().size());
	}
	
	@Test
	public void appointmentInvitationTest()
	{

		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		User user1 = User.create("TestUser1", "Pass", "Håkon Bråten1", "mail@mail.com");
		User user2 = User.create("TestUser2", "Pass", "Håkon Bråten2", "mail@mail.com");
		User user3 = User.create("TestUser3", "Pass", "Håkon Bråten3", "mail@mail.com");
		User user4 = User.create("TestUser4", "Pass", "Håkon Bråten4", "mail@mail.com");
		User user5 = User.create("TestUser5", "Pass", "Håkon Bråten5", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "Outer Space");

		a.inviteUser(user1);
		a.inviteUser(user2);
		a.inviteUser(user3);
		a.inviteUser(user4);
		a.inviteUser(user5);
		
		assertEquals(a.getInvitesWithResponse(0).size(), 5);
		
		user5.getInvitations().get(0).accept();
		user1.getInvitations().get(0).accept();

		user2.getInvitations().get(0).reject();
		user3.getInvitations().get(0).reject();

		assertEquals(a.getInvitesWithResponse(1).size(), 2);
		assertEquals(a.getInvitesWithResponse(0).size(), 1);
		assertEquals(a.getInvitesWithResponse(-1).size(), 2);

		assertEquals(a.getParticipants().size(), 3);
		
		assertEquals(2, user.getAlerts().size());
		
		user.deleteAppointment(a);
		
		assertEquals(1, user1.getAlerts().size());
		assertEquals(0, user1.getInvitations().size());
	}
	
	@Test
	public void roomChangeTest()
	{

		Room room = Room.create("Rom 1", 2);
		Room room2 = Room.create("Rom 2", 5);
		Room room3 = Room.create("Rom 3", 50);
		
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte");
		
		for (int i = 1; i < 10; i++)
		{
			User u = User.create("User" + i, "pass", "kljh", "kljhlkj");
			a.addParticipant(u);
			int participants = a.getParticipants().size();
			if (participants <= 2)
				assertEquals("Rom 1", Room.getByID(a.getRoomId()).getRoomNumber());
			else if (participants <= 5)
				assertEquals("Rom 2", Room.getByID(a.getRoomId()).getRoomNumber());
			else
				assertEquals("Rom 3", Room.getByID(a.getRoomId()).getRoomNumber());
		}
	}

	@Test
	public void appointmentGetBetweenForTest()
	{
		
		Room r1 = Room.create("Room 1",5);
		User user1 = User.create("Person1", "p", "Person Personsen", "post@inet.no");
		Timestamp start = new Timestamp(3600 * 1000);
		Timestamp end = new Timestamp(3650 * 1000);
		String description = "First test appointment";
		
		Appointment app = Appointment.create(user1, start, end, description,"");
		
		assertEquals(1, Appointment.getAllBetween(start, end).size());
		assertEquals(app, Appointment.getAllBetween(start, end).get(0));
		
		Appointment app2 = Appointment.create(user1, start, end, description);
		
		assertEquals(2, Appointment.getAllBetween(start, end).size());
	}
}
