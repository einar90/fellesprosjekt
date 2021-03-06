package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

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
			{
				User user = new User(rs.getInt("id"), username, password, name, email);
				Group personalGroup = Group.createPrivate(user.getUsername());
				personalGroup.addMember(user);
				return user;
			}

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
 	
	public ArrayList<Group> getGroups(boolean includePrivate)
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
			if (!includePrivate)
				groups.remove(getPrivateGroup());

		} catch (SQLException e) {
			System.out.println("Could not get user");
			System.out.println(e.getMessage());
		}

		return groups;
	}

	public Appointment createAppointment(Timestamp start, Timestamp end, String description)
	{
		return Appointment.create(this, start, end, description);
	}
	
	public Appointment createAppointment(Timestamp start, Timestamp end, String description, String place)
	{
		return Appointment.create(this, start, end, description, place);
	}
	
	public ArrayList<Appointment> getAppointments()
	{
		return Appointment.getAllFor(this);
	}

	
	public ArrayList<Appointment> getAppointmentsForWeek(int week)
	{
		DateTime date = new DateTime();
		date = date.withWeekOfWeekyear(week).withDayOfWeek(1).withTimeAtStartOfDay();
		Timestamp start = new Timestamp(date.getMillis());
		date = date.withDayOfWeek(7).plusDays(1).withTimeAtStartOfDay();
		Timestamp end = new Timestamp(date.getMillis());

		ArrayList<Appointment> appointments = Appointment.getAllBetweenFor(this, start, end);
		
		return appointments;
	}
	
	
	public ArrayList<Appointment> getAppointmentsBetween(Timestamp start, Timestamp end)
	{
		return Appointment.getAllBetweenFor(this, start, end);
	}
	
	public Group getPrivateGroup()
	{
		String sql = String.format(
				"SELECT gruppe_id " +
						"FROM gruppe " +
						"WHERE navn = '%s' " +
						"AND privat = true",
						username);

		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				return Group.getByID(rs.getInt("gruppe_id"));
			}

		} catch (SQLException e) {
			System.out.println("Could not get group");
			System.out.println(e.getMessage());
		}

		return null;
	}
	
	public ArrayList<Invitation> getInvitations()
	{
		ArrayList<Invitation> invitations = new ArrayList<Invitation>();

		String sql = String.format(
				"SELECT DISTINCT(avtale_id), bruker_id " +
				"FROM innkalling as i " +
				"WHERE i.bruker_id = %d ",
				userId);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				invitations.add(Invitation.getByID(rs.getInt("avtale_id"), rs.getInt("bruker_id")));
			}
			return invitations;
			
		} catch (SQLException e) {
			System.out.println("Could not get invitations");
			System.out.println(e.getMessage());
		}

		return invitations;
	}
	public ArrayList<Invitation> getInvitationsWithResponse(int r)
	{
		ArrayList<Invitation> invitations = new ArrayList<Invitation>();

		String sql = String.format(
				"SELECT DISTINCT(avtale_id), bruker_id " +
				"FROM innkalling as i " +
				"WHERE i.bruker_id = %d " +
				"AND i.svar = %d",
				userId, r);
		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			while (rs.next()) {
				invitations.add(Invitation.getByID(rs.getInt("avtale_id"), rs.getInt("bruker_id")));
			}
			return invitations;
			
		} catch (SQLException e) {
			System.out.println("Could not get invitations");
			System.out.println(e.getMessage());
		}

		return invitations;
	}
	
	public ArrayList<Alarm> getAlarms()
	{
		return Alarm.getAllAlarmsForUser(this);
	}
	public ArrayList<Alert> getAlerts()
	{
		return Alert.getAllAlertsForUser(this);
	}
	
	public void deleteAppointment(Appointment appointment)
	{
		if (this.getUserId() == appointment.getCreatedBy())
		{
			ArrayList<User> participants = appointment.getParticipants();
			appointment.setStatus("avlyst");
			appointment.deleteAllInvites();
			for (User u : participants)
				Alert.create(appointment.getAppointmentId(), u.getUserId(), "Møte avlyst");
		}
		else
		{
			Invitation i = Invitation.getByID(appointment.getAppointmentId(), this.getUserId());
			i.reject();
		}
		if (appointment.getParticipants().contains(this))
			appointment.removeParticipant(this);
	}
	
	public boolean addAlarm(Appointment a)
	{
		DateTime alarmTime = new DateTime(a.getStartTime()).minusMinutes(10);
		Timestamp alarmTimestamp = new Timestamp(alarmTime.getMillis());
		String sql = String.format("INSERT INTO alarm (avtale_id, bruker_id, tidspunkt) " +
									"VALUES (%d, %d, '%s')",
									a.getAppointmentId(), this.getUserId(), alarmTimestamp);
		try
		{
			Database.makeUpdate(sql);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Could not add alarm");
			e.printStackTrace();
		}
		return false;
	}
}
