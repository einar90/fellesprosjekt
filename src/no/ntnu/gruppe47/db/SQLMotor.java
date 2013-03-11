package no.ntnu.gruppe47.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import no.ntnu.gruppe47.db.entities.User;
/**
 * The Factory should be the interface where Objects are created and they are mapped to entries in the database
 * 
 * @author orestis
 *
 */
public class SQLMotor {
	
	DBConnection db;
	
	public SQLMotor(Properties properties) throws ClassNotFoundException, SQLException
	{
		 db=new DBConnection(properties);
	}
	
	public  User  createPerson(String name) throws ClassNotFoundException, SQLException
	{
		User u = new User(name);
		String query=String.format("insert into employee (name) values ('%s')",name); 
		db.initialize();
		db.makeSingleUpdate(query);
		db.close();
		
		return u;
	}
	
	public User getUser(int id) throws ClassNotFoundException, SQLException
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
	}
	
}
