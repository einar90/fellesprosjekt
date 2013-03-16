package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;


public class Group {

    private int groupId;
    private String name;
    boolean isPrivate;


    public int getGroupId() {
		return groupId;
	}


	public String getName() {
		return name;
	}

	public boolean isPrivate() {
		return isPrivate;
	}


	private Group(int groupId, String name, boolean isPrivate) {
        this.groupId = groupId;
        this.name = name;
        this.isPrivate = isPrivate;
    }
	
	public static ArrayList<Group> getAll()
	{
		ArrayList<Group> groups = new ArrayList<Group>();
		
		String sql = "SELECT * FROM gruppe";
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Group group = new Group(rs.getInt("gruppe_id"), rs.getString("navn"), rs.getBoolean("privat"));
				groups.add(group);
			}

		} catch (SQLException e) {
			System.out.println("Could not get user");
			System.out.println(e.getMessage());
		}

		return groups;
	}

	public static Group getByID(int gruppe_id)
	{
		String sql = String.format(
				"SELECT * " +
						"FROM gruppe " +
						"WHERE gruppe_id = %d",
						gruppe_id);

		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				return new Group(	rs.getInt("gruppe_id"),
									rs.getString("navn"),
									rs.getBoolean("privat"));
			}

		} catch (SQLException e) {
			System.out.println("Could not get group");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public static Group create(String name) {
		String sql = String.format(
				"INSERT INTO gruppe (navn) " +
						"VALUES  ('%s')",
						name);

		try {
			Database.makeUpdate(sql);

			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Group(rs.getInt("id"), name, false);

		} catch (SQLException e) {
			System.out.println("Could not add group");
			System.out.println(e.getMessage());
		}

		return null;
	}
	
	public static Group createPrivate(String name) {
		String sql = String.format(
				"INSERT INTO gruppe (navn, privat) " +
						"VALUES  ('%s', true)",
						name);

		try {
			Database.makeUpdate(sql);

			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Group(rs.getInt("id"), name, true);

		} catch (SQLException e) {
			System.out.println("Could not add group");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public boolean addMember(User user) {
		if (this.isPrivate && user.getUsername() != this.getName())
			return false;
		
		String sql = String.format(
				"INSERT INTO medlem_av (bruker_id, gruppe_id) " +
						"VALUES  ('%d', '%d')",
						user.getUserId(), this.groupId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not add group");
			System.out.println(e.getMessage());
		}

		return false;
	}


	public ArrayList<User> getMembers()
	{
		ArrayList<User> users = new ArrayList<User>();

		String sql = String.format(
				"SELECT bruker_id " +
						"FROM medlem_av " +
						"WHERE gruppe_id = %d",
						this.groupId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				int bruker_id = rs.getInt("bruker_id");
				users.add(User.getByID(bruker_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get members");
			System.out.println(e.getMessage());
		}

		return users;
	}
	public ArrayList<Appointment> getAppointments()
	{
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();

		String sql = String.format(
				"SELECT avtale_id " +
						"FROM har_avtale " +
						"WHERE gruppe_id = %d",
						this.groupId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				int avtale_id = rs.getInt("avtale_id");
				appointments.add(Appointment.getByID(avtale_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get appointments");
			System.out.println(e.getMessage());
		}

		return appointments;
	}
}
