package no.ntnu.gruppe47.db.entities;

import no.ntnu.gruppe47.db.DBConnection;
import no.ntnu.gruppe47.db.RoomSQL;

public class Room {

private int room_id;
private String roomNumber;
private int capacity;

//TODO: ordne constructoren slik at den kan hente rom fra db.

public Room(String roomNumber, int capacity, DBConnection db){
	RoomSQL roomSQL = new RoomSQL(db);
	
	if (roomSQL.roomNumberExists(roomNumber)){
		Room temp = roomSQL.getRoom(roomNumber);
		this.room_id = temp.room_id;
		this.roomNumber = temp.roomNumber;
		this.capacity = temp.capacity;
	}
	else if (capacity < 1) roomSQL.addRoom(roomNumber);
	else roomSQL.addRoom(roomNumber);
}

public Room(String roomNumber, DBConnection db){
	this(roomNumber,-1,db);
}

public int getRoom_id(){
	return room_id;
}

public void setRoomNumber(String roomNumber){
	if (roomNumber == null)
		throw new IllegalArgumentException("Cannot delete a room number");
	this.roomNumber = roomNumber;
}

public String getRoomNumber(){
	return roomNumber;
}

public void setCapacity(int capacity){
	this.capacity = capacity;
}

public int getCapacity(){
	return capacity;
}

}
