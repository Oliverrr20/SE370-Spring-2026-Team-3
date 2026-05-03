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

    public Map<Integer, PatientUserJunction> getAllAssignments() {
        return patientUserJunctions;
    }

    public PatientUserJunction findByPatientID(int patientID) {
        for (PatientUserJunction a : patientUserJunctions.values()) {
            if (a.getPatientID() == patientID) {
                return a;
            }
        }
        return null;
    }

    public PatientUserJunction findByUserID(int userID) {
        for (PatientUserJunction a : patientUserJunctions.values()) {
            if (a.getUserID() == userID) {
                return a;
            }
        }
        return null;
    }

    public void updateAssignment(int id, PatientUserJunction updatedAssignment) {
        if (!patientUserJunctions.containsKey(id)) {
            throw new RuntimeException("Assignment not found");
        }

        patientUserJunctions.put(id, updatedAssignment);
    }

    public void updateAssignmentTime(int id, String time) {
        PatientUserJunction a = getAssignment(id);
        a.setAssignmentTime(time);
    }

    public void updateAssignmentEnd(int id, String end) {
        PatientUserJunction a = getAssignment(id);
        a.setAssignmentEnd(end);
    }

    public void removeAssignment(int id) {
        if (!patientUserJunctions.containsKey(id)) {
            throw new RuntimeException("Assignment not found");
        }

        patientUserJunctions.remove(id);
    }
}