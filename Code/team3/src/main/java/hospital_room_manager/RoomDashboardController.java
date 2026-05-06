package hospital_room_manager;

import java.io.IOException;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class RoomDashboardController{

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
    private VBox popupPane;

    @FXML
    private void initialize(){
        loadDashboard();
    }

    private void loadDashboard(){

        ObservableList<GuiRoom> rooms = HospitalGuiData.getRooms();

        int available = 0;
        int occupied = 0;
        int closed = 0;

        roomCardsPane.getChildren().clear();

        for(GuiRoom room: rooms){
            if(room.getRoomStatus().equalsIgnoreCase("Available")){
                available++;
            } else if(room.getRoomStatus().equalsIgnoreCase("Occupied")){
                occupied++;
            } else if(room.getRoomStatus().equalsIgnoreCase("Closed")){
                closed++;
            }

            VBox card = createRoomCard(room);
            roomCardsPane.getChildren().add(card);
        }

        totalRoomsLabel.setText(String.valueOf(rooms.size()));
        availableRoomsLabel.setText(String.valueOf(available));
        occupiedRoomsLabel.setText(String.valueOf(occupied));
        closedRoomsLabel.setText(String.valueOf(closed));
    }

    private VBox createRoomCard(GuiRoom room){
        Label roomNumber = new Label("Room: " + room.getRoomNumber());
        roomNumber.getStyleClass().add("room-name");

        Label floor = new Label("Floor: " + room.getFloorNumber());
        floor.getStyleClass().add("room-floor");

        Label type = new Label("Type: " + room.getRoomType());
        type.getStyleClass().add("room-type");
        Label status = new Label("Status: " + room.getRoomStatus());
        status.getStyleClass().add("room-staus");

        VBox card = new VBox(8, roomNumber, floor, type, status);
        card.getStyleClass().add("room-card");

        if(room.getRoomStatus().equalsIgnoreCase("Available")){
            card.getStyleClass().add("available-room");
        } else if(room.getRoomStatus().equalsIgnoreCase("Occupied")){
            card.getStyleClass().add("occupied-room");
        } else{
            card.getStyleClass().add("closed-room");
        }
        return card;
    }

    @FXML
    private void openPopup() throws IOException{
        popupPane.setVisible(true);
    }
    @FXML
    private void closePopup() {
        popupPane.setVisible(false);
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
    private void goToPatientInfo() throws  IOException{
        App.setRoot("patient_info");
    }
    @FXML void logout() throws IOException{
        App.setRoot("login");
    }
}