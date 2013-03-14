package no.ntnu.gruppe47.db.entities;

public class Room {

    private int room_id;
    private String roomNumber;
    private int capacity;

    public Room(String roomNumber, int capacity) {
        this(roomNumber);
        this.capacity = capacity;
    }

    public Room(String roomNumber) {
        this.roomNumber = roomNumber;
        // TODO: Sync with database
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoomNumber(String roomNumber) {
        if (roomNumber == null)
            throw new IllegalArgumentException("Cannot delete a room number");
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
