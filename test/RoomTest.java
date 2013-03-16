

import static org.junit.Assert.assertEquals;
import junit.framework.TestCase;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Room;

import org.junit.Test;

public class RoomTest extends TestCase {
	
	@Override
	public void setUp(){
		Database.reset();
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
		
	}
	
	@Override
	public void tearDown(){
		Database.closeConnection();
	}
	
}