package hospital_room_manager;

import backend.Room;
import backend.RoomManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HospitalGuiData {

    private static final RoomManager roomManager = new RoomManager();

    private static final ObservableList<GuiPatient> patients = FXCollections.observableArrayList(
            new GuiPatient(1, "Julia", "Hernandez", "Female", "1989-09-06",
                    "760-111-2222", "maria@email.com", "2025-04-03", 2),
            new GuiPatient(2, "Will", "Smith", "Male", "2002-04-20",
                    "760-333-4444", "smith@email.com", "2025-02-03", 6),
            new GuiPatient(3, "Bruce", "Wayne", "Male", "1999-02-15",
                    "760-555-6666", "batman@email.com", "2025-02-05", null),
            new GuiPatient(4, "Hannah", "Jones", "Female", "1981-12-08",
                    "760-777-8888", "jones@email.com", "2025-02-07", null)
    );

    public static ObservableList<GuiRoom> getRooms() {
        ObservableList<GuiRoom> guiRooms = FXCollections.observableArrayList();

        for (Room room : roomManager.getAllRooms()) {
            guiRooms.add(convertRoomToGuiRoom(room));
        }

        return guiRooms;
    }

    public static ObservableList<GuiPatient> getPatients() {
        return patients;
    }

    public static ObservableList<GuiRoom> getAvailableRooms() {
        ObservableList<GuiRoom> availableRooms = FXCollections.observableArrayList();

        for (GuiRoom room : getRooms()) {
            if (room.getRoomStatus() != null && room.getRoomStatus().equalsIgnoreCase("Available")) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }

    public static GuiRoom findRoomById(Integer roomId) {
        if (roomId == null) {
            return null;
        }

        for (GuiRoom room : getRooms()) {
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
            roomManager.updateRoomStatus(oldRoom.getRoomId(), "Available");
        }

        patient.setRoomId(room.getRoomId());
        roomManager.updateRoomStatus(room.getRoomId(), "Occupied");
    }

    private static GuiRoom convertRoomToGuiRoom(Room room) {
        return new GuiRoom(
                room.getRoomID(),
                room.getRoomNumber(),
                room.getFloorNumber(),
                room.getRoomType(),
                room.getRoomStatus()
        );
    }
}