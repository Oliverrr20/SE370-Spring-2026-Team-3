package hospital_room_manager;

import backend.Client;
import backend.Patient;
import backend.PatientManager;
import backend.Room;
import backend.RoomManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HospitalGuiData {

    private static final PatientManager patientManager = new PatientManager();
    private static final RoomManager roomManager = new RoomManager();

    public static ObservableList<GuiRoom> getRooms() {
        ObservableList<GuiRoom> guiRooms = FXCollections.observableArrayList();

        for (Room room : roomManager.getAllRooms()) {
            guiRooms.add(convertRoomToGuiRoom(room));
        }

        return guiRooms;
    }

    public static ObservableList<GuiPatient> getPatients() {
        ObservableList<GuiPatient> guiPatients = FXCollections.observableArrayList();

        for (Patient patient : patientManager.getAllPatients()) {
            guiPatients.add(convertPatientToGuiPatient(patient));
        }

        return guiPatients;
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

    public static void placePatientInRoom(GuiPatient patient, GuiRoom room) {
        if (patient == null || room == null) {
            throw new RuntimeException("Select a patient and an available room");
        }

        int assignedByUserId = 0;
        Client currentClient = LoginSession.getCurrentClient();

        if (currentClient != null) {
            assignedByUserId = currentClient.getClientID();
        }

        patientManager.placePatientInRoom(patient.getPatientId(), room.getRoomId(), assignedByUserId);
        patient.setRoomId(room.getRoomId());
        room.setRoomStatus("Occupied");
    }

    public static void assignPatientToRoom(GuiPatient patient, GuiRoom room) {
        placePatientInRoom(patient, room);
    }

    private static GuiPatient convertPatientToGuiPatient(Patient patient) {
        return new GuiPatient(
                patient.getPatientID(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getGender(),
                patient.getDOB(),
                patient.getPhone(),
                patient.getEmail(),
                patient.getAdmissionDate(),
                patient.getRoomID()
        );
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