package test;

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
	public void testRoomConstructor(){
		Room rom1 = new Room(0,"B-42323%#Vtefd",63);
		
		assertEquals(0, rom1.getRoomId());
		assertEquals("B-42323%#Vtefd", rom1.getRoomNumber());
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
	
}