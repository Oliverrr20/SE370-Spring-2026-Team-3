package hospital_room_manager;

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

    private static final ObservableList<GuiPatient> patients = FXCollections.observableArrayList(
            new GuiPatient(1, "Maria", "Lopez", "Female", "1992-04-11",
                    "760-111-2222", "maria@email.com", "2025-02-01", 2),
            new GuiPatient(2, "James", "Brown", "Male", "1985-09-20",
                    "760-333-4444", "james@email.com", "2025-02-03", 6),
            new GuiPatient(3, "Ava", "Smith", "Female", "2001-01-15",
                    "760-555-6666", "ava@email.com", "2025-02-05", null),
            new GuiPatient(4, "Daniel", "Garcia", "Male", "1978-12-03",
                    "760-777-8888", "daniel@email.com", "2025-02-07", null)
    );

    public static ObservableList<GuiRoom> getRooms() {
        return rooms;
    }

    public static ObservableList<GuiPatient> getPatients() {
        return patients;
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