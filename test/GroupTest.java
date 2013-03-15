import static org.junit.Assert.assertEquals;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Group;

import org.junit.Test;


public class GroupTest {
	
	@Test
	public void groupTest()
	{
		Database.reset();
		
		assertEquals(0, Group.getAll().size());
		
		Group.create("Gruppe 1");

		assertEquals(1, Group.getAll().size());
		assertEquals("Gruppe 1", Group.getAll().get(0).getName());
		

		Group.create("Gruppe 1");
		assertEquals(1, Group.getAll().size());
	}
}