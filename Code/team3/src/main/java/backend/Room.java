package backend;

class Room {
    //variables
    int roomID; //roomID
    int roomNumber; //roomnum
    String type; //room type
    String status; //occupied or available

    //construtor
    public Room(int id, int number, String type) {
        roomID = id; //set the id
        roomNumber = number; //set the num
        type = this.type; //set type
        status = "Available"; //at the start the rooms available
    }
}