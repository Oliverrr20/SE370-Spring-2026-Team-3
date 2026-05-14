package hospital_room_manager;

import java.io.IOException;

import backend.Patient;
import backend.PatientManager;
import backend.Room;
import backend.RoomManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

//Controller for the patient info screen and patient search.
public class PatientInfoController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<Patient> patientTable;
    @FXML
    private TableColumn<Patient, String> nameColumn;
    @FXML
    private TableColumn<Patient, String> genderColumn;
    @FXML
    private TableColumn<Patient,String> dobColumn;
    @FXML
    private TableColumn<Patient, String> phoneColumn;
    @FXML
    private TableColumn<Patient, String> emailColumn;
    @FXML
    private TableColumn<Patient, String> admissionColumn;
    @FXML
    private TableColumn<Patient, String> roomColumn;
    @FXML
    private Label selectedPatientLabel;

    private final RoomManager roomManager = new RoomManager();
    private final PatientManager patientManager = new PatientManager();

    @FXML
    //Sets up the patient table and loads the current patient list.
    private void initialize() {
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFirstName() + " " + data.getValue().getLastName()));

        genderColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGender()));
        dobColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDOB()));
        phoneColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPhone()));

        emailColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail()));

        admissionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAdmissionDate()));

        roomColumn.setCellValueFactory(data ->
                new SimpleStringProperty(formatRoom(data.getValue().getRoomID())));

        ObservableList<Patient> patients =
                FXCollections.observableArrayList(patientManager.getAllPatients());
        patientTable.setItems(patients);

        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedPatient) -> {
            if (selectedPatient == null) {
                selectedPatientLabel.setText("Patient Selected: Null");
            } else {
                selectedPatientLabel.setText(
                        "Patient Selected: " + selectedPatient.getFirstName() + " " + selectedPatient.getLastName()
                                + " | Phone: " + selectedPatient.getPhone()
                                + " | Email: " + selectedPatient.getEmail()
                );
            }
        });
    }

    @FXML
    //It function is to filter the table by using the search box
    private void searchPatients() {
        String searchText = searchField.getText();

        if (searchText == null || searchText.isBlank()){
            ObservableList<Patient> patients =
                    FXCollections.observableArrayList(patientManager.getAllPatients());
            patientTable.setItems(patients);
            return;
        }

        String lowerSearch = searchText.toLowerCase();
        ObservableList<Patient> filteredPatients = FXCollections.observableArrayList();

        for (Patient patient : patientManager.getAllPatients()){
            if (patient.getFirstName().toLowerCase().contains(lowerSearch)
                    || patient.getLastName().toLowerCase().contains(lowerSearch)
                    || patient.getGender().toLowerCase().contains(lowerSearch)
                    || patient.getDOB().toLowerCase().contains(lowerSearch)
                    || patient.getPhone().toLowerCase().contains(lowerSearch)
                    || patient.getEmail().toLowerCase().contains(lowerSearch)
                    || formatRoom(patient.getRoomID()).toLowerCase().contains(lowerSearch)) {
                filteredPatients.add(patient);
            }
        }

        patientTable.setItems(filteredPatients);
    }

    @FXML
    //Clears the search box, showing all patients again
    private void clearSearch(){
        searchField.clear();
        ObservableList<Patient> patients =
                    FXCollections.observableArrayList(patientManager.getAllPatients());
            patientTable.setItems(patients);
        selectedPatientLabel.setText("Patient Selected: Null");
    }

    @FXML
    private void goToDashboard() throws IOException{
        App.setRoot("room_dashboard");
    }

    @FXML
    private void goToAssignment() throws IOException{
        App.setRoot("patient_assignment");
    }

    @FXML
    private void goToPatientInfo() throws IOException{
        App.setRoot("patient_info");
    }

    @FXML private void goToAddPatient() throws IOException {
        App.setRoot("add_patient");
    }

    @FXML
    private void logout() throws IOException{
        App.setRoot("login");
    }
    //helper for room format
    private String formatRoom(Integer roomId) {
    if (roomId == null) {
        return "Unassigned";
    }

    try {
        Room room = roomManager.getRoom(roomId);
        return "Floor " + room.getFloorNumber() + " - Room " + room.getRoomNumber();
    } catch (Exception e) {
        return "Unknown Room";
    }
}
}