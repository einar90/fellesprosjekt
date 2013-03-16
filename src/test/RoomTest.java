package test;

import static org.junit.Assert.assertEquals;
import junit.framework.TestCase;
import no.ntnu.gruppe47.db.Database;;
import no.ntnu.gruppe47.db.entities.Room;

import org.junit.Test;

public class RoomTest extends TestCase {

	Database sql;
	
	public RoomTest(){
		Database.reset();
		sql = new Database();
	}
	
	@Test
	public void testAddRoom(){
		assertEquals(0, sql.room.getAllRooms().size());
		
		sql.room.addRoom("RL-534");
		assertEquals(1, sql.room.getAllRooms().size());
		
		sql.room.addRoom((String) null);
		assertEquals(1, sql.room.getAllRooms().size());
		
		String navn = "T-42";
		int kapasitet = 64;
		int romID = sql.room.addRoom(navn,kapasitet);
		Room rom1 = new Room(romID, navn, kapasitet);
		
		assertEquals(2, sql.room.getAllRooms().size());
		
		assertEquals(romID, rom1.getRoomId());
		assertEquals(navn, rom1.getRoomNumber());
		assertEquals(kapasitet, rom1.getCapacity());
		
		assertEquals(romID, sql.room.getRoom(romID).getRoomId());
		assertEquals(romID, sql.room.getRoom(navn).getRoomId());
		
		assertEquals(kapasitet, sql.room.getRoom(romID).getCapacity());
		assertEquals(kapasitet, sql.room.getRoom(navn).getCapacity());
		
		assertEquals(navn, sql.room.getRoom(romID).getRoomNumber());
		assertEquals(navn, sql.room.getRoom(navn).getRoomNumber());
	}
}