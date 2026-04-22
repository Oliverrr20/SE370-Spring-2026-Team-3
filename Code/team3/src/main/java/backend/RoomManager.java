package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager  {
    //storeRooms
    private Map<Integer, Room> rooms = new HashMap<>(); 
    //add a room
    public void addRoom(Room r) {
        //if statement to check if the room exists or not
        if (rooms.containsKey(r.roomID)) {
            //error if it does
            throw new RuntimeException("Room already exists");
        }
        rooms.put(r.roomID, r);//add the room
    }
    //get room
    public Room getRoom(int id) {
        //if statement to check if it exists
        if (!rooms.containsKey(id)) {
            //if it doesnt then error
            throw new RuntimeException("Room not found");
        }
        return rooms.get(id);
    }
    //update the room status
    public void updateRoomStatus(int id, String status) {
        //get room
        Room r = getRoom(id);
        r.status = status; //change the status
    }
    //get all the rooms
    public List<Room> getAllRooms() {
        return new ArrayList<>(rooms.values());
    }
    //remove room
    public void removeRoom(int id) {
        Room r = getRoom(id);//get room
        //if statement to check if occupied or not 
        if (r.status.equals("Occupied")) {
            //cant remove an occupied room
            throw new RuntimeException("Cannot delete occupied room");
        }
        rooms.remove(id); //remove the room
    }

}
