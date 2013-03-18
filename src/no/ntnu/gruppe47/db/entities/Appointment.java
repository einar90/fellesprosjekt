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
    private int room_id;
    private String place;

    private Appointment(int appointmentId, int createdBy, Timestamp startTime, Timestamp endTime, String description, String status, int rom_id, String place) {
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.status = status;
        this.room_id = rom_id;
        this.place = place;
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

    public int getCreatedBy(){
    	return createdBy;
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
						rs.getString("status"),
						rs.getInt("rom_id"),
						rs.getString("sted"));
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
						rs.getString("status"),
						rs.getInt("rom_id"),
						rs.getString("sted"));
				apps.add(app);
			}

		} catch (SQLException e) {
			System.out.println("Could not get all users");
			System.out.println(e.getMessage());
		}

		return apps;
	}

	public static ArrayList<Appointment> getAllFor(User user){
		ArrayList<Appointment> apps = new ArrayList<Appointment>();

		String sql = String.format(
				"SELECT DISTINCT(a.avtale_id), opprettet_av, start, slutt, beskrivelse, status, rom_id, sted " +
				"FROM avtale as a, har_avtale as ha, medlem_av as ma " +
				"WHERE ma.bruker_id = %d " +
				"AND ha.avtale_id = a.avtale_id " +
				"AND ma.gruppe_id = ha.gruppe_id;"
					  , user.getUserId());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Appointment app = new Appointment(rs.getInt("avtale_id"),
						rs.getInt("opprettet_av"),
						rs.getTimestamp("start"),
						rs.getTimestamp("slutt"),
						rs.getString("beskrivelse"),
						rs.getString("status"),
						rs.getInt("rom_id"),
						rs.getString("sted"));
				apps.add(app);
			}
			return apps;

		} catch (SQLException e) {
			System.out.println("Could not get this particular user");
			System.out.println(e.getMessage());
		}

		return null;
	}
	
	public static ArrayList<Appointment> getAllBetweenFor(User user, Timestamp start, Timestamp end){
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		
		String sql = String.format(
				"SELECT DISTINCT(a.avtale_id), opprettet_av, start, slutt, beskrivelse, status, rom_id, sted " +
				"FROM avtale as a, har_avtale as ha, medlem_av as ma " +
				"WHERE ma.bruker_id = %d " +
				"AND ma.gruppe_id = ha.gruppe_id " +
				"AND ha.avtale_id = a.avtale_id " +
				"AND start >= '%s' AND start <= '%s';",
				user.getUserId(), start, end);
		
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				appointments.add(new Appointment(
						rs.getInt("avtale_id"),
						rs.getInt("opprettet_av"),
						rs.getTimestamp("start"),
						rs.getTimestamp("slutt"),
						rs.getString("beskrivelse"),
						rs.getString("status"),
						rs.getInt("rom_id"),
						rs.getString("sted")));
			}
			return appointments;
		} catch (SQLException e) {
			System.out.println("Unable to get the dates between " + start + " and " + end);
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Appointment> getAllBetween(Timestamp start, Timestamp end)
	{
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String sql = String.format(
				"SELECT * " +
				"FROM avtale " +
				"WHERE start <= '%s' AND slutt >= '%s'", start, end);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				appointments.add(new Appointment(
						rs.getInt("avtale_id"),
						rs.getInt("opprettet_av"),
						rs.getTimestamp("start"),
						rs.getTimestamp("slutt"),
						rs.getString("beskrivelse"),
						rs.getString("status"),
						rs.getInt("rom_id"),
						rs.getString("sted")));
			}
			return appointments;
		} catch (SQLException e) {
			System.out.println("Unable to get the dates between " + start + " and " + end);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Appointment create(User user, Timestamp start, Timestamp end, String description, String status)
	{
		Room room = Room.getAvailableRoom(start, end, 0);
		if (room == null)
		{
			System.out.println("Couldnt find a room");
			return null;
		}
		String sql = String.format(
				"INSERT INTO avtale (opprettet_av, start, slutt, beskrivelse, status, rom_id) " +
						"VALUES  ('%d', '%s', '%s', '%s', '%s', %d)",
						user.getUserId(), start, end, description, status, room.getRoomId());

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Appointment(rs.getInt("id"),	user.getUserId(), start,
						end, description, status, room.getRoomId(), "");

		} catch (SQLException e) {
			System.out.println("Could not add appointment");
			System.out.println(e.getMessage());
		}
		return null;
	}
	public static Appointment create(User user, Timestamp start, Timestamp end, String description, String status, String place)
	{
		String sql = String.format(
				"INSERT INTO avtale (opprettet_av, start, slutt, beskrivelse, status, sted) " +
						"VALUES  ('%d', '%s', '%s', '%s', '%s', %d)",
						user.getUserId(), start, end, description, status, place);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Appointment(rs.getInt("id"),	user.getUserId(), start,
						end, description, status, 0, place);

		} catch (SQLException e) {
			System.out.println("Could not add appointment");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public boolean addParticipant(Group group) {
		int numParticipants = getUserParticipants().size();
		Room r = Room.getByID(this.room_id);
		if (numParticipants == r.getCapacity())
		{
			Room newRoom = Room.getAvailableRoom(startTime, endTime, numParticipants+1);
			if (newRoom == null)
				return false;
			this.setRoomId(newRoom.getRoomId());
		}
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

	public boolean inviteGroup(Group group) {
		String sql = String.format(
				"INSERT INTO inkalling (gruppe_id, avtale_id) " +
						"VALUES  ('%d', '%d')",
						group.getGroupId(), this.appointmentId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not invite group");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public void setRoomId(int room_id) {
		Room newRoom = Room.getByID(room_id);
		if (newRoom == null || newRoom.getCapacity() < this.getUserParticipants().size())
			return;
		this.room_id = room_id;
		this.update();
		
	}


	private boolean update()
	{
		String sql = String.format(
				"UPDATE avtale " +
						"SET  start = '%s', slutt = '%s', beskrivelse = '%s', " +
								"status = '%s', opprettet_av = %d, rom_id = %d " +
						"WHERE avtale_id = %d",
						startTime, endTime, description, status, createdBy, room_id, appointmentId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update appointment");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public ArrayList<Group> getGroupParticipants() {

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
	
	public ArrayList<User> getUserParticipants() {

		ArrayList<User> users = new ArrayList<User>();

		String sql = String.format(
						"SELECT DISTINCT(bruker_id) " +
						"FROM har_avtale as ha, medlem_av as ma " +
						"WHERE ha.avtale_id = %d " +
						"AND ha.gruppe_id = ma.gruppe_id ",
						this.appointmentId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				int bruker_id = rs.getInt("bruker_id");
				users.add(User.getByID(bruker_id));
			}

		} catch (SQLException e) {
			System.out.println("Could not get participants");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return users;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (!(other instanceof Appointment))
			return false;
		
		Appointment a = (Appointment) other;
		
		return (this.appointmentId == a.getAppointmentId());
	}

	@Override
	public String toString()
	{
		String out = description + ": " + startTime + " - " + endTime + "("+appointmentId+")" +
				" Creadet by: " + User.getByID(createdBy).getName() + " ("+createdBy+").";
		return out;
	}

	public int getRoomId() {
		return room_id;
	}
}
