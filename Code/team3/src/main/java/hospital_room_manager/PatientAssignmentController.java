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
        patientComboBox.setItems(HospitalGuiData.getPatients());
        roomComboBox.setItems(HospitalGuiData.getAvailableRooms());

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

        roomComboBox.setConverter(new StringConverter<GuiRoom>() {
            @Override
            public String toString(GuiRoom room) {
                if (room == null) {
                    return "";
                }

                return "Room "+ room.getRoomNumber() + " - " + room.getRoomType();
            }

            @Override
            public GuiRoom fromString(String string) {
                return null;
            }
        });

        patientComboBox.setOnAction(event -> updatePreview());
        roomComboBox.setOnAction(event -> updatePreview());

        updatePreview();
    }

    private void updatePreview() {
        GuiPatient patient = patientComboBox.getValue();
        GuiRoom room = roomComboBox.getValue();

        if (patient == null) {
            selectedPatientLabel.setText("Patient Selected: Null");
        } else {
            selectedPatientLabel.setText("Patient Selected: " + patient.getFullName());
        }

        if (room == null) {
            selectedRoomLabel.setText("Room Selected: Null");
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

        HospitalGuiData.assignPatientToRoom(selectedPatient, selectedRoom);

        messageLabel.setText(selectedPatient.getFullName()
                + " has been assigned to Room "
                + selectedRoom.getRoomNumber()
                + ".");

        roomComboBox.setItems(HospitalGuiData.getAvailableRooms());
        roomComboBox.setValue(null);

        patientComboBox.setItems(null);
        patientComboBox.setItems(HospitalGuiData.getPatients());

        updatePreview();
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
        App.setRoot("login");
    }
}