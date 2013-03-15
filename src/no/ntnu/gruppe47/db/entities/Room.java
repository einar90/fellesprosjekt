package no.ntnu.gruppe47.db.entities;

public class Room {

	private final int roomId;
	private String roomNumber;
	private int capacity;

	// TODO: ordne constructoren slik at den kan hente rom fra db.

	public Room(int roomId, String roomNumber, int capacity) {
		if (capacity < 1)
			this.capacity = 0;
		else
			this.capacity = capacity;

		this.roomId = roomId;
		this.roomNumber = roomNumber;

	}

	public Room(int roomId, String roomNumber) {
		this(roomId, roomNumber, -1);
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomNumber(String roomNumber) {
		if (roomNumber == null)
			System.out.println("Cannot delete a room number");
		// throw new IllegalArgumentException("Cannot delete a room number");
		// Evt bare skrive til skj¾rm og ikke gj¿re noe ellers
		else
			this.roomNumber = roomNumber;
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

}
