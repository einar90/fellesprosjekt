package no.ntnu.gruppe47.db;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import no.ntnu.gruppe47.db.entities.User;

/**
 * The Factory should be the interface where Objects are created and they are mapped to entries in the database
 *
 * @author orestis
 */
public class SQLMotor {

	public DBConnection db;

	public SQLMotor() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException, IOException {
		db = new DBConnection("properties");
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

	public User login(String username, String password)
	{
		String sql = String.format(
						"SELECT bruker_id " +
						"FROM person " +
						"WHERE brukernavn = \"%s\"" +
						"AND passord = \"%s\" ",
						username,
						password);

		int user_id = -1;
		try {
			ResultSet rs = db.makeSingleQuery(sql);

			if (rs.first()) {
				user_id = rs.getInt("bruker_id");
			}

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}
		return getUser(user_id);
	}

	public User getUser(int user_id)
	{
		String sql = String.format(
				"SELECT * " +
						"FROM person " +
						"WHERE bruker_id = %d",
						user_id);

		try {
			ResultSet rs = db.makeSingleQuery(sql);

			if (rs.first()) {
				return new User(rs.getInt("bruker_id"),
						rs.getString("brukernavn"),
						rs.getString("passord"),
						rs.getString("navn"),
						rs.getString("epost"));
			}

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public User addUser(String username, String password, String name, String email)
	{
		String sql = String.format(
						"INSERT INTO person (brukernavn, passord, navn, epost) " +
						"VALUES  ('%s', '%s', '%s', '%s')",
						username, password, name, email);

		try {
			db.makeUpdate(sql);
			return login(username, password);

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public boolean updateUser(User user)
	{
		String sql = String.format(
						"UPDATE person " +
						"SET  brukernavn = '%s', passord = '%s', navn = '%s', epost = '%s' " +
						"WHERE bruker_id = %d",
						user.getUsername(), user.getPasword(), user.getName(), user.getEmail(), user.getUserId());

		try {
			db.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}

		return false;
	}
	public boolean deleteUser(User user)
	{
		String sql = String.format(
						"DELETE FROM person " +
						"WHERE bruker_id = %d",
						user.getUserId());

		try {
			db.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}

		return false;
	}
}
