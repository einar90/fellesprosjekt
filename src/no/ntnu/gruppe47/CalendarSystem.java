package no.ntnu.gruppe47;

import no.ntnu.gruppe47.db.SQLMotor;
import no.ntnu.gruppe47.db.entities.User;

public class CalendarSystem {
	public static void main(String[] args) {
		try
		{
			SQLMotor sql = new SQLMotor();
			sql.resetDatabase();
			
	        User attempt1 = sql.login("user1", "pass");
	        System.out.println(attempt1);
	        
	        sql.addUser("User1", "pass", "Håkon Bråten", "h@h.com");
	        User attempt2 = sql.login("user1", "pass");
	        System.out.println(attempt2);
	        
	        attempt2.setName("Allah Akhbar");
	        sql.updateUser(attempt2);
	        User attempt3 = sql.login("user1", "pass");
	        System.out.println(attempt3);
	        
	        sql.deleteUser(attempt3);
	        User attempt4 = sql.login("user1", "pass");
	        System.out.println(attempt4);
	        
	        sql.db.closeConnection();
		}
		catch(Exception e)
		{
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
	}
}
