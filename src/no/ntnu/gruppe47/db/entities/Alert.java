package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;

public class Alert {

    private final int appointment_id;
    private final int user_id;
    private String text;

    private Alert(int aid, int uid, String t) {
        this.appointment_id = aid;
        this.user_id = uid;
        this.text = t;
    }
    
    public static Alert create(int aid, int  uid, String status)
    {
		String sql = String.format(
						"INSERT INTO varsel (avtale_id, bruker_id, tekst) " +
						"VALUES  (%d, %d, '%s')",
						aid, uid, status);

		try {
			Database.makeUpdate(sql);
			Alert alert = new Alert(aid, uid, status);
			return alert;

		} catch (SQLException e) {
			System.out.println("Could not add alert");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    public void setStatus(String status)
    {
    	this.text = status;
    	this.update();
    }
    
    private boolean update()
    {
		String sql = String.format(
				"UPDATE varsel " +
				"SET tekst = '%s' " +
				"WHERE avtale_id = %d AND bruker_id = %d",
				this.text, this.appointment_id, this.user_id);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update alert");
			System.out.println(e.getMessage());
		}
		return false;
    }

    public static ArrayList<Alert> getAllAlertsForUser(User user){
		ArrayList<Alert> alarms = new ArrayList<Alert>();
		String sql = String.format(
					"SELECT avtale_id, bruker_id, tekst " +
					"FROM varsel as v " +
					"WHERE v.bruker_id = %d", user.getUserId());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				alarms.add(new Alert(rs.getInt("avtale_id"), rs.getInt("bruker_id"), rs.getString("tekst")));
			}
			return alarms;
		} catch (SQLException e) {
			System.out.println("Unble to get the alerts for " + user.getUserId());
			e.printStackTrace();
		}
		return alarms;
	}
    
    @Override
    public String toString()
    {
    	return Appointment.getByID(appointment_id).getDescription() + ": " + text;
    }
}
