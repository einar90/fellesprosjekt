package no.ntnu.gruppe47.db.entities;

import java.util.ArrayList;

public class User {

    private final String name;
    private String username;
    private String pasword;
    private final int userId;
    private String email;
    private int phone;
    private ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
    private ArrayList<Calendar> calendarList = new ArrayList<Calendar>();
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

    public int getPhone() {
        return phone;
    }

    public ArrayList<Alarm> getAlarmList() {
        return alarmList;
    }

    public ArrayList<Calendar> getCalendarList() {
        return calendarList;
    }

    public ArrayList<Alert> getAlertList() {
        return alertList;
    }

    public String getPasword() {
        return pasword;
    }

    /**
     * Creates a new user, setting all information.
     *
     * @param name             The full name of the user
     * @param username         The selected username, used for login
     * @param pasword          The selected password, used for login
     * @param userId           The assigned userId, used in the database
     * @param email            The users email address
     * @param phone            The users phone number
     * @param personalCalendar The users personal calendar; is set as the first element in the calendar list
     */
    public User(String name, String username, String pasword, int userId,
                String email, int phone, Calendar personalCalendar) {
        this.name = name;
        this.username = username;
        this.pasword = pasword;
        this.userId = userId;
        this.email = email;
        this.phone = phone;
        calendarList.add(personalCalendar);
    }

    /**
     * Method used for logging in.
     *
     * @param username
     * @param password
     * @return TRUE if the username and password matches the info
     *         stored in the user object and it is able to connect to the database.
     *         If one of the points fails it returns FALSE.
     */
    public boolean login(String username, String password) {
        if (!(username.equals(this.username) && password.equals(this.pasword))) {
            System.out.println("Username or password incorrect.");
            return false;
        }
        // TODO: Prøv å koble til databasen. Hvis det fungerer, returner true.
        System.out.println("Unable to connect to database.");
        return false; // Returnerer false hvis man ikke får koblet til databasen.
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

    public void addCalendar(Calendar calendar) {
        // TODO: Also add calendar to DB.
        calendarList.add(calendar);
    }

    /**
     * @param calendar The calendar entity to delete from the calendar list.
     * @return TRUE if it is able to delete the provided calendar entity
     *         from the user's calendar list. If it cannot find the calendar the
     *         method returns FALSE.
     */
    public boolean deleteCalendar(Calendar calendar) {
        // TODO: Also delete calendar from DB.
        try {
            calendarList.remove(calendar);
            System.out.println("Calendar deleted");
            return true;
        } catch (Exception e) {
            System.out.println("Could not delete calendar because it was not found in users calendar list.");
            return false;
        }
    }


}
