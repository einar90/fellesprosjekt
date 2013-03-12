package no.ntnu.gruppe47.db;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;

/**
 * The Factory should be the interface where Objects are created and they are mapped to entries in the database
 *
 * @author orestis
 */
public class SQLMotor {

	public DBConnection db;
	
	public GroupSQL group;
	public UserSQL user;

	public SQLMotor()
	{
		try
		{
			db = new DBConnection("properties");
			group = new GroupSQL(db);
			user = new UserSQL(db);
		}
		catch (Exception e)
		{
			System.out.println("Could not connect to database, exiting");
			System.exit(0);
		}
	}

	public boolean resetDatabase()
	{
		try
		{
			  // Delimiter
		    String delimiter = ";";

		    // Create scanner
		    Scanner scanner = new Scanner(new File("database.sql")).useDelimiter(delimiter);

		    // Loop through the SQL file statements 
		    while(scanner.hasNext()) {
		        // Get statement 
		        String rawStatement = scanner.next() + delimiter;
		        if (rawStatement.length() <= 5)
		        	continue;
		        try {
		            db.makeUpdate(rawStatement);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}

	
	
}
