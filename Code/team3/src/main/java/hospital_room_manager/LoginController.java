package hospital_room_manager;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

//import backend.Client;
//import backend.ClientManager;

public class LoginController{

    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label messageLabel;
    
    //private ClientManager clientRepo;

    @FXML
    private void handleLogin() throws IOException{
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if(phone == null || phone.isBlank() || password == null || password.isBlank()){
            messageLabel.setText("Enter password and phone-number.");
            return;
        }

        App.setRoot("Room_Dashboard");
    }
}