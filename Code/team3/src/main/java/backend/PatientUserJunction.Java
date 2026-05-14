package backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//This is a data object which purpose is to link a patient record to the staff.
@Entity
public class PatientUserJunction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int PatientUserJunctionID;

    int PatientID;
    int UserID;
    String AssignmentTime;
    String AssignmentEnd;

    //An empty contructor for object creation
    public PatientUserJunction() {
    }

    
    //It is a constructor that is used in case a patient wants to get connected to a staff member.
    public PatientUserJunction(int pi, int ui) {
        PatientID = pi;
        UserID = ui;
    }

    public int getPatientUserJunctionID() {
        return PatientUserJunctionID;
    }

    public void setPatientUserJunctionID(int pujID) {
        PatientUserJunctionID = pujID;
    }

    public int getPatientID() {
        return PatientID;
    }

    public int getUserID() {
        return UserID;
    }
}