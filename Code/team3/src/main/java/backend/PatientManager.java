package backend;

import java.util.HashMap;
import java.util.Map;

class PatientManager {
    //store patients
    private Map<Integer, Patient> patients = new HashMap<>();
    //add patients
    public void addPatient(Patient p) {
        //if statement to check if the patient exists or not
        if (patients.containsKey(p.patientID)) {
            //if they do then its an error
            throw new RuntimeException("Patient already exists");
        }
        patients.put(p.patientID, p); //add
    }
    //get patient
    public Patient getPatient(int id) {
        //if statement to check if theyu exist
        if (!patients.containsKey(id)) {
            //error if they dont
            throw new RuntimeException("Patient not found");
        }
        return patients.get(id);
    }
    //remove the patient
    public void removePatient(int id) {
        Patient p = getPatient(id); //get the patient
        //if statement to check if they're assigned'
        if (p.assignedRoomID != null) {
            throw new RuntimeException("Cannot delete patient assigned to a room");
        }
        patients.remove(id); //remove them
    }
}
