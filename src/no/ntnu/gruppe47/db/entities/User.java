package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;


public class User {

	private String name;
	private String username;
	private String password;
	private final int userId;
	private String email;

	public static User create(String username, String password, String name, String email)
	{
		String sql = String.format(
				"INSERT INTO person (brukernavn, passord, navn, epost) " +
						"VALUES  ('%s', '%s', '%s', '%s')",
						username, password, name, email);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new User(rs.getInt("id"), username, password, name, email);

		} catch (SQLException e) {
			System.out.println("Could not add user");
			System.out.println(e.getMessage());
		}
		return null;
	}

	private User(int userId, String username, String pasword, String name, 
			String email) {
		this.name = name;
		this.username = username;
		this.password = pasword;
		this.userId = userId;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public int getUserId() {
		return userId;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.update();
	}

	public void setName(String name) {
		this.name = name;
		this.update();
	}

	public void setUsername(String username) {
		this.username = username;
		this.update();
	}

	public void setEmail(String email) {
		this.email = email;
		this.update();
	}

	@Override
	public String toString() {
		return "User: " + userId + " - " + username + " - " + password + " - " + name + " - " + email;
	}

	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof User))
			return false;

		User other = (User) o;

		if (other.getUserId() != this.getUserId())
			return false;
		if (other.getUsername() != this.getUsername())
			return false;
		if (other.getPassword() != this.getPassword())
			return false;
		if (other.getName() != this.getName())
			return false;
		if (other.getEmail() != this.getEmail())
			return false;

		return true;
	}

	private boolean update()
	{
		String sql = String.format(
				"UPDATE person " +
						"SET  brukernavn = '%s', passord = '%s', navn = '%s', epost = '%s' " +
						"WHERE bruker_id = %d",
						username, password, name, email, userId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update user");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public boolean delete()
	{
		String sql = String.format(
				"DELETE FROM person " +
						"WHERE bruker_id = %d",
						this.userId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return false;
	}

	// Static methods

	public static User getByID(int user_id)
	{
		String sql = String.format(
				"SELECT * " +
						"FROM person " +
						"WHERE bruker_id = %d",
						user_id);

		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				return new User(rs.getInt("bruker_id"),
						rs.getString("brukernavn"),
						rs.getString("passord"),
						rs.getString("navn"),
						rs.getString("epost"));
			}

		} catch (SQLException e) {
			System.out.println("Could not get user");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public static ArrayList<User> getAll()
	{
		ArrayList<User> users = new ArrayList<User>();

		String sql = "SELECT * FROM person";
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				User user = new User(	rs.getInt("bruker_id"),
						rs.getString("brukernavn"),
						rs.getString("passord"),
						rs.getString("navn"),
						rs.getString("epost"));
				users.add(user);
			}

		} catch (SQLException e) {
			System.out.println("Could not get all users");
			System.out.println(e.getMessage());
		}

		return users;
	}

	public ArrayList<Group> getGroups()
	{
		ArrayList<Group> groups = new ArrayList<Group>();

		String sql = String.format(
				"SELECT gruppe_id " +
						"FROM medlem_av " +
						"WHERE bruker_id = %d",
						userId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				int gruppe_id = rs.getInt("gruppe_id");
				groups.add(Group.getByID(gruppe_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get user");
			System.out.println(e.getMessage());
		}

		return groups;
	}
}
