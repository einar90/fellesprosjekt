package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import no.ntnu.gruppe47.db.Database;

public class Invitation {

    private final int appointment_id;
    private final int user_id;
    private Boolean response;

    private Invitation(int aid, int uid, Boolean response) {
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
				Invitation alert = new Invitation(rs.getInt("avtale_id"), rs.getInt("bruker_id"), null);
				return alert;
			}

		} catch (SQLException e) {
			System.out.println("Could not add invitation");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    public void accept()
    {
    	this.response = true;
    	this.update();
    }
    
    public void reject()
    {
    	this.response = false;
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
}
