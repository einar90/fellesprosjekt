package no.ntnu.gruppe47.db.entities;

import java.util.Date;

public class Alarm {

    private User user;
    private Appointment appointment;
    private Date time;

    public Alarm(User user, Appointment appointment, Date time) {
        this.user = user;
        this.appointment = appointment;
        this.time = time;
        user.addAlarm(this);
    }
}
