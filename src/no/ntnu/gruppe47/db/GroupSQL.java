package no.ntnu.gruppe47.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.entities.Group;
import no.ntnu.gruppe47.db.entities.User;

public class GroupSQL {
	
	DBConnection db;
	
	public GroupSQL(DBConnection db)
	{
		this.db = db;
	}

	public ArrayList<Group> getGroups(User user)
	{
		ArrayList<Group> groups = new ArrayList<Group>();
		
		String sql = String.format(
				"SELECT gruppe_id " +
						"FROM medlem_av " +
						"WHERE bruker_id = %d",
						user.getUserId());
		try {
			ResultSet rs = db.makeSingleQuery(sql);

			while (rs.next()) {
				int gruppe_id = rs.getInt("gruppe_id");
				groups.add(getGroup(gruppe_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get user");
			System.out.println(e.getMessage());
		}

		return groups;
	}
	public ArrayList<Group> getAllGroups()
	{
		ArrayList<Group> groups = new ArrayList<Group>();
		
		String sql = "SELECT gruppe_id FROM gruppe";
		try {
			ResultSet rs = db.makeSingleQuery(sql);

			while (rs.next()) {
				int gruppe_id = rs.getInt("gruppe_id");
				groups.add(getGroup(gruppe_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get all groups");
			System.out.println(e.getMessage());
		}

		return groups;
	}

	public Group getGroup(int gruppe_id)
	{
		String sql = String.format(
				"SELECT * " +
						"FROM gruppe " +
						"WHERE gruppe_id = %d",
						gruppe_id);

		try {
			ResultSet rs = db.makeSingleQuery(sql);

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

	public Group addGroup(String name) {
		String sql = String.format(
				"INSERT INTO gruppe (navn) " +
						"VALUES  ('%s')",
						name);

		try {
			db.makeUpdate(sql);

			ResultSet rs = db.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			rs.first();
			return getGroup(rs.getInt("id"));

		} catch (SQLException e) {
			System.out.println("Could not add group");
			System.out.println(e.getMessage());
		}

		return null;
	}
}
