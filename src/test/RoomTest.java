package test;

import static org.junit.Assert.assertEquals;
import junit.framework.TestCase;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Room;

import org.junit.Test;

public class RoomTest extends TestCase {
	
	public void setUp(){
		Database.reset();
	}
	
	@Test
	public void testAddRoom(){
		assertEquals(0, Room.getAll().size());
		
		Room.create("RL-534");
		assertEquals(1, Room.getAll().size());
		
		Room.create((String) null);
		assertEquals(1, Room.getAll().size());
		
		String navn = "T-42";
		int kapasitet = 64;
		int romID = Room.create(navn,kapasitet).getRoomId();
		Room rom1 = new Room(romID, navn, kapasitet);
		
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
}