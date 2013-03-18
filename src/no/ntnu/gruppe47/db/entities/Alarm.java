package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import no.ntnu.gruppe47.db.Database;

public class Alarm {

    private final int appointment_id;
    private final int user_id;
    private Timestamp time;

    private Alarm(int aid, int uid, Timestamp time) {
        this.appointment_id = aid;
        this.user_id = uid;
        this.time = time;
    }
    
    public static Alarm create(int aid, int  uid, Timestamp time)
    {
		String sql = String.format(
				"INSERT INTO alarm (avtale_id, bruker_id, tid) " +
						"VALUES  (%d, %d, '%s')",
						aid, uid, time);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
			{
				Alarm alarm = new Alarm(rs.getInt("avtale_id"), rs.getInt("bruker_id"), rs.getTimestamp("tid"));
				return alarm;
			}

		} catch (SQLException e) {
			System.out.println("Could not add user");
			System.out.println(e.getMessage());
		}
		return null;
    }
    
    public void setTime(Timestamp time)
    {
    	this.time = time;
    	this.update();
    }
    
    private boolean update()
    {
		String sql = String.format(
				"UPDATE alarm " +
				"SET tid = '%s' " +
				"WHERE avtale_id = %d AND bruker_id = %d",
				this.time, this.appointment_id, this.user_id);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update alarm");
			System.out.println(e.getMessage());
		}
		return false;
    }
}
