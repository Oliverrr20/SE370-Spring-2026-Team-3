package backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
class PatientUserJunction {
    //variables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int PatientUserJunctionID; //roomID
    Long PatientID; //roomnum
    Long UserID;
    String AssignmentTime; //room type
    String AssignmentEnd; //occupied or available or close
    //empty constructor
    public PatientUserJunction (){}
    //construtor with variables
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
}