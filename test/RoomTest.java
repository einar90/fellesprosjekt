import static org.junit.Assert.*;

import java.sql.Timestamp;

import junit.framework.TestCase;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Appointment;
import no.ntnu.gruppe47.db.entities.Room;
import no.ntnu.gruppe47.db.entities.User;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import sun.security.krb5.internal.APOptions;

public class RoomTest {
	
	@Before
	public void setUp(){
		Database.reset();
	}
	
	@Test
	public void testRoomConstructor(){
		Room rom1 = Room.create("B-42323%",63);
		
		assertEquals("B-42323%", rom1.getRoomNumber());
		assertEquals(63, rom1.getCapacity());
	}
	
	@Test
	public void testCreateName(){
		assertEquals(0, Room.getAll().size());
		
		Room.create("RL-534");
		assertEquals(1, Room.getAll().size());
		
		Room.create((String) null);
		assertEquals(1, Room.getAll().size());
		
		String navn = "T-42";
		int kapasitet = 64;
		Room rom1 = Room.create(navn,kapasitet);
		int romID = rom1.getRoomId();
		
		assertEquals(2, Room.getAll().size());
		
		assertEquals(romID, rom1.getRoomId());
		assertEquals(navn, rom1.getRoomNumber());
		assertEquals(kapasitet, rom1.getCapacity());
		
		assertEquals(romID, Room.getByID(romID).getRoomId());
		assertEquals(romID, Room.getByName(navn).getRoomId());
		
		assertEquals(kapasitet, Room.getByID(romID).getCapacity());
		assertEquals(kapasitet, Room.getByName(navn).getCapacity());
		
		assertEquals(navn, Room.getByID(romID).getRoomNumber());
		assertEquals(navn, Room.getByName(navn).getRoomNumber());
	}
	
	@Test
	public void testCreateNameAndCapasity(){
		assertEquals(0, Room.getAll().size());
		
		int rom_id = Room.create("R1",400).getRoomId();
		assertEquals(1, Room.getAll().size());
		
		assertEquals("R1", Room.getByName("R1").getRoomNumber());
		assertEquals("R1", Room.getByID(rom_id).getRoomNumber());
		
		assertEquals(rom_id, Room.getByID(rom_id).getRoomId());
		assertEquals(rom_id, Room.getByName("R1").getRoomId());
		
		assertEquals(400, Room.getByID(rom_id).getCapacity());
		assertEquals(400, Room.getByName("R1").getCapacity());
	}
	
	@Test
	public void testAvailability(){
		Room r1 = Room.create("Rom 1", 200);
		Room r2 = Room.create("Rom 2", 200);
		Room r3 = Room.create("Rom 3", 200);
		
		DateTime date = new DateTime();
		Timestamp start = new Timestamp(date.getMillis());
		Timestamp end = new Timestamp(date.getMillis() + 3600*100);
		
		User user = User.create("User1", "pass", "Allah", "lhjlkh");
		Appointment a = Appointment.create(user, start, end, "Avtale", "planlagt");
		Appointment b = Appointment.create(user, start, end, "Avtale", "planlagt");
		Appointment c = Appointment.create(user, start, end, "Avtale", "planlagt");
		Appointment d = Appointment.create(user, start, end, "Avtale", "planlagt");
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		assertEquals(null, d);

		DateTime newDate = new DateTime().plusDays(1);
		Timestamp newStart = new Timestamp(newDate.getMillis());
		Timestamp newEnd = new Timestamp(newDate.getMillis() + 3600*100);

		Appointment a2 = Appointment.create(user, newStart, newEnd, "Avtale", "planlagt");
		Appointment b2 = Appointment.create(user, newStart, newEnd, "Avtale", "planlagt");
		Appointment c2 = Appointment.create(user, newStart, newEnd, "Avtale", "planlagt");
		Appointment d2 = Appointment.create(user, newStart, newEnd, "Avtale", "planlagt");
		
		assertNotNull(a2);
		assertNotNull(b2);
		assertNotNull(c2);
		assertEquals(null, d2);
	}
	
}