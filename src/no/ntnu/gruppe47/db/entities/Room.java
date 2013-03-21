package no.ntnu.gruppe47.db.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import no.ntnu.gruppe47.db.Database;

public class Room {

	private final int roomId;
	private String roomNumber;
	private int capacity;

	/**
	 * Creates a new room object. With all the parameters
	 * NB! At this point there is NO synchronization with the database
	 * 
	 * @param roomID		A unique id for that particular room
	 * @param roomNumber	The name of the room 
	 * @param capasity		The number of people that can be in this room at any given time.
	 * 
	 */
	private Room(int roomId, String roomNumber, int capacity) {
		if (capacity < 1)
			this.capacity = 0;
		else
			this.capacity = capacity;
		this.roomId = roomId;
		this.roomNumber = roomNumber;

	}
	
	public int getRoomId() {
		return roomId;
	}

	public void setRoomNumber(String roomNumber) {
		if (roomNumber != null) this.roomNumber = roomNumber;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getCapacity() {
		return capacity;
	}

	// Statiske metoder for samhandling med databasen.

	public static Room create(String name){
		return create(name, -1);
	}
	
	public static Room create(String name, int capacity){
		if (name == null) return null;
		String sql = "";
		
		if (capacity < 0){
			sql = String.format(
					"INSERT INTO rom (romnummer, kapasitet) " +
				    "VALUES ('%s', null);", 
				  		name);
		}else{
			sql = String.format(
				"INSERT INTO rom (romnummer, kapasitet) " +
				"VALUES  ('%s', %d);",
					name, capacity);
		}
		try {
			Database.makeUpdate(sql);
			ResultSet rs = Database.makeSingleQuery("SELECT LAST_INSERT_ID() AS id");
			if (rs.first())
				return new Room(rs.getInt("id"), name, capacity);

		} catch (SQLException e) {
			System.out.println("Could not add room");
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static Room getByID(int roomId){
		String sql = String.format(
				"SELECT * " +
				"FROM rom "+
				"WHERE rom_id = %d;", roomId);
		try {
			ResultSet res = Database.makeSingleQuery(sql);
			if (res.next())
				return new Room(res.getInt("rom_id"), res.getString("romnummer"), res.getInt("kapasitet"));
		} catch (SQLException e) {
			System.out.println("Unable to get the room");
			e.printStackTrace();
		}

		return null;
	}
	
	public static Room getByName(String name){
		String sql = String.format(
						"SELECT * " +
						"FROM rom " +
						"WHERE romnummer = '%s';", name);
		try {
			ResultSet res = Database.makeSingleQuery(sql);
			if (res.next())
				return new Room(res.getInt("rom_id"), res.getString("romnummer"), res.getInt("kapasitet"));
		} catch (SQLException e) {
			System.out.println("Unable to get the room");
			e.printStackTrace();
		}
		return null;
	}
	
	public static ArrayList<Room> getAll(){
		ArrayList<Room> rooms = new ArrayList<Room>();
		String sql = "SELECT * " +
					 "FROM rom;";
		try {
			ResultSet res = Database.makeSingleQuery(sql);
			while(res.next()){
				rooms.add(new Room(res.getInt("rom_id"), res.getString("romnummer"), res.getInt("kapasitet")));
			}
		} catch (SQLException e) {
			System.out.println("Unable to get the rooms.");
			e.printStackTrace();
		}
		return rooms;
	}
	
	public static ArrayList<Room> getAvailableRooms(Timestamp start, Timestamp end, int cap)
	{
		ArrayList<Room> rooms = new ArrayList<Room>();
		String innerSQL = String.format(
				"SELECT rom_id " +
				"FROM avtale " +
				"WHERE NOT (slutt <= '%s' OR start >= '%s')"
				,start, end);
		
		String sql = String.format(
				"SELECT DISTINCT(rom_id), romnummer, kapasitet " +
				"FROM rom " +
				"WHERE rom_id NOT IN (%s) " +
				"AND kapasitet >= %d",
				innerSQL, cap);
		
		try {
			ResultSet rs = Database.makeSingleQuery(sql);
			while (rs.next()){
				rooms.add(new Room(rs.getInt("rom_id"), rs.getString("romnummer"), rs.getInt("kapasitet")));
			}
			if (rooms.size() > 0)
				return rooms;//.get(0);
		} catch (SQLException e) {
			System.out.println("Unable to get available rooms between " + start + " and " + end);
			e.printStackTrace();
		}
		return null;
	}
	
	public static Room getAvailableRoom(Timestamp start, Timestamp end, int cap)
	{
		return getAvailableRooms(start, end, cap).get(0);
	}
	
	@Override
	public boolean equals(Object other){
		if (this.getRoomId() == ((Room) other).getRoomId() && 
			this.getRoomNumber().equals(((Room) other).getRoomNumber()) &&
			this.getCapacity() == ((Room) other).getCapacity()){
				return true;
		}else return false;
	}

}