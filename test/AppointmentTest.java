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
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "planlagt");
		
		assertEquals(true, a.addParticipant(user));
		
		assertEquals(1, a.getParticipants().size());
		
		assertEquals(1, user.getAppointments().size());
	}
	
	@Test
	public void appointmentInvitationTest()
	{
		Database.reset();

		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		User user1 = User.create("TestUser1", "Pass", "Håkon Bråten", "mail@mail.com");
		User user2 = User.create("TestUser2", "Pass", "Håkon Bråten", "mail@mail.com");
		User user3 = User.create("TestUser3", "Pass", "Håkon Bråten", "mail@mail.com");
		User user4 = User.create("TestUser4", "Pass", "Håkon Bråten", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte", "Outer Space");
		
		a.inviteUser(user);
		a.inviteUser(user1);
		a.inviteUser(user2);
		a.inviteUser(user3);
		a.inviteUser(user4);
		
		assertEquals(a.getInvitesWithResponse(0).size(), 5);
		
		user.getInvitations().get(0).accept();
		user1.getInvitations().get(0).accept();

		user2.getInvitations().get(0).reject();
		user3.getInvitations().get(0).reject();

		assertEquals(a.getInvitesWithResponse(1).size(), 2);
		assertEquals(a.getInvitesWithResponse(0).size(), 1);
		assertEquals(a.getInvitesWithResponse(-1).size(), 2);

		assertEquals(a.getParticipants().size(), 2);
	}
	
	@Test
	public void roomChangeTest()
	{
		Database.reset();

		Room room = Room.create("Rom 1", 2);
		Room room2 = Room.create("Rom 2", 5);
		Room room3 = Room.create("Rom 3", 50);
		
		User user = User.create("TestUser", "Pass", "Håkon Bråten", "mail@mail.com");
		Appointment a = Appointment.create(user, new Timestamp(1363430605), new Timestamp(1363436605), "Møte");
		
		for (int i = 1; i < 10; i++)
		{
			User u = User.create("User" + i, "pass", "kljh", "kljhlkj");
			a.addParticipant(u);
			if (i <= 2)
				assertEquals(1, a.getRoomId());
			else if (i <= 5)
				assertEquals(2, a.getRoomId());
			else
				assertEquals(3, a.getRoomId());
		}
	}
}
