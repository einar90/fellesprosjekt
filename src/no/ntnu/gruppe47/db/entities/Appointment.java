package no.ntnu.gruppe47.db.entities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Appointment {

    private int appointmentId;
    private User createdBy;
    private Group participantsGroup;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
    private String status;

    public Appointment(int appointmentId, User createdBy, Group participantsGroupId, Timestamp startTime, Timestamp endTime, String description) {
        this.appointmentId = appointmentId;
        this.createdBy = createdBy;
        this.participantsGroup = participantsGroupId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Group getParticipantsGroup() {

        return participantsGroup;
    }

    public void setParticipantsGroup(Group participantsGroup) {
        this.participantsGroup = participantsGroup;
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
}
