import static org.junit.Assert.assertEquals;
import no.ntnu.gruppe47.db.SQLMotor;

import org.junit.Test;


public class GroupTest {
	
	@Test
	public void groupTest()
	{
		SQLMotor sql = new SQLMotor();
		sql.resetDatabase();
		
		assertEquals(0, sql.group.getAllGroups().size());
		
		sql.group.addGroup("Gruppe 1");

		assertEquals(1, sql.group.getAllGroups().size());
		assertEquals("Gruppe 1", sql.group.getAllGroups().get(0).getName());
		

		sql.group.addGroup("Gruppe 1");
		assertEquals(1, sql.group.getAllGroups().size());
	}
}