package no.ntnu.gruppe47.db.entities;

import java.util.ArrayList;

public class User {

    private String name;
    private String username;
    private String pasword;
    private int userId;
    private String email;
    private int phone;
    private ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
    private ArrayList<Calendar> calendarList = new ArrayList<Calendar>();
    private ArrayList<Alert> alertList = new ArrayList<Alert>();

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

    public boolean login(String username, String password) {
        if (!(username.equals(this.username) && password.equals(this.pasword))) {
            System.out.println("Username or password incorrect.");
            return false;
        }
        // TODO: Prøv å koble til databasen. Hvis det fungerer, returner true.
        System.out.println("Unable to connect to database.");
        return false; // Returnerer false hvis man ikke får koblet til databasen.
    }

}
