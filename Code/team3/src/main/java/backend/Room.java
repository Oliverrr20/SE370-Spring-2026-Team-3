package backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
class Room {
    //variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int RoomID; //roomID
    int RoomNumber; //roomnum
    int FloorNumber;
    String RoomType; //room type
    String RoomStatus; //occupied or available or closed
    String LastUpdated;
    //empty constructor
    public Room (){}
    //construtor with variables
    public Room(int rn, int fn, String type) {
        RoomNumber = rn; //set the num
        FloorNumber = fn;
        RoomType = type; //set type
        RoomStatus = "Available"; //at the start the rooms available
    }
    public int getRoomID() {
        return RoomID;
    }
    public void setRoomID(int roomID) {
        this.RoomID = roomID;
    }
}