package no.ntnu.gruppe47.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.entities.Room;

public class RoomSQL {

	private DBConnection db;
	
	public RoomSQL(DBConnection db){
		this.db = db;
	}
 
	public int addRoom(String roomNumber, int capacity){
		String insert_sql = "";
		if (capacity > 0){
			insert_sql = String.format(
					"INSERT INTO Rom (rom_id, romnummer, kapasitet)" +
					"null, %s, %d", roomNumber, capacity);
			
		}else{
			insert_sql = String.format(
					"INSERT INTO Rom (rom_id, romnummer, kapasitet)" +
					"null, '%s', null", roomNumber);
		}
		
		String rom_id_sql = String.format(
							"SELECT rom_id" +
							"FROM Rom" +
							"WHERE romnummer = '%s'",roomNumber);
		ResultSet result = null;
		try {
			db.makeUpdate(insert_sql);
			result = db.makeSingleQuery(rom_id_sql);
		} catch (SQLException e) {
			System.out.println("Unable to insert the new room.");
			e.printStackTrace();
		}
		try {
			return result.getInt("rom_id");
		} catch (SQLException e) {
			System.out.println("Unable to get the room id.");
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public int addRoom(String roomNumber){
		return addRoom(roomNumber, -1);
	}
	
	public int addRoom(Room room){
		return addRoom(room.getRoomNumber(), room.getCapacity());
	}
	
	public Room getRoom(String roomNumber){
		String sql = String.format(
				"SELCT *" +
				"FROM Rom"+
				"WHERE romnummer = '%s'", roomNumber);
		ResultSet res = null;
		try {
			res = db.makeSingleQuery(sql);
		} catch (SQLException e) {
			System.out.println("Unable to get the room");
			e.printStackTrace();
		}
		
		if (res != null){
			try {
				return new Room(res.getInt("rom_id"), res.getString("romnummer"), res.getInt("kapasitet"));
			} catch (SQLException e) {
				System.out.println("Unable to get the room info from database.");
				e.printStackTrace();
			}
		}return null;
	}
	
	public Room getRoom(int room_id){
		String sql = String.format(
				"SELCT *" +
				"FROM Rom"+
				"WHERE romnummer = '%d'", room_id);
		ResultSet res = null;
		try {
			res = db.makeSingleQuery(sql);
		} catch (SQLException e) {
			System.out.println("Unable to get the room");
			e.printStackTrace();
		}
		
		if (res != null){
			try {
				return new Room(res.getInt("rom_id"), res.getString("romnummer"), res.getInt("kapasitet"));
			} catch (SQLException e) {
				System.out.println("Unable to get the room info from database.");
				e.printStackTrace();
			}
		}
		return null;
	}

	public boolean roomNumberExists(String roomNumber) {
		String sql = String.format(
				"SELECT *" +
				"FROM Rom" +
				"WHERE romnummer = %d", roomNumber);
		ResultSet res = null;
		try {
			res = db.makeSingleQuery(sql);
		} catch (SQLException e) {
			System.out.println("Unable to get the room; there has been an SQL-error");
			e.printStackTrace();
		}
		
		try {
			if (!res.next()) return true;
		} catch (SQLException e) {
			System.out.println("There has been an error while checking if there exists a particular room.");
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Room> getAllRooms(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String sql = "SELCT rom_id, romnummer, kapasitet" +
				 "FROM Rom";
		ResultSet res = null;
		try {
			res = db.makeSingleQuery(sql);
		} catch (SQLException e) {
			System.out.println("Unable to get the rooms.");
			e.printStackTrace();
		}
		try {
			while(res.next()){
				rooms.add(new Room(res.findColumn("rom_id"), res.getString("Romnummer"), res.getInt("kapasitet")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rooms;
	}
	
	public ArrayList<Room> getRoomsAvaliable(Timestamp start, Timestamp end){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String sql = String.format(
				"SELECT rom_id, romnummer, kapasitet" +
				"FROM Avtale INNER JOIN Rom ON Avtale.rom_id = Rom.rom_id" +
				"WHERE start >= %d AND slutt <= %d", start, end);
		
		ResultSet res = null;
		
		try {
			res = db.makeSingleQuery(sql);
			while (res.next()){
				rooms.add( new Room(res.getInt("rom_id"),res.getString("romnummer"), res.getInt("kapasitet")));
			}
		} catch (SQLException e) {
			System.out.println("Unable to get the available rooms for the spesified timeinterval");
			e.printStackTrace();
		}
		
		return rooms;
		
	}

}
