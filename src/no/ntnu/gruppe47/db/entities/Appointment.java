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

    public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
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
        update();
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
				"SELECT avtale_id " +
				"FROM har_avtale as ha " +
				"WHERE ha.bruker_id = %d "
					  , user.getUserId());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Appointment app = Appointment.getByID(rs.getInt("avtale_id"));
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
				"FROM avtale as a, har_avtale as ha " +
				"WHERE ha.bruker_id = %d " +
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
			System.out.println("Unable to get the appointments between " + start + " and " + end);
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Appointment> getAllBetween(Timestamp start, Timestamp end)
	{
		ArrayList<Appointment> appointments = new ArrayList<Appointment>();
		String sql = String.format(
				"SELECT * " +
				"FROM avtale; ");
//				"WHERE start >= '%s' AND slutt >= '%s';", start, end);
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
	
	public static Appointment create(User user, Timestamp start, Timestamp end, String description)
	{
		Room room = Room.getAvailableRoom(start, end, 0);
		if (room == null)
		{
			System.out.println("Couldn\'t find an avilable room");
			return null;
		}
		String sql = String.format(
						"INSERT INTO avtale (opprettet_av, start, slutt, beskrivelse, status, rom_id) " +
						"VALUES  ('%d', '%s', '%s', '%s', '%s', %d);",
						user.getUserId(), start, end, description, "planlagt", room.getRoomId());

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id;");
			if (rs.first())
			{
				Appointment a = new Appointment(rs.getInt("id"),	user.getUserId(), start,
						end, description, "planlagt", room.getRoomId(),"");
				a.addParticipant(user);
				return a;
			}

		} catch (SQLException e) {
			System.out.println("Could not add appointment");
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Appointment create(User user, Timestamp start, Timestamp end, String description, String place)
	{
		String sql = String.format(
				"INSERT INTO avtale (opprettet_av, start, slutt, beskrivelse, status, sted) " +
						"VALUES  ('%d', '%s', '%s', '%s', '%s', '%s');",
						user.getUserId(), start, end, description, "planlagt", place);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
			{
				Appointment a = new Appointment(rs.getInt("id"),	user.getUserId(), start,
						end, description, "planlagt", 0, place);
				a.addParticipant(user);
				return a;
			}

		} catch (SQLException e) {
			System.out.println("Could not add appointment");
			System.out.println(e.getMessage());
		}
		return null;
	}

	public boolean addParticipant(User user) {
		if (room_id != 0)
		{
			int numParticipants = getParticipants().size();
			Room r = Room.getByID(this.room_id);
			if (numParticipants == r.getCapacity())
			{
				Room newRoom = Room.getAvailableRoom(startTime, endTime, numParticipants+1);
				if (newRoom == null)
					return false;
				this.setRoomId(newRoom.getRoomId());
			}
		}
		
		String sql = String.format(
						"INSERT INTO har_avtale (bruker_id, avtale_id) " +
						"VALUES  (%d, %d);",
						user.getUserId(), this.appointmentId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not add participant");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public void inviteGroup(Group group) {
		for (User u : group.getMembers())
			inviteUser(u);
	}
	
	public boolean inviteUser(User user)
	{
		String sql = String.format(
						"INSERT INTO innkalling (bruker_id, avtale_id) " +
						"VALUES  ('%d', '%d');",
						user.getUserId(), this.appointmentId);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not invite user");
			System.out.println(e.getMessage());
		}

		return false;
	}

	public void setRoomId(int room_id) {
		Room newRoom = Room.getByID(room_id);
		if (newRoom == null || newRoom.getCapacity() < this.getParticipants().size())
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
						"WHERE avtale_id = %d;",
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
	
	public ArrayList<User> getParticipants() {

		ArrayList<User> users = new ArrayList<User>();

		String sql = String.format(
						"SELECT bruker_id " +
						"FROM har_avtale as ha " +
						"WHERE ha.avtale_id = %d;",
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
	
	public ArrayList<Invitation> getInvitations() {

		ArrayList<Invitation> invitations = new ArrayList<Invitation>();

		String sql = String.format(
						"SELECT bruker_id, avtale_id, svar " +
						"FROM innkalling as i " +
						"WHERE i.avtale_id = %d;",
						this.appointmentId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				Invitation invite = Invitation.getByID(rs.getInt("avtale_id"), rs.getInt("bruker_id"));
				invitations.add(invite);
			}

		} catch (SQLException e) {
			System.out.println("Could not get participants");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return invitations;
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

	public ArrayList<User> getInvitesWithResponse(int response) {
		ArrayList<User> users = new ArrayList<User>();
		ArrayList<Invitation> invitations = getInvitations();
		for (Invitation i : invitations)
		{
			if (i.getResponse() == response)
				users.add(User.getByID(i.getUser_id()));
		}
		return users;
	}

	public boolean deleteAllInvites() {
		String sql = String.format(
				"DELETE FROM innkalling " +
						"WHERE avtale_id = %d;"
						,getAppointmentId());
		try {
			Database.makeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println("Unable to get the creator of this appointment: " + getAppointmentId());
			e.printStackTrace();
		}
		return false;

	}
}
