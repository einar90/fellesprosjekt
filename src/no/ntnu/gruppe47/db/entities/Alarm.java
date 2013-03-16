package no.ntnu.gruppe47.db.entities;

import java.util.Date;

public class Alarm {

    private final Appointment appointment;
    private Date time;

    public Alarm(Appointment appointment, Date time) {
        this.appointment = appointment;
        this.time = time;
    }
}
