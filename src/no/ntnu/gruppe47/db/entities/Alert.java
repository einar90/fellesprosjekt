package no.ntnu.gruppe47.db.entities;

/**
 * Created by:
 * User: Einar
 * Date: 12.03.13
 * Time: 10:47
 */
public class Alert {

    private String status;
    private Appointment appointment;

    public Alert(Appointment appointment) {
        this.appointment = appointment;
        status = "Not viewed";
    }

    public void setStatusViewed() {
        status = "Viewed";
    }

    public void setStatusAccepted() {
        status = "Accepted";
    }

    public void setStatusDeclined() {
        status = "Declined";
    }

    public String getStatus() {
        return status;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
