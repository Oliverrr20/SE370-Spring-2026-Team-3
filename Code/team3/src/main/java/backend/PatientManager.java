package backend;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

class PatientManager {
    //store patients
    private Map<Integer, Patient> Patients = new HashMap<>();
    //add patients
    public void addPatient(Patient p) {
        //if statement to check if the patient exists or not
        if (Patients.containsKey(p.PatientID)) {
            //if they do then its an error
            throw new RuntimeException("Patient already exists");
        }
        Patients.put(p.PatientID, p); //add
    }
    //get patient
    public Patient getPatient(int id) {
        //if statement to check if theyu exist
        if (!Patients.containsKey(id)) {
            //error if they dont
            throw new RuntimeException("Patient not found");
        }
        return Patients.get(id);
    }
    //remove the patient
    public void removePatient(int id) {
        Patient p = getPatient(id); //get the patient
        //if statement to check if they're assigned'
        if (p.RoomID != null) {
            throw new RuntimeException("Cannot delete patient assigned to a room");
        }
        Patients.remove(id); //remove them
    }
    public Patient authenticate(String phone, String pass) {
        for (Patient p : Patients.values()) {
            if (p.Phone != null && p.Pass != null && p.Phone.equals(phone)) {
                return p;
            }
            if (BCrypt.checkpw(pass, p.Pass)) {
                return p;   // login success
            }
        }

        return null;
    }
}
