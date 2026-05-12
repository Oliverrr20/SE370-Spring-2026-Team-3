package hospital_room_manager;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backend.Client;
import backend.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class NewAccountController {
    @FXML private TextField firstField;
    @FXML private TextField lastField;
    @FXML private ComboBox<String> roleBox;
    @FXML private DatePicker dobPicker;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    private ClientManager clientManager = new ClientManager();

    @FXML public void initialize() {
        roleBox.getItems().addAll("Admin", "Nurse", "Doctor", "Staff");

        firstField.textProperty().addListener((o, oldV, newV) -> clearInvalid(firstField));
        lastField.textProperty().addListener((o, oldV, newV) -> clearInvalid(lastField));
        phoneField.textProperty().addListener((o, oldV, newV) -> clearInvalid(phoneField));
        emailField.textProperty().addListener((o, oldV, newV) -> clearInvalid(emailField));
        passwordField.textProperty().addListener((o, oldV, newV) -> clearInvalid(passwordField));

        roleBox.valueProperty().addListener((o, oldV, newV) -> clearInvalid(roleBox));

        dobPicker.valueProperty().addListener((o, oldV, newV) -> clearInvalid(dobPicker));
    }

    @FXML private void accountCreated() throws IOException {
        boolean hasError = false;
        boolean invalidPass = false;

        messageLabel.setText("");

        if (firstField.getText().isBlank()) {
            markInvalid(firstField);
            hasError = true;
        }

        if (lastField.getText().isBlank()) {
            markInvalid(lastField);
            hasError = true;
        }

        if (roleBox.getValue() == null) {
            markInvalid(roleBox);
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

        if (passwordField.getText().isBlank()) {
            markInvalid(passwordField);
            hasError = true;
        } else{
            Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
            Matcher matcher = pattern.matcher(passwordField.getText());
            if (!(matcher.find())) {
                markInvalid(passwordField);
                invalidPass = true;
                hasError = true;
            }
        }

        if (hasError) {
            messageLabel.setText("Please correct the highlighted fields.");
            if(invalidPass){
                messageLabel.setText("Password must contain 8 characters, one uppercase letter, one lowercase letter, one number and one special character.");
            }
            return;
        }

        if (clientManager.checkPhone(cleanedPhone)) {
            markInvalid(phoneField);
            messageLabel.setText("Phone number already registered.");
            return;
        }

        Client newClient = new Client(
            firstField.getText(),
            lastField.getText(),
            roleBox.getValue(),
            dobPicker.getValue().toString(),
            cleanedPhone,
            emailField.getText(),
            passwordField.getText()
        );

        clientManager.addClient(newClient);
        App.setRoot("login");
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
}