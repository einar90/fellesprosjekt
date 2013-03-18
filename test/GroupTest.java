

import static org.junit.Assert.assertEquals;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;

import org.junit.Test;


public class GroupTest {
	
	@Test
	public void groupCreateTest()
	{
		Database.reset();
		
		assertEquals(0, Group.getAll().size());
		
		Group.create("Gruppe 1");

		assertEquals(1, Group.getAll().size());
		assertEquals("Gruppe 1", Group.getAll().get(0).getName());
	}
	
	@Test
	public void groupMemberTest()
	{
		Database.reset();
		
		User user = User.create("user", "pass", "kjhad", "adkjh");
		Group group = Group.create("TheUltimateGroup");
		group.addMember(user);
		
		assertEquals(2, user.getGroups(true).size());
		assertEquals(1, user.getGroups(false).size());
		assertEquals(1, group.getMembers().size());
	}
}