package hospital_room_manager;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class PatientInfoController {

    @FXML
    private TextField searchField;
    @FXML
    private TableView<GuiPatient> patientTable;
    @FXML
    private TableColumn<GuiPatient, String> nameColumn;
    @FXML
    private TableColumn<GuiPatient, String> genderColumn;
    @FXML
    private TableColumn<GuiPatient,String> dobColumn;
    @FXML
    private TableColumn<GuiPatient, String> phoneColumn;
    @FXML
    private TableColumn<GuiPatient, String> emailColumn;
    @FXML
    private TableColumn<GuiPatient, String> admissionColumn;
    @FXML
    private TableColumn<GuiPatient, String> roomColumn;
    @FXML
    private Label selectedPatientLabel;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFullName()));

        genderColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getGender()));
        dobColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDateOfBirth()));
        phoneColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getPhone()));

        emailColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEmail()));

        admissionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getAdmissionDate()));

        roomColumn.setCellValueFactory(data ->
                new SimpleStringProperty(HospitalGuiData.getRoomDisplayText(data.getValue().getRoomId())));

        patientTable.setItems(HospitalGuiData.getPatients());

        patientTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedPatient) -> {
            if (selectedPatient == null) {
                selectedPatientLabel.setText("Patient Selected: Null");
            } else {
                selectedPatientLabel.setText(
                        "Patient Selected: " + selectedPatient.getFullName()
                                + " | Phone: " + selectedPatient.getPhone()
                                + " | Email: " + selectedPatient.getEmail()
                );
            }
        });
    }

    @FXML
    private void searchPatients() {
        String searchText = searchField.getText();

        if (searchText == null || searchText.isBlank()){
            patientTable.setItems(HospitalGuiData.getPatients());
            return;
        }

        String lowerSearch = searchText.toLowerCase();
        ObservableList<GuiPatient> filteredPatients = FXCollections.observableArrayList();

        for (GuiPatient patient : HospitalGuiData.getPatients()){
            if (patient.getFullName().toLowerCase().contains(lowerSearch)
                    || patient.getGender().toLowerCase().contains(lowerSearch)
                    || patient.getDateOfBirth().toLowerCase().contains(lowerSearch)
                    || patient.getPhone().toLowerCase().contains(lowerSearch)
                    || patient.getEmail().toLowerCase().contains(lowerSearch)
                    || HospitalGuiData.getRoomDisplayText(patient.getRoomId()).toLowerCase().contains(lowerSearch)) {
                filteredPatients.add(patient);
            }
        }

        patientTable.setItems(filteredPatients);
    }

    @FXML
    private void clearSearch(){
        searchField.clear();
        patientTable.setItems(HospitalGuiData.getPatients());
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
}