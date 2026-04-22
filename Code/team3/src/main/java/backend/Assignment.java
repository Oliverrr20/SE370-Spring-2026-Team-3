package backend;

public class Assignment {
     //variables
    int assignmentID;
    int patientID;
    int roomID;
    int userID;
    String startTime;
    String endTime;

    //constructor
    public Assignment(int aID, int pID, int rID, int uID, String time) {
        assignmentID = aID; //set id
        patientID = pID; //set patientid
        roomID = rID; //set roomid
        userID = uID; //set user id
        startTime = time; //set start time
        endTime = null; //no end time if havent ended
    }
}
