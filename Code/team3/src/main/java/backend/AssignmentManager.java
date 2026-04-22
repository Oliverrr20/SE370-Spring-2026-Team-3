package backend;

import java.util.HashMap;
import java.util.Map;

public class AssignmentManager  {
    private Map<Integer, Assignment> assignments = new HashMap<>();
    private int nextAssignmentID = 1;

    private PatientManager patientManager;
    private RoomManager roomManager;

    public AssignmentManager(PatientManager pm, RoomManager rm) {
        patientManager = pm;
        roomManager = rm;
    }
}
