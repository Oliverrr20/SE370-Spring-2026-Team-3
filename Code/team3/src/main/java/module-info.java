module hospital_room_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;

    opens hospital_room_manager to javafx.fxml;
    exports hospital_room_manager;
}
