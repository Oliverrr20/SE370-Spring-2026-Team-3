package hospital_room_manager;

//A room view object used by the Gui for room labes, cards, etc. 
public class GuiRoom {

    private int roomId;
    private int roomNumber;
    private int floorNumber;
    private String roomType;
    private String roomStatus;

    //Stores a "snapshot" of the room
    public GuiRoom(int roomId, int roomNumber, int floorNumber, String roomType, String roomStatus) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(String roomStatus) {
        this.roomStatus = roomStatus;
    }

    //A text that is shown in dropdowns if JavaFX needs room label.
    @Override
    public String toString() {
        return "Room " + roomNumber + " - " + roomType + " - " + roomStatus;
    }
}