package hospital_room_manager;

public class GuiRoom {

    private int roomId;
    private int roomNumber;
    private int floorNumber;
    private String roomType;
    private String roomStatus;

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

    @Override
    public String toString() {
        return "Room " + roomNumber + " - " + roomType + " - " + roomStatus;
    }
}