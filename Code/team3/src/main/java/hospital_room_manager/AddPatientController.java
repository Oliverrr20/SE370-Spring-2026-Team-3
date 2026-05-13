package hospital_room_manager;

import java.io.IOException;
import java.time.LocalDate;

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

    @FXML
    public void initialize() {
        genderField.getItems().addAll("Female", "Male", "Nonbinary", "Prefer Not To Say");

        firstField.textProperty().addListener((o, oldV, newV) -> clearInvalid(firstField));
        lastField.textProperty().addListener((o, oldV, newV) -> clearInvalid(lastField));
        phoneField.textProperty().addListener((o, oldV, newV) -> clearInvalid(phoneField));
        emailField.textProperty().addListener((o, oldV, newV) -> clearInvalid(emailField));
        genderField.valueProperty().addListener((o, oldV, newV) -> clearInvalid(genderField));
        dobPicker.valueProperty().addListener((o, oldV, newV) -> clearInvalid(dobPicker));
    }

    @FXML
    private void addPatient() {
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

        LocalDate dateOfBirth = dobPicker.getValue();

        if (dateOfBirth == null) {
            markInvalid(dobPicker);
            hasError = true;
        }

        String cleanedPhone = phoneField.getText().replaceAll("\\D", "");

        if (cleanedPhone.length() != 10) {
            markInvalid(phoneField);
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

        try {
            if (patientManager.authenticate(cleanedPhone)) {
                markInvalid(phoneField);
                messageLabel.setText("Phone number already registered.");
                return;
            }

            Patient newPatient = new Patient(
                    firstField.getText().trim(),
                    lastField.getText().trim(),
                    genderField.getValue(),
                    dateOfBirth.toString(),
                    cleanedPhone,
                    emailField.getText().trim()
            );

            patientManager.addPatient(newPatient);

            messageLabel.setText("Patient added successfully.");
            clearPatientForm();

        } catch (RuntimeException e) {
            messageLabel.setText("Patient was not saved: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearPatientForm() {
        firstField.clear();
        lastField.clear();
        genderField.setValue(null);
        dobPicker.setValue(null);
        phoneField.clear();
        emailField.clear();

        clearInvalid(firstField);
        clearInvalid(lastField);
        clearInvalid(genderField);
        clearInvalid(dobPicker);
        clearInvalid(phoneField);
        clearInvalid(emailField);
    }

    private void markInvalid(Control field) {
        if (!field.getStyleClass().contains("input-error")) {
            field.getStyleClass().add("input-error");
        }
    }

    private void clearInvalid(Control field) {
        field.getStyleClass().remove("input-error");
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

    @FXML
    private void goToAddPatient() throws IOException {
        App.setRoot("add_patient");
    }

    @FXML
    private void logout() throws IOException {
        LoginSession.logout();
        App.setRoot("login");
    }
}