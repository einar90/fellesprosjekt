import static org.junit.Assert.assertEquals;
import junit.framework.TestCase;
import no.ntnu.gruppe47.db.SQLMotor;

import org.junit.Test;

public class RoomTest extends TestCase{
	
	@Test
	public void RoomTest(){
		SQLMotor sql - new RoomTest();
		sql.resetDatabase();
		
		assertEquals(0, sql.room.getAllRooms().size());
		
		sql.room.addRoom("RL-534");
		assertEquals(1, sql.room.getAllRooms().size());
	}
}
