package backend;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PatientUserJunction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    int PatientUserJunctionID;

    int PatientID;
    int UserID;
    String AssignmentTime;
    String AssignmentEnd;

    public PatientUserJunction() {
    }

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

    public String getAssignmentTime() {
        return AssignmentTime;
    }

    public void setAssignmentTime(String assignmentTime) {
        AssignmentTime = assignmentTime;
    }

    public String getAssignmentEnd() {
        return AssignmentEnd;
    }

    public void setAssignmentEnd(String assignmentEnd) {
        AssignmentEnd = assignmentEnd;
    }
}