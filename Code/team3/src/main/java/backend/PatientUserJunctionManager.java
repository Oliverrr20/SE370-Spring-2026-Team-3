package backend;

import java.util.HashMap;
import java.util.Map;

public class PatientUserJunctionManager {

    private Map<Integer, PatientUserJunction> patientUserJunctions = new HashMap<>();

    public void addAssignment(PatientUserJunction assignment) {
        if (assignment == null) {
            throw new RuntimeException("Assignment cannot be null");
        }

        patientUserJunctions.put(assignment.getPatientUserJunctionID(), assignment);
    }

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