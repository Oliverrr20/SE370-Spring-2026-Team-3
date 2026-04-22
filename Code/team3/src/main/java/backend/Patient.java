package backend;

public class Patient {
    //variables
    int patientID; 
    String firstName;
    String lastName;
    String gender;
    String dob;
    String phone;
    String admissionDate;
    String leaveDate;
    Integer assignedRoomID; 
    //constructor
    public Patient(int id, String fn, String ln) {
        patientID = id; //set id
        firstName = fn; //set first name
        lastName = ln; //set ;last name
        assignedRoomID = null; //no room at the start
    }

}
