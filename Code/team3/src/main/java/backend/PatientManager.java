package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientManager {

    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new RuntimeException("Patient cannot be null");
        }

        String sql = "INSERT INTO Patient "
                + "(FirstName, LastName, Gender, DOB, Phone, Email, AdmissionDate, LastUpdated) "
                + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getGender());
            statement.setString(4, patient.getDOB());
            statement.setString(5, patient.getPhone());
            statement.setString(6, patient.getEmail());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add patient: " + e.getMessage(), e);
        }
    }

    public Patient getPatient(int id) {
        String sql = "SELECT * FROM Patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new RuntimeException("Patient not found");
                }

                return mapPatient(result);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to get patient: " + e.getMessage(), e);
        }
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM Patient ORDER BY LastName, FirstName";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                patients.add(mapPatient(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load patients: " + e.getMessage(), e);
        }

        return patients;
    }

    public void placePatientInRoom(int patientId, int roomId, int assignedByUserId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Integer oldRoomId = findCurrentRoomForPatient(connection, patientId);
                String roomStatus = findRoomStatusForAssignment(connection, roomId);

                if (oldRoomId != null && oldRoomId == roomId) {
                    connection.commit();
                    return;
                }

                if (roomStatus.equalsIgnoreCase("Closed")) {
                    throw new RuntimeException("Closed rooms cannot receive patients");
                }

                if (roomAlreadyHasPatient(connection, patientId, roomId)) {
                    throw new RuntimeException("Room is already assigned to another patient");
                }

                if (roomStatus.equalsIgnoreCase("Occupied")) {
                    throw new RuntimeException("Room is already occupied");
                }

                endOpenAssignmentRecords(connection, patientId);
                setPatientRoom(connection, patientId, roomId);
                paintRoomStatus(connection, roomId, "Occupied");

                if (oldRoomId != null) {
                    paintRoomStatus(connection, oldRoomId, "Available");
                }

                if (assignedByUserId > 0) {
                    writeRoomAssignmentHistory(connection, patientId, assignedByUserId);
                }

                connection.commit();

            } catch (RuntimeException | SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to assign patient to room: " + e.getMessage(), e);
        }
    }

    private Integer findCurrentRoomForPatient(Connection connection, int patientId) throws SQLException {
        String sql = "SELECT RoomID FROM Patient WHERE PatientID = ? FOR UPDATE";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new RuntimeException("Patient not found");
                }

                int oldRoomId = result.getInt("RoomID");
                return result.wasNull() ? null : oldRoomId;
            }
        }
    }

    private String findRoomStatusForAssignment(Connection connection, int roomId) throws SQLException {
        String sql = "SELECT RoomStatus FROM Room WHERE RoomID = ? FOR UPDATE";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new RuntimeException("Room not found");
                }

                return result.getString("RoomStatus");
            }
        }
    }

    private boolean roomAlreadyHasPatient(Connection connection, int patientId, int roomId) throws SQLException {
        String sql = "SELECT PatientID FROM Patient WHERE RoomID = ? AND PatientID <> ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setInt(2, patientId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    private void endOpenAssignmentRecords(Connection connection, int patientId) throws SQLException {
        String sql = "UPDATE PatientUserJunction "
                + "SET AssignmentEnd = NOW() "
                + "WHERE PatientID = ? AND AssignmentEnd IS NULL";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);
            statement.executeUpdate();
        }
    }

    private void setPatientRoom(Connection connection, int patientId, int roomId) throws SQLException {
        String sql = "UPDATE Patient SET RoomID = ?, LastUpdated = NOW() WHERE PatientID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.setInt(2, patientId);
            statement.executeUpdate();
        }
    }

    private void paintRoomStatus(Connection connection, int roomId, String status) throws SQLException {
        String sql = "UPDATE Room SET RoomStatus = ?, LastUpdated = NOW() WHERE RoomID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, roomId);
            statement.executeUpdate();
        }
    }

    private void writeRoomAssignmentHistory(Connection connection, int patientId, int assignedByUserId) throws SQLException {
        String sql = "INSERT INTO PatientUserJunction "
                + "(PatientID, UserID, AssignmentTime) "
                + "VALUES (?, ?, NOW())";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);
            statement.setInt(2, assignedByUserId);
            statement.executeUpdate();
        }
    }

    public void removePatient(int id) {
        Patient patient = getPatient(id);

        if (patient.getRoomID() != null) {
            throw new RuntimeException("Cannot delete patient assigned to a room");
        }

        String sql = "DELETE FROM Patient WHERE PatientID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove patient: " + e.getMessage(), e);
        }
    }

    public boolean authenticate(String phone) {
        String sql = "SELECT * FROM Patient WHERE Phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, phone);

            try (ResultSet result = statement.executeQuery()) {
                return result.next() && result.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to check phone number: " + e.getMessage(), e);
        }
    }

    private Patient mapPatient(ResultSet result) throws SQLException {
        Patient patient = new Patient();

        patient.setPatientID(result.getInt("PatientID"));
        patient.setFirstName(result.getString("FirstName"));
        patient.setLastName(result.getString("LastName"));
        patient.setGender(result.getString("Gender"));
        patient.setDOB(String.valueOf(result.getDate("DOB")));
        patient.setPhone(result.getString("Phone"));
        patient.setEmail(result.getString("Email"));

        if (result.getTimestamp("AdmissionDate") != null) {
            patient.setAdmissionDate(String.valueOf(result.getTimestamp("AdmissionDate")));
        }

        if (result.getTimestamp("LeaveDate") != null) {
            patient.setLeaveDate(String.valueOf(result.getTimestamp("LeaveDate")));
        }

        if (result.getTimestamp("LastUpdated") != null) {
            patient.setLastUpdated(String.valueOf(result.getTimestamp("LastUpdated")));
        }

        int roomId = result.getInt("RoomID");
        patient.setRoomID(result.wasNull() ? null : roomId);

        return patient;
    }
}