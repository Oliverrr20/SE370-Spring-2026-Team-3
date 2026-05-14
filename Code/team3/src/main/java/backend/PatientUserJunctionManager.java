package backend;

import java.util.HashMap;
import java.util.Map;

//Database helper (Works as the PatientUserJunction table)
public class PatientUserJunctionManager {

    private Map<Integer, PatientUserJunction> patientUserJunctions = new HashMap<>();

    //Main purpose is to save the link between a staff and patient in the junction table.
    public void addAssignment(PatientUserJunction assignment) {
        if (assignment == null) {
            throw new RuntimeException("Assignment cannot be null");
        }

        patientUserJunctions.put(assignment.getPatientUserJunctionID(), assignment);
    }

    //a placeholder just in case the app needs it 
    public PatientUserJunction getAssignment(int id) {
        if (!patientUserJunctions.containsKey(id)) {
            throw new RuntimeException("Assignment not found");
        }

        return patientUserJunctions.get(id);
    }

    public void removeAssignment(int id) {
        patientUserJunctions.remove(id);
    }
}