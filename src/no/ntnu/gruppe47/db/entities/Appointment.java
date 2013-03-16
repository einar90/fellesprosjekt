package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import no.ntnu.gruppe47.db.Database;

public class Appointment {

    private int appointmentId;
    private int createdBy;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
    private String status;

    private Appointment(int appointmentId, int createdBy, Timestamp startTime, Timestamp endTime, String description, String status) {
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }
    

	public static Appointment getByID(int app_id)
	{
		String sql = String.format(
				"SELECT * " +
						"FROM avtale " +
						"WHERE avtale_id = %d",
						app_id);

		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				return new Appointment(rs.getInt("avtale_id"),
						rs.getInt("opprettet_av"),
						rs.getTimestamp("start"),
						rs.getTimestamp("slutt"),
						rs.getString("beskrivelse"),
						rs.getString("status"));
			}

		} catch (SQLException e) {
			System.out.println("Could not get appointment");
			System.out.println(e.getMessage());
		}

		return null;
	}

	public static ArrayList<Appointment> getAll()
	{
		ArrayList<Appointment> apps = new ArrayList<Appointment>();

		String sql = "SELECT * FROM avtale";
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Appointment app = new Appointment(rs.getInt("avtale_id"),
												rs.getInt("opprettet_av"),
												rs.getTimestamp("start"),
												rs.getTimestamp("slutt"),
												rs.getString("beskrivelse"),
												rs.getString("status"));
				apps.add(app);
			}

		} catch (SQLException e) {
			System.out.println("Could not get all users");
			System.out.println(e.getMessage());
		}

		return apps;
	}

	public static ArrayList<Appointment> getAllBetween(Timestamp start, Timestamp end)
	{
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String sql = String.format(
				"SELECT * " +
				"FROM avtale " +
				"WHERE start <= %d AND slutt >= %d", start.getTime(), end.getTime());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				appointments.add(new Appointment(
						rs.getInt("avtale_id"),
   					    rs.getInt("opprettet_av"),
						rs.getTimestamp("start"),
						rs.getTimestamp("slutt"),
						rs.getString("beskrivelse"),
						rs.getString("status")));
			}
		} catch (SQLException e) {
			System.out.println("Unable to get the dates between " + start + " and " + end);
			e.printStackTrace();
		}
		
		return appointments;
	}
	
	public static Appointment create(User user, Timestamp start, Timestamp end, String description, String status)
	{
		String sql = String.format(
				"INSERT INTO avtale (opprettet_av, start, slutt, beskrivelse, status) " +
						"VALUES  ('%d', '%s', '%s', '%s', '%s')",
						user.getUserId(), start, end, description, status);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Appointment(rs.getInt("id"),	user.getUserId(), start,
						end, description, status);

		} catch (SQLException e) {
			System.out.println("Could not add user");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public boolean addParticipant(Group group) {
		String sql = String.format(
				"INSERT INTO har_avtale (gruppe_id, avtale_id) " +
						"VALUES  ('%d', '%d')",
						group.getGroupId(), this.appointmentId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not add participant");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public ArrayList<Group> getParticipants() {

		ArrayList<Group> groups = new ArrayList<Group>();

		String sql = String.format(
				"SELECT gruppe_id " +
						"FROM har_avtale " +
						"WHERE avtale_id = %d",
						this.appointmentId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				int gruppe_id = rs.getInt("gruppe_id");
				groups.add(Group.getByID(gruppe_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get participants");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return groups;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Appointment))
			return false;
		
		Appointment a = (Appointment) other;
		
		return (this.appointmentId == a.getAppointmentId());
	}
}
