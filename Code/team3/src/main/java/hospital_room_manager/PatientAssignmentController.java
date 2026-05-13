package hospital_room_manager;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

public class PatientAssignmentController {

    @FXML
    private ComboBox<GuiPatient> patientComboBox;
    @FXML
    private ComboBox<GuiRoom> roomComboBox;
    @FXML
    private Label selectedPatientLabel;
    @FXML
    private Label selectedRoomLabel;
    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        preparePatientNames();
        prepareRoomNames();
        refreshAssignmentChoices();

        patientComboBox.setOnAction(event -> updatePreview());
        roomComboBox.setOnAction(event -> updatePreview());

        updatePreview();
    }

    private void preparePatientNames() {
        patientComboBox.setConverter(new StringConverter<GuiPatient>(){
            @Override
            public String toString(GuiPatient patient){
                if (patient == null) {
                    return "";
                }

                return patient.getFullName() + " - " + HospitalGuiData.getRoomDisplayText(patient.getRoomId());
            }

            @Override
            public GuiPatient fromString(String string){
                return null;
            }
        });
    }

    private void prepareRoomNames() {
        roomComboBox.setConverter(new StringConverter<GuiRoom>() {
            @Override
            public String toString(GuiRoom room) {
                if (room == null) {
                    return "";
                }

                return "Room " + room.getRoomNumber()
                        + " - Floor " + room.getFloorNumber()
                        + " - " + room.getRoomType();
            }

            @Override
            public GuiRoom fromString(String string) {
                return null;
            }
        });
    }

    private void refreshAssignmentChoices() {
        try {
            patientComboBox.setItems(HospitalGuiData.getPatients());
            roomComboBox.setItems(HospitalGuiData.getAvailableRooms());
        } catch (RuntimeException e) {
            patientComboBox.getItems().clear();
            roomComboBox.getItems().clear();
            messageLabel.setText("Couldn't load patient or room data. Please check your database connection.");
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        GuiPatient patient = patientComboBox.getValue();
        GuiRoom room = roomComboBox.getValue();

        if (patient == null) {
            selectedPatientLabel.setText("Patient Selected: None");
        } else {
            selectedPatientLabel.setText("Patient Selected: " + patient.getFullName());
        }

        if (room == null) {
            selectedRoomLabel.setText("Room Selected: None");
        } else {
            selectedRoomLabel.setText("Room Selected: Room " + room.getRoomNumber());
        }
    }

    @FXML
    private void assignPatient() {
        GuiPatient selectedPatient = patientComboBox.getValue();
        GuiRoom selectedRoom = roomComboBox.getValue();

        if (selectedPatient == null || selectedRoom == null) {
            messageLabel.setText("Select a patient and an available room.");
            return;
        }

        try {
            HospitalGuiData.placePatientInRoom(selectedPatient, selectedRoom);

            messageLabel.setText(selectedPatient.getFullName()
                    + " has been assigned to Room "
                    + selectedRoom.getRoomNumber()
                    + ".");

            roomComboBox.setValue(null);
            patientComboBox.setValue(null);
            refreshAssignmentChoices();
            updatePreview();

        } catch (RuntimeException e) {
            messageLabel.setText("Assignment was not saved: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToDashboard() throws IOException {
        App.setRoot("room_dashboard");
    }

    @FXML
    private void goToAssignment() throws IOException {
        App.setRoot("patient_assignment");
    }

    @FXML
    private void goToPatientInfo() throws IOException {
        App.setRoot("patient_info");
    }

    @FXML private void goToAddPatient() throws IOException {
        App.setRoot("add_patient");
    }

    @FXML
    private void logout() throws IOException {
        LoginSession.logout();
        App.setRoot("login");
    }
}