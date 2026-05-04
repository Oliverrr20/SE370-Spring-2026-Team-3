package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class PatientManager {

    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new RuntimeException("Patient cannot be null");
        }

        String sql = "INSERT INTO Patient "
                + "(FirstName, LastName, Gender, DOB, Phone, Email, Pass, AdmissionDate, RoomID, LastUpdated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, patient.getFirstName());
            statement.setString(2, patient.getLastName());
            statement.setString(3, patient.getGender());
            statement.setString(4, patient.getDOB());
            statement.setString(5, patient.getPhone());
            statement.setString(6, patient.getEmail());
            statement.setString(7, patient.getPass());

            if (patient.getRoomID() == null) {
                statement.setNull(8, java.sql.Types.INTEGER);
            } else {
                statement.setInt(8, patient.getRoomID());
            }

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

    public Patient authenticate(String phone, String pass) {
        String sql = "SELECT * FROM Patient WHERE Phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, phone);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                Patient patient = mapPatient(result);
                String savedPassword = patient.getPass();

                if (savedPassword == null) {
                    return null;
                }

                boolean passwordMatches;

                if (savedPassword.startsWith("$2a$")
                        || savedPassword.startsWith("$2b$")
                        || savedPassword.startsWith("$2y$")) {
                    passwordMatches = BCrypt.checkpw(pass, savedPassword);
                } else {
                    passwordMatches = savedPassword.equals(pass);
                }

                return passwordMatches ? patient : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to authenticate patient: " + e.getMessage(), e);
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
        patient.setPass(result.getString("Pass"));

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