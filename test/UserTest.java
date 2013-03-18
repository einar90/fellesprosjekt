

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import no.ntnu.gruppe47.db.Database;
import no.ntnu.gruppe47.db.entities.User;

import org.junit.Test;


public class UserTest {
	
	@Test
	public void userTest()
	{
		Database.reset();

		User attempt1 = Database.login("user1", "pass");
		assertNull(attempt1);

		User.create("User1", "pass", "Håkon Bråten", "h@h.com");
		User attempt2 = Database.login("user1", "pass");
		assertNotNull(attempt2);

		User attempt3 = Database.login("user1", "galtpass");
		assertNull(attempt3);

		attempt2.setName("Allah Akhbar");
		User attempt4 = Database.login("user1", "pass");
		assert(attempt2.equals(attempt4));

		attempt4.delete();
		User attempt5 = Database.login("user1", "pass");
		assertNull(attempt5);
	}
}
