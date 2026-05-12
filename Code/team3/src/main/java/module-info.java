module hospital_room_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires java.sql;           

    opens hospital_room_manager to javafx.fxml;
    opens backend to jakarta.persistence;

    exports hospital_room_manager;
}
