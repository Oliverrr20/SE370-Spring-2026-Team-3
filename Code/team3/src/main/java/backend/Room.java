package backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//Room data object which matches the columns with the Room table. 
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int RoomID;

    private int RoomNumber;
    private int FloorNumber;
    private String RoomType;
    private String RoomStatus;
    private String LastUpdated;

    //An empty constructor, used when cases where a room is filled in. 
    public Room() {
    }

    //This constructor is used when nurses or staff add new rooms from the dashboard.
    public Room(int rn, int fn, String type){
        RoomNumber = rn;
        FloorNumber = fn;
        RoomType = type;
        RoomStatus = "Available";
    }
    //Getters and Setters which purpose is keep controllers from editing fields directly. 
    public int getRoomID(){
        return RoomID;
    }
    public void setRoomID(int roomID){
        RoomID = roomID;
    }

    public int getRoomNumber(){
        return RoomNumber;
    }
    public void setRoomNumber(int roomNumber){
        RoomNumber = roomNumber;
    }

    public int getFloorNumber(){
        return FloorNumber;
    }
    public void setFloorNumber(int floorNumber){
        FloorNumber = floorNumber;
    }

    public String getRoomType(){
        return RoomType;
    }
    public void setRoomType(String roomType){
        RoomType = roomType;
    }

    public String getRoomStatus(){
        return RoomStatus;
    }
    public void setRoomStatus(String roomStatus){
        RoomStatus = roomStatus;
    }

    public String getLastUpdated(){
        return LastUpdated;
    }
    public void setLastUpdated(String lastUpdated){
        LastUpdated = lastUpdated;
    }
}