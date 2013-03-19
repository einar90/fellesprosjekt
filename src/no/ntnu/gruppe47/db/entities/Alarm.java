package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;

public class Alarm {

	private final int appointment_id;
    private final int group_id;
    private Timestamp time;
    
    private Alarm(int aid, int gid, Timestamp time)
    {
    	this.appointment_id = aid;
    	this.group_id = gid;
    	this.time = time;
    }
    
    public int getAppointmentmId()
    {
    	return appointment_id;
    }
    
    public int getGroupId()
    {
    	return group_id;
    }
    
    public static Alarm create(int aid, int  uid, Timestamp time)
    {
		String sql = String.format(
						"INSERT INTO alarm (avtale_id, gruppe_id, tid) " +
						"VALUES  (%d, %d, %d);",
						aid, uid, time);

		try {
			Database.makeUpdate(sql);
			return new Alarm(aid, uid, time);

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
				this.time, this.appointment_id, this.group_id);

		try {
			Database.makeUpdate(sql);
			return true;

		} catch (SQLException e) {
			System.out.println("Could not update alarm");
			System.out.println(e.getMessage());
		}
		return false;
    }


    public static ArrayList<Alarm> getAllAlarmsForUser(User user){
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		String sql = String.format(
					"SELECT alarm_id, bruker_id, tid " +
					"FROM alarm as a " +
					"WHERE a.bruker_id = %d", user.getUserId());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				alarms.add(new Alarm(rs.getInt("avtale_id"), rs.getInt("gruppe_id"), rs.getTimestamp("tid")));
			}
			return alarms;
		} catch (SQLException e) {
			System.out.println("Unble to get the alarms for " + user.getUserId());
			e.printStackTrace();
		}
		return alarms;
	}
}
