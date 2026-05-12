package hospital_room_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HospitalGuiData {

    private static final ObservableList<GuiRoom> rooms = FXCollections.observableArrayList(
            new GuiRoom(1, 101, 1, "Standard", "Available"),
            new GuiRoom(2, 102, 1, "Standard", "Occupied"),
            new GuiRoom(3, 103, 1, "ICU", "Available"),
            new GuiRoom(4, 201, 2, "Standard", "Closed"),
            new GuiRoom(5, 202, 2, "Private", "Available"),
            new GuiRoom(6, 203, 2, "Private", "Occupied"),
            new GuiRoom(7, 301, 3, "ICU", "Available"),
            new GuiRoom(8, 302, 3, "Standard", "Available")
    );

    private static final String URL  = "jdbc:mysql://localhost:3306/se370team3";
    private static final String USER = "admin";
    private static final String PASS = "team3Pass!";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static ObservableList<GuiRoom> getRooms() {
        return rooms;
    }

    public static ObservableList<GuiPatient> getPatients() {
        ObservableList<GuiPatient> list = FXCollections.observableArrayList();

        String sql = "SELECT PatientID, FirstName, LastName, Gender, DOB, " +
                     "Phone, Email, AdmissionDate, RoomID FROM Patient";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new GuiPatient(
                        rs.getInt("PatientID"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Gender"),
                        rs.getString("DOB"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("AdmissionDate"),
                        rs.getObject("RoomID", Integer.class)
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static ObservableList<GuiRoom> getAvailableRooms() {
        ObservableList<GuiRoom> availableRooms = FXCollections.observableArrayList();

        for (GuiRoom room : rooms) {
            if (room.getRoomStatus().equalsIgnoreCase("Available")) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public static GuiRoom findRoomById(Integer roomId) {
        if (roomId == null) {
            return null;
        }

        for (GuiRoom room : rooms) {
            if (room.getRoomId() == roomId) {
                return room;
            }
        }

        return null;
    }

    public static String getRoomDisplayText(Integer roomId) {
        GuiRoom room = findRoomById(roomId);

        if (room == null) {
            return "Not Assigned";
        }

        return "Room " + room.getRoomNumber();
    }

    public static void assignPatientToRoom(GuiPatient patient, GuiRoom room) {
        if (patient == null || room == null) {
            return;
        }

        GuiRoom oldRoom = findRoomById(patient.getRoomId());

        if (oldRoom != null) {
            oldRoom.setRoomStatus("Available");
        }

        patient.setRoomId(room.getRoomId());
        room.setRoomStatus("Occupied");
    }
}
