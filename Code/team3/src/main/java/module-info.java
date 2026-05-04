module hospital_room_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires jbcrypt;
    requires java.sql;

    opens hospital_room_manager to javafx.fxml;
    opens backend to jakarta.persistence;

    exports hospital_room_manager;
    exports backend;
}