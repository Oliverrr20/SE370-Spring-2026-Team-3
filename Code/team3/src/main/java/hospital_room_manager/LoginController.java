package hospital_room_manager;

import java.io.IOException;

import backend.Client;
import backend.ClientManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField phoneField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label messageLabel;

    private final ClientManager clientManager = new ClientManager();

    @FXML
    private void handleLogin() throws IOException {
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (phone == null || phone.isBlank() || password == null || password.isBlank()) {
            messageLabel.setText("Enter phone number and password.");
            return;
        }

        try {
            Client client = clientManager.authenticate(phone.trim(), password);

            if (client == null) {
                messageLabel.setText("Invalid phone number or password.");
                return;
            }

            LoginSession.setCurrentClient(client);
            App.setRoot("room_dashboard");

        } catch (RuntimeException e) {
            messageLabel.setText("Database error. Check MySQL connection.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewAccount() throws IOException {
        App.setRoot("new_account");
    }
}