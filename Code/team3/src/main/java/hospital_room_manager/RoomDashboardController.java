package hospital_room_manager;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class RoomDashboardController {

    @FXML
    private Label totalRoomsLabel;
    @FXML
    private Label availableRoomsLabel;
    @FXML
    private Label occupiedRoomsLabel;
    @FXML
    private Label closedRoomsLabel;
    @FXML
    private FlowPane roomCardsPane;
    @FXML
    private Rectangle overlay;
    @FXML
    private VBox popupPane;
    @FXML
    private TextField roomNumberField;
    @FXML
    private TextField floorField;
    @FXML
    private TextField typeField;

    @FXML
    private void initialize() {
        closePopup();
        loadDashboard();
    }

    private void loadDashboard() {
        try {
            ObservableList<GuiRoom> rooms = HospitalGuiData.getRooms();

            int available = 0;
            int occupied = 0;
            int closed = 0;

            roomCardsPane.getChildren().clear();

            for (GuiRoom room : rooms) {
                if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Available")) {
                    available++;
                } else if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Occupied")) {
                    occupied++;
                } else if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Closed")) {
                    closed++;
                }

                VBox card = createRoomCard(room);
                roomCardsPane.getChildren().add(card);
            }

            totalRoomsLabel.setText(String.valueOf(rooms.size()));
            availableRoomsLabel.setText(String.valueOf(available));
            occupiedRoomsLabel.setText(String.valueOf(occupied));
            closedRoomsLabel.setText(String.valueOf(closed));

            if (rooms.isEmpty()) {
                Label emptyLabel = new Label("Sorry, no rooms were found in the database.");
                emptyLabel.getStyleClass().add("info-label");
                roomCardsPane.getChildren().add(emptyLabel);
            }

        } catch (RuntimeException e) {
            totalRoomsLabel.setText("0");
            availableRoomsLabel.setText("0");
            occupiedRoomsLabel.setText("0");
            closedRoomsLabel.setText("0");

            roomCardsPane.getChildren().clear();

            Label errorLabel = new Label("Sorry, couldn't load rooms. Please check your database connection.");
            errorLabel.getStyleClass().add("error-label");
            roomCardsPane.getChildren().add(errorLabel);

            e.printStackTrace();
        }
    }

    private VBox createRoomCard(GuiRoom room) {
        Label roomNumber = new Label("Room " + room.getRoomNumber());
        roomNumber.getStyleClass().add("room-title");

        Label floor = new Label("Floor: " + room.getFloorNumber());
        Label type = new Label("Type: " + room.getRoomType());
        Label status = new Label("Status: " + room.getRoomStatus());

        VBox card = new VBox(8, roomNumber, floor, type, status);
        card.getStyleClass().add("room-card");

        if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Available")) {
            card.getStyleClass().add("available-room");
        } else if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Occupied")) {
            card.getStyleClass().add("occupied-room");
        } else {
            card.getStyleClass().add("closed-room");
        }

        return card;
    }

    @FXML
    private void openPopup() {
        if (overlay != null) {
            overlay.setVisible(true);
            overlay.setManaged(true);
        }

        if (popupPane != null) {
            popupPane.setVisible(true);
            popupPane.setManaged(true);
        }
    }

    @FXML
    private void closePopup() {
        if (overlay != null) {
            overlay.setVisible(false);
            overlay.setManaged(false);
        }

        if (popupPane != null) {
            popupPane.setVisible(false);
            popupPane.setManaged(false);
        }

        if (roomNumberField != null) {
            roomNumberField.clear();
        }

        if (floorField != null) {
            floorField.clear();
        }

        if (typeField != null) {
            typeField.clear();
        }
    }

    @FXML
    private void goToDashboard() throws IOException {
        loadDashboard();
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
    private void logout() throws IOException {
        LoginSession.logout();
        App.setRoot("login");
    }
}