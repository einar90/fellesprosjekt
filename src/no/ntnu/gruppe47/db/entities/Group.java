package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;


public class Group {

    private int groupId;
    private String name;


    public int getGroupId() {
		return groupId;
	}


	public String getName() {
		return name;
	}


	private Group(int groupId, String name) {
        this.groupId = groupId;
        this.name = name;
    }
	
	public static ArrayList<Group> getAll()
	{
		ArrayList<Group> groups = new ArrayList<Group>();
		
		String sql = "SELECT * FROM gruppe";
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Group group = new Group(rs.getInt("gruppe_id"), rs.getString("navn"));
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
									rs.getString("navn"));
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
				return new Group(rs.getInt("id"), name);

		} catch (SQLException e) {
			System.out.println("Could not add group");
			System.out.println(e.getMessage());
		}

		return null;
	}
}
