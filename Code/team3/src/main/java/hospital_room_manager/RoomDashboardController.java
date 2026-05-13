package hospital_room_manager;

import java.io.IOException;

import backend.Room;
import backend.RoomManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.StringConverter;

public class RoomDashboardController {
    @FXML private Label totalRoomsLabel;
    @FXML private Label availableRoomsLabel;
    @FXML private Label occupiedRoomsLabel;
    @FXML private Label closedRoomsLabel;
    @FXML private FlowPane roomCardsPane;

    @FXML private Rectangle overlay;

    @FXML private VBox popupPane;
    @FXML private Spinner<Integer> roomNumberField;
    @FXML private Spinner<Integer> floorField;
    @FXML private TextField typeField;
    @FXML private Label messageLabel;

    @FXML private VBox deleteRoomPane;
    @FXML private ComboBox<GuiRoom> roomChosenForDeleteBox;
    @FXML private Label deleteRoomMessageLabel;

    private RoomManager roomManager = new RoomManager();

    @FXML
    private void initialize() {
        roomNumberField.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1)
        );

        floorField.setValueFactory(
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 50, 0)
        );

        roomNumberField.setEditable(true);
        floorField.setEditable(true);

        prepareRoomDeleteDropdown();

        closePopup();
        closeRoomDeletePopup();
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

    private void prepareRoomDeleteDropdown() {
        roomChosenForDeleteBox.setConverter(new StringConverter<GuiRoom>() {
            @Override
            public String toString(GuiRoom room) {
                if (room == null) {
                    return "";
                }

                return "Room " + room.getRoomNumber()
                        + " - Floor " + room.getFloorNumber()
                        + " - " + room.getRoomType()
                        + " - " + room.getRoomStatus();
            }

            @Override
            public GuiRoom fromString(String string) {
                return null;
            }
        });
    }

    private void refillRoomDeleteChoices() {
        roomChosenForDeleteBox.setItems(HospitalGuiData.getRooms());
        roomChosenForDeleteBox.setValue(null);
    }

    @FXML
    private void openPopup() {
        closeRoomDeletePopup();

        if (messageLabel != null) {
            messageLabel.setText("");
        }

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
            roomNumberField.getValueFactory().setValue(1);
        }

        if (floorField != null) {
            floorField.getValueFactory().setValue(0);
        }

        if (typeField != null) {
            typeField.clear();
        }

        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }

    @FXML
    private void openRoomDeletePopup() {
        closePopup();

        try {
            refillRoomDeleteChoices();

            if (deleteRoomMessageLabel != null) {
                deleteRoomMessageLabel.setText("");
            }

            if (overlay != null) {
                overlay.setVisible(true);
                overlay.setManaged(true);
            }

            if (deleteRoomPane != null) {
                deleteRoomPane.setVisible(true);
                deleteRoomPane.setManaged(true);
            }

        } catch (RuntimeException e) {
            if (deleteRoomMessageLabel != null) {
                deleteRoomMessageLabel.setText("Couldn't load rooms to delete.");
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void closeRoomDeletePopup() {
        if (overlay != null) {
            overlay.setVisible(false);
            overlay.setManaged(false);
        }

        if (deleteRoomPane != null) {
            deleteRoomPane.setVisible(false);
            deleteRoomPane.setManaged(false);
        }

        if (roomChosenForDeleteBox != null) {
            roomChosenForDeleteBox.setValue(null);
        }

        if (deleteRoomMessageLabel != null) {
            deleteRoomMessageLabel.setText("");
        }
    }

    @FXML
    private void onSaveRoomClicked() throws IOException {
        if (roomNumberField == null || floorField == null || typeField.getText().isBlank()) {
            if (messageLabel != null) {
                messageLabel.setText("Please enter a room type.");
            }
            return;
        }

        try {
            int roomNumber = roomNumberField.getValue();
            int floorNumber = floorField.getValue();

            Room newRoom = new Room(
                roomNumber,
                floorNumber,
                typeField.getText().trim()
            );

            roomManager.addRoom(newRoom);

            closePopup();
            loadDashboard();

        } catch (RuntimeException e) {
            if (messageLabel != null) {
                messageLabel.setText("Room was not added: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteRoomChosenByStaff() {
        GuiRoom roomChosenByStaff = roomChosenForDeleteBox.getValue();

        if (roomChosenByStaff == null) {
            deleteRoomMessageLabel.setText("Please choose a room to delete.");
            return;
        }

        try {
            roomManager.deleteRoomAfterSafetyCheck(roomChosenByStaff.getRoomId());

            deleteRoomMessageLabel.setText("Room "
                    + roomChosenByStaff.getRoomNumber()
                    + " was deleted.");

            refillRoomDeleteChoices();
            loadDashboard();

        } catch (RuntimeException e) {
            deleteRoomMessageLabel.setText("Room was not deleted: " + e.getMessage());
            e.printStackTrace();
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
    private void goToAddPatient() throws IOException {
        App.setRoot("add_patient");
    }

    @FXML
    private void logout() throws IOException {
        LoginSession.logout();
        App.setRoot("login");
    }
}