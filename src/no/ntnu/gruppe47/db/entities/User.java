package no.ntnu.gruppe47.db.entities;

import java.util.ArrayList;

public class User {

    private String name;
    private String username;
    private String password;
    private final int userId;
    private String email;
    private ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
    private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
    private ArrayList<Alert> alertList = new ArrayList<Alert>();

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

    public ArrayList<Alarm> getAlarmList() {
        return alarmList;
    }

    public ArrayList<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public ArrayList<Alert> getAlertList() {
        return alertList;
    }

    public void addAlert(Alert alert) {
        alertList.add(alert);
    }

    public String getPasword() {
        return password;
    }

    /**
     * Creates a new user, setting all information.
     *
     * @param name     The full name of the user
     * @param username The selected username, used for login
     * @param pasword  The selected password, used for login
     * @param userId   The assigned userId, used in the database
     * @param email    The users email address
     * @param phone    The users phone number
     */
    public User(int userId, String username, String pasword, String name, 
                String email) {
        this.name = name;
        this.username = username;
        this.password = pasword;
        this.userId = userId;
        this.email = email;
    }

    public void addAlarm(Alarm alarm) {
        // TODO: Also add alarm to DB
        alarmList.add(alarm);
    }

    /**
     * @param alarm The alarm entity to delete from the alarm list.
     * @return TRUE if it is able to delete the provided alarm entity
     *         from the user's alarm list. If it cannot find the alarm the method
     *         returns FALSE.
     */
    public boolean deleteAlarm(Alarm alarm) {
        // TODO: Also delete alarm from DB.
        try {
            alarmList.remove(alarm);
            System.out.println("Alarm deleted");
            return true;
        } catch (Exception e) {
            System.out.println("Could not delete alarm because it was not found in users alarm list.");
            return false;
        }
    }

    public void addAppointment(Appointment appointment) {
        appointmentList.add(appointment);
    }

    /**
     * @param appointment The appointment entity to delete from the appointment list.
     * @return TRUE if it is able to delete the provided calendar entity
     *         from the user's calendar list. If it cannot find the calendar the
     *         method returns FALSE.
     */
    public boolean deleteAppointment(Appointment appointment) {
        // TODO: Also delete calendar from DB.
        try {
            appointmentList.remove(appointment);
            System.out.println("Appointment deleted");
            return true;
        } catch (Exception e) {
            System.out.println("Could not delete appointment because it was not found in users appointment list.");
            return false;
        }
    }


    public String toString() {
        return "User: " + userId + " - " + username + " - " + password + " - " + name + " - " + email;
    }

	public void setName(String newName) {
		this.name = newName;
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
		if (other.getPasword() != this.getPasword())
			return false;
		if (other.getName() != this.getName())
			return false;
		if (other.getEmail() != this.getEmail())
			return false;
		
		return true;
	}


}
