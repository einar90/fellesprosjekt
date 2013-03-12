package no.ntnu.gruppe47.db;

import no.ntnu.gruppe47.db.entities.Calendar;
import no.ntnu.gruppe47.db.entities.User;

import java.sql.SQLException;
import java.util.Properties;

/**
 * The Factory should be the interface where Objects are created and they are mapped to entries in the database
 *
 * @author orestis
 */
public class SQLMotor {

    DBConnection db;

    public SQLMotor(Properties properties) throws ClassNotFoundException, SQLException {
        db = new DBConnection(properties);
    }

    public User createPerson(String name, String username, String pasword, int userId,
                             String email, int phone, Calendar personalCalendar) throws ClassNotFoundException, SQLException {
        User u = new User(name, username, pasword, userId, email, phone, personalCalendar);
        String query = String.format("insert into employee (name) values ('%s')", name);
        db.initialize();
        db.makeSingleUpdate(query);
        db.close();

        return u;
    }

    // TODO: Denne metoden må implementeres på nytt etter endringer i User konstruktøren
    /*public User getUser(int id) throws ClassNotFoundException, SQLException
	{
		String query=String.format("Select name from employee where id=%d",id);
		db.initialize();
		ResultSet rs=db.makeSingleQuery(query);
		String name=null;
		while(rs.next())
		{
			name=rs.getString(1);
		}
		
		User u = new User(name);
		rs.close();
		db.close();
		
		return u;
	}*/

}
