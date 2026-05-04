package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomManager {

    public void addRoom(Room room){
        if (room == null){
            throw new RuntimeException("Room cannot be null");
        }

        String sql = "INSERT INTO Room "
                + "(RoomNumber, FloorNumber, RoomType, RoomStatus, LastUpdated) "
                + "VALUES (?, ?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, room.getRoomNumber());
            statement.setInt(2, room.getFloorNumber());
            statement.setString(3, room.getRoomType());
            statement.setString(4, room.getRoomStatus());

            statement.executeUpdate();

        } catch (SQLException e){
            throw new RuntimeException("Unable to add room: " + e.getMessage(), e);
        }
    }

    public Room getRoom(int id){
        String sql = "SELECT * FROM Room WHERE RoomID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()){
                if (!result.next()){
                    throw new RuntimeException("Room not found");
                }

                return mapRoom(result);
            }

        } catch (SQLException e){
            throw new RuntimeException("Unable to get room: " + e.getMessage(), e);
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Room ORDER BY FloorNumber, RoomNumber";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                rooms.add(mapRoom(result));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to load rooms: " + e.getMessage(), e);
        }

        return rooms;
    }

    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();

        String sql = "SELECT * FROM Room WHERE RoomStatus = 'Available' ORDER BY FloorNumber, RoomNumber";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()){
                rooms.add(mapRoom(result));
            }

        } catch (SQLException e){
            throw new RuntimeException("Unable to load available rooms: " + e.getMessage(), e);
        }

        return rooms;
    }

    public void updateRoomStatus(int id, String status) {
        if (!status.equalsIgnoreCase("Available")
                && !status.equalsIgnoreCase("Occupied")
                && !status.equalsIgnoreCase("Closed")) {
            throw new RuntimeException("Invalid room status");
        }

        String updateRoom = "UPDATE Room SET RoomStatus = ?, LastUpdated = NOW() WHERE RoomID = ?";
        String releasePatients = "UPDATE Patient SET RoomID = NULL, LastUpdated = NOW() WHERE RoomID = ?";
        String endAssignments = "UPDATE PatientUserJunction j "
                + "JOIN Patient p ON j.PatientID = p.PatientID "
                + "SET j.AssignmentEnd = NOW() "
                + "WHERE p.RoomID = ? AND j.AssignmentEnd IS NULL";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                if (status.equalsIgnoreCase("Available") || status.equalsIgnoreCase("Closed")) {
                    try (PreparedStatement statement = connection.prepareStatement(endAssignments)) {
                        statement.setInt(1, id);
                        statement.executeUpdate();
                    }

                    try (PreparedStatement statement = connection.prepareStatement(releasePatients)) {
                        statement.setInt(1, id);
                        statement.executeUpdate();
                    }
                }

                try (PreparedStatement statement = connection.prepareStatement(updateRoom)) {
                    statement.setString(1, formatStatus(status));
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to update room status: " + e.getMessage(), e);
        }
    }

    public void removeRoom(int id) {
        Room room = getRoom(id);

        if (room.getRoomStatus().equalsIgnoreCase("Occupied")) {
            throw new RuntimeException("Cannot delete occupied room");
        }

        String sql = "DELETE FROM Room WHERE RoomID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to remove room: " + e.getMessage(), e);
        }
    }

    private Room mapRoom(ResultSet result) throws SQLException {
        Room room = new Room();

        room.setRoomID(result.getInt("RoomID"));
        room.setRoomNumber(result.getInt("RoomNumber"));
        room.setFloorNumber(result.getInt("FloorNumber"));
        room.setRoomType(result.getString("RoomType"));
        room.setRoomStatus(result.getString("RoomStatus"));

        if (result.getTimestamp("LastUpdated") != null) {
            room.setLastUpdated(String.valueOf(result.getTimestamp("LastUpdated")));
        }

        return room;
    }

    private String formatStatus(String status) {
        if (status.equalsIgnoreCase("Available")) {
            return "Available";
        }

        if (status.equalsIgnoreCase("Occupied")) {
            return "Occupied";
        }

        return "Closed";
    }
}