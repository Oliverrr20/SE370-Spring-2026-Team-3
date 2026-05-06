package hospital_room_manager;

import java.io.IOException;

import backend.Client;
import backend.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class NewAccountController {
    @FXML private TextField firstField;
    @FXML private TextField lastField;
    @FXML private TextField roleField;
    @FXML private TextField dobField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private ClientManager clientManager = new ClientManager();

    @FXML
    private void accountCreated() throws IOException {
        if (firstField.getText().isBlank() ||
            lastField.getText().isBlank() ||
            roleField.getText().isBlank() ||
            dobField.getText().isBlank() ||
            phoneField.getText().isBlank() ||
            emailField.getText().isBlank() ||
            passwordField.getText().isBlank()) {

            messageLabel.setText("All fields are required.");
            return;
        }

        // 2. Check if phone already exists
        if (clientManager.checkPhone(phoneField.getText())) {
            messageLabel.setText("Phone number already registered.");
            return;
        }

        // 3. Create Client object
        Client newClient = new Client(
            firstField.getText(),
            lastField.getText(),
            roleField.getText(),
            dobField.getText(),
            phoneField.getText(),
            emailField.getText(),
            passwordField.getText()
        );

        // 4. Save to database
        clientManager.addClient(newClient);

        // 5. Redirect to login
        App.setRoot("login");
    }
}