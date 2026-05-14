package hospital_room_manager;

import java.io.IOException;

import backend.Patient;
import backend.PatientManager;
import backend.Room;
import backend.RoomManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

//Controller for assigning patients to available rooms
public class PatientAssignmentController {

    @FXML private ComboBox<Patient> patientComboBox;
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private Label selectedPatientLabel;
    @FXML private Label selectedRoomLabel;
    @FXML private Label messageLabel;

    private final PatientManager patientManager = new PatientManager();
    private final RoomManager roomManager = new RoomManager();

    @FXML

    //Set up dropdown labels, also refreshes the assignment choices.
    private void initialize() {
        setupPatientConverter();
        setupRoomConverter();

        refreshAssignmentChoices();

        patientComboBox.setOnAction(event -> updatePreview());
        roomComboBox.setOnAction(event -> updatePreview());

        updatePreview();
    }

    private void setupPatientConverter() {
        patientComboBox.setConverter(new StringConverter<Patient>() {
            @Override
            public String toString(Patient p) {
                if (p == null) return "";
                return p.getFirstName() + " " + p.getLastName() +
                        " (" + formatRoom(p.getRoomID()) + ")";
            }

            @Override
            public Patient fromString(String s) { return null; }
        });
    }

    private void setupRoomConverter() {
        roomComboBox.setConverter(new StringConverter<Room>() {
            @Override
            public String toString(Room r) {
                if (r == null) return "";
                return "Floor " + r.getFloorNumber() +
                        " – Room " + r.getRoomNumber() +
                        " (" + r.getRoomType() + ")";
            }

            @Override
            public Room fromString(String s) { return null; }
        });
    }

    //Reloads patients and available rooms from database
    private void refreshAssignmentChoices() {
        ObservableList<Patient> unassignedPatients = FXCollections.observableArrayList();
        for (Patient p : patientManager.getAllPatients()) {
            if (p.getRoomID() == null) {
                unassignedPatients.add(p);
            }
        }

        ObservableList<Room> availableRooms =
                FXCollections.observableArrayList(roomManager.getAvailableRooms());

        patientComboBox.setItems(unassignedPatients);
        roomComboBox.setItems(availableRooms);
    }

    //Updates the message depending on the patient or room status
    private void updatePreview() {
        Patient p = patientComboBox.getValue();
        Room r = roomComboBox.getValue();

        selectedPatientLabel.setText(
                p == null ? "Patient Selected: None"
                        : "Patient Selected: " + p.getFirstName() + " " + p.getLastName()
        );

        selectedRoomLabel.setText(
                r == null ? "Room Selected: None"
                        : "Room Selected: Floor " + r.getFloorNumber() + " – Room " + r.getRoomNumber()
        );
    }

    @FXML
    //Saves the assignment (After both dropdowns have a selection)
    private void assignPatient() {
        Patient p = patientComboBox.getValue();
        Room r = roomComboBox.getValue();

        if (p == null || r == null) {
            messageLabel.setText("Select a patient and an available room.");
            return;
        }

        try {
            // Assign patient
            patientManager.assignPatientToRoom(p.getPatientID(), r.getRoomID());

            // Mark room as occupied
            roomManager.updateRoomStatus(r.getRoomID(), "Occupied");

            messageLabel.setText(
                    p.getFirstName() + " " + p.getLastName() +
                            " has been assigned to Floor " + r.getFloorNumber() +
                            " – Room " + r.getRoomNumber()
            );

            refreshAssignmentChoices();
            updatePreview();

        } catch (RuntimeException e) {
            messageLabel.setText("Assignment failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String formatRoom(Integer roomId) {
        if (roomId == null) return "Unassigned";
        try {
            Room r = roomManager.getRoom(roomId);
            return "Floor " + r.getFloorNumber() + " – Room " + r.getRoomNumber();
        } catch (Exception e) {
            return "Unknown Room";
        }
    }

    @FXML private void goToDashboard() throws IOException { App.setRoot("room_dashboard"); }
    @FXML private void goToAssignment() throws IOException { App.setRoot("patient_assignment"); }
    @FXML private void goToPatientInfo() throws IOException { App.setRoot("patient_info"); }
    @FXML private void goToAddPatient() throws IOException { App.setRoot("add_patient"); }
    @FXML private void logout() throws IOException { App.setRoot("login"); }
}
