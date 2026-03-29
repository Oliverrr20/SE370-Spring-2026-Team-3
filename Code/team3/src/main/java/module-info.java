module hospital_room_manager {
    requires javafx.controls;
    requires javafx.fxml;

    opens hospital_room_manager to javafx.fxml;
    exports hospital_room_manager;
}
