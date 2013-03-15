import static org.junit.Assert.*;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Room;

import org.junit.Test;

public class RoomTest{
	
	@Test
	public void roomTest(){
		Database.reset();
		
		assertEquals(0, Room.getAll().size());
		
		Room.create("RL-534", 4);
		assertEquals(1, Room.getAll().size());
	}
}
