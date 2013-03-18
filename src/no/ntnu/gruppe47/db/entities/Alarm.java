package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;

public class Alarm {

    private final int alarm_id;
	private final int appointment_id;
    private final int group_id;
    private Timestamp time;

    private Alarm(int alid, int appid, int gid, Timestamp time)
    {
    	this.alarm_id = alid;
        this.appointment_id = appid;
        this.group_id = gid;
        this.time = time;
    }
    
    private Alarm(int aid, int gid, Timestamp time)
    {
    	this.alarm_id = -1;
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
    public int getAlarmId()
    {
    	return alarm_id;
    }
    
    public static Alarm create(int aid, int  uid, Timestamp time)
    {
		String sql = String.format(
						"INSERT INTO alarm (avtale_id, gruppe_id, tid) " +
						"VALUES  (%d, %d, %d);",
						aid, uid, time);

		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
			{
				Alarm alarm = new Alarm(rs.getInt("alarm_id"), rs.getInt("avtale_id"), rs.getInt("gruppe_id"), rs.getTimestamp("tid"));
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


    public static ArrayList<Alarm> getAllAlarmsForGroup(Group group){
		ArrayList<Alarm> alarms = new ArrayList<Alarm>();
		String sql = String.format(
					"SELECT avtale_id, bruker_id, tid " +
					"FROM person INNER JOIN avtale ON gruppe.gruppe_id = avtal.gruppe_id AND" +
							"gruppe.gruppe_id = %d;", group.getGroupId());
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				alarms.add(new Alarm(rs.getInt("avtale_id"), rs.getInt("gruppe_id"), rs.getTimestamp("tid")));
			}
			return alarms;
		} catch (SQLException e) {
			System.out.println("Unble to get the alarms for " + group.getGroupId());
			e.printStackTrace();
		}
		return null;
	}
        
    public static void sendAlarmToGroup(Alarm alarm, Group group)
    {
    	group.addAlarm(alarm);
    }
}
