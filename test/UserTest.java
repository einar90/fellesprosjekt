import static org.junit.Assert.*;

import org.junit.Test;

import no.ntnu.gruppe47.db.SQLMotor;
import no.ntnu.gruppe47.db.entities.User;


public class UserTest {
	
	@Test
	public void userTest()
	{
		SQLMotor sql = new SQLMotor();
		sql.resetDatabase();

		User attempt1 = sql.login("user1", "pass");
		assertNull(attempt1);

		sql.addUser("User1", "pass", "Håkon Bråten", "h@h.com");
		User attempt2 = sql.login("user1", "pass");
		assertNotNull(attempt2);

		attempt2.setName("Allah Akhbar");
		sql.updateUser(attempt2);
		User attempt3 = sql.login("user1", "pass");
		assertFalse(attempt2.equals(attempt3));

		sql.deleteUser(attempt3);
		User attempt4 = sql.login("user1", "pass");
		assertNull(attempt4);

		sql.db.closeConnection();
	}
}
