package backend;

import java.util.HashMap;
import java.util.Map;

public class PatientUserJunctionManager {
    //store junction
    private Map<Integer, PatientUserJunction> PatientUserJunction = new HashMap<>();
}
public boolean canViewPatient(int userID, int patientID){
    for(PatientUserJunction j : PatientUserJunction.value()) {
        if(j.UserID == userID && j.PatientID) {
            return true;
        }
        }
    return false;
        }
