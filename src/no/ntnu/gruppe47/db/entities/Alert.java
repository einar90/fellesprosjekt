package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import no.ntnu.gruppe47.db.Database;

public class Alert {

    private final int appointment_id;
    private final int user_id;
    private String status;

    private Alert(int aid, int uid, String status) {
        this.appointment_id = aid;
        this.user_id = uid;
        this.status = status;
    }
    
    public static Alert create(int aid, int  uid, String status)
    {
		String sql = String.format(
						"INSERT INTO varsel (avtale_id, bruker_id, status) " +
						"VALUES  (%d, %d, '%s')",
						aid, uid, status);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
			{
				Alert alert = new Alert(rs.getInt("avtale_id"), rs.getInt("bruker_id"), rs.getString("status"));
				return alert;
			}

		} catch (SQLException e) {
			System.out.println("Could not add user");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    public void setStatus(String status)
    {
    	this.status = status;
    	this.update();
    }
    
    private boolean update()
    {
		String sql = String.format(
				"UPDATE alert " +
				"SET status = '%s' " +
				"WHERE avtale_id = %d AND bruker_id = %d",
				this.status, this.appointment_id, this.user_id);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update alert");
			System.out.println(e.getMessage());
		}
		return false;
    }
}
