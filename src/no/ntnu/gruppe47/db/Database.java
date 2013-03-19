package no.ntnu.gruppe47.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

import no.ntnu.gruppe47.db.entities.User;


public class Database {

	private static Connection connection;

	public static Connection getConnection()
	{		
		if(Database.connection == null)
			initializeDB();
		return Database.connection;
	}	

	private static void initializeDB()
	{
		try
		{
			File f = new File("properties");
			Properties p = new Properties();
			p.load(new FileInputStream(f));

			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(p.getProperty("dbAddress"), p);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static ResultSet makeSingleQuery(String query) throws SQLException
	{
		Connection c = Database.getConnection();
		Statement st=c.createStatement();
		//st.setQueryTimeout(10800);
		ResultSet rs= st.executeQuery(query);
		return rs;
	}

	public static int makeUpdate(String query) throws SQLException
	{
		Connection c = Database.getConnection();
		Statement st=c.createStatement();
		int res=st.executeUpdate(query);
		return res;
	}

	/**
	 * For making batch updates like many insertions.
	 * 
	 * */
	public static PreparedStatement preparedStatement(String sql) throws SQLException
	{
		Connection c = Database.getConnection();
		return c.prepareStatement(sql);
	}


	public static void closeQuery(ResultSet rs) throws SQLException
	{
		try
		{
			if(rs.getStatement()!=null)
				rs.getStatement().close();
			else
				rs.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void closeConnection()
	{
		Connection c = Database.getConnection();
		try {
			c.close();
			connection = null;
		} catch (SQLException e) {
			System.out.println("Could not close connection");
			e.printStackTrace();
		}
	}


	public static boolean reset()
	{
		try
		{
			  // Delimiter
		    String delimiter = ";";

		    // Create scanner
		    File f = new File("database.sql");
		    Scanner scanner = new Scanner(f);
		    scanner.useDelimiter(delimiter);

		    // Loop through the SQL file statements 
		    while(scanner.hasNext()) {
		        // Get statement 
		        String rawStatement = scanner.next() + delimiter;
		        if (rawStatement.length() <= 5)
		        	continue;
		        try {
		        	//System.out.println(rawStatement);
		            Database.makeUpdate(rawStatement);
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    scanner.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}

	
	public static User login(String username, String password)
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
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				user_id = rs.getInt("bruker_id");
			}

		} catch (SQLException e) {
			System.out.println("Something wrong happened");
			System.out.println(e.getMessage());
		}
		
		return User.getByID(user_id);
	}

}