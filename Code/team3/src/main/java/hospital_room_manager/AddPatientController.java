package hospital_room_manager;

import java.io.IOException;

import backend.Patient;
import backend.PatientManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


public class AddPatientController {
    @FXML private TextField firstField;
    @FXML private TextField lastField;
    @FXML private ComboBox<String> genderField;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private Label messageLabel;
    private PatientManager patientManager = new PatientManager();

    @FXML public void initialize() {
        genderField.getItems().addAll("Female", "Male", "Nonbinary", "Prefer Not To Say");

        firstField.textProperty().addListener((o, oldV, newV) -> clearInvalid(firstField));
        lastField.textProperty().addListener((o, oldV, newV) -> clearInvalid(lastField));
        phoneField.textProperty().addListener((o, oldV, newV) -> clearInvalid(phoneField));
        emailField.textProperty().addListener((o, oldV, newV) -> clearInvalid(emailField));

        genderField.valueProperty().addListener((o, oldV, newV) -> clearInvalid(genderField));

        dobPicker.valueProperty().addListener((o, oldV, newV) -> clearInvalid(dobPicker));
    }

    @FXML private void addPatient() throws IOException {
        boolean hasError = false;

        messageLabel.setText("");

        if (firstField.getText().isBlank()) {
            markInvalid(firstField);
            hasError = true;
        }

        if (lastField.getText().isBlank()) {
            markInvalid(lastField);
            hasError = true;
        }

        if (genderField.getValue() == null) {
            markInvalid(genderField);
            hasError = true;
        }

        if (dobPicker.getValue() == null) {
            markInvalid(dobPicker);
            hasError = true;
        }

        String cleanedPhone = phoneField.getText().replaceAll("\\D", "");
        if (cleanedPhone.length() != 10) {
            markInvalid(phoneField);
            messageLabel.setText("Phone number must contain 10 digits.");
            hasError = true;
        }

        if (emailField.getText().isBlank()) {
            markInvalid(emailField);
            hasError = true;
        }

        if (hasError) {
            messageLabel.setText("Please correct the highlighted fields.");
            return;
        }

        if (patientManager.authenticate(cleanedPhone)) {
            markInvalid(phoneField);
            messageLabel.setText("Phone number already registered.");
            return;
        }

        Patient newPatient = new Patient(
            firstField.getText(),
            lastField.getText(),
            genderField.getValue(),
            dobPicker.getValue().toString(),
            cleanedPhone,
            emailField.getText()
        );

        patientManager.addPatient(newPatient);
        App.setRoot("add_patient");
    }
    //for invalid field logic
    private void markInvalid(Control field) {
        if (!field.getStyleClass().contains("input-error")) {
            field.getStyleClass().add("input-error");
        }
    }
    private void clearInvalid(Control field) {
        field.getStyleClass().remove("input-error");
    }
    @FXML private void goToDashboard() throws IOException {
        App.setRoot("room_dashboard");
    }

    @FXML private void goToAssignment() throws IOException {
        App.setRoot("patient_assignment");
    }

    @FXML private void goToPatientInfo() throws IOException {
        App.setRoot("patient_info");
    }

    @FXML private void goToAddPatient() throws IOException {
        App.setRoot("add_patient");
    }
    
    @FXML private void logout() throws IOException {
        App.setRoot("login");
    }
}
