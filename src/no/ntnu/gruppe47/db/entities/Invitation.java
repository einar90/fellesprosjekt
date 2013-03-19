package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import no.ntnu.gruppe47.db.Database;

public class Invitation {

    private final int appointment_id;
    private final int user_id;
    private int response; //-1 negativt, 0 ikke svart, 1 positivt

    private Invitation(int aid, int uid, int response) {
        this.appointment_id = aid;
        this.user_id = uid;
        this.response = response;
    }
    
    public static Invitation create(int aid, int  uid)
    {
		String sql = String.format(
						"INSERT INTO varsel (avtale_id, bruker_id) " +
						"VALUES  (%d, %d, '%s')",
						aid, uid);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
			{
				Invitation alert = new Invitation(rs.getInt("avtale_id"), rs.getInt("bruker_id"), 0);
				return alert;
			}

		} catch (SQLException e) {
			System.out.println("Could not add invitation");
			System.out.println(e.getMessage());
		}
		return null;
    }
    public static Invitation getByID(int aid, int  uid)
    {
		String sql = String.format(
				"SELECT * " +
						"FROM innkalling " +
						"WHERE avtale_id = %d " +
						"AND bruker_id = %d",
						aid, uid);

		try {
			ResultSet rs = Database.makeSingleQuery(sql);

			if (rs.first()) {
				return new Invitation(aid, uid, rs.getInt("svar"));
			}

		} catch (SQLException e) {
			System.out.println("Could not get appointment");
			System.out.println(e.getMessage());
		}

		return null;
    }
    
    public void accept()
    {
    	this.response = 1;
    	this.update();
    	Appointment a = Appointment.getByID(appointment_id);
    	User u = User.getByID(user_id);
    	a.addParticipant(u);
    }
    
    public void reject()
    {
    	this.response = -1;
    	this.update();
    	Appointment a = Appointment.getByID(appointment_id);
    	User u = User.getByID(user_id);
    	if (a.getCreatedBy() != u.getUserId())
    		Alert.create(a.getAppointmentId(), a.getCreatedBy(), u.getName() + " is not coming");
    }
    
    public void reset()
    {
    	this.response = 0;
    	this.update();
    }
    
    private boolean update()
    {
		String sql = String.format(
				"UPDATE innkalling " +
				"SET svar = '%s' " +
				"WHERE avtale_id = %d AND bruker_id = %d",
				this.response, this.appointment_id, this.user_id);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update invitation");
			System.out.println(e.getMessage());
		}
		return false;
    }

	public int getAppointment_id() {
		return appointment_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public int getResponse() {
		return response;
	}
}
