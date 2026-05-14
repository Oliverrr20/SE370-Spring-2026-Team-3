package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//Important class that handles the room database actions used by the room dashboard
public class RoomManager {

    //Saves a new room when it is created, and shows it as "Available" in the database.
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
    //Finds a room using the Id
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
    //loads and shows all rooms for the dashboard. 
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

    //Used to only load rooms that can be used in assignment (or detetion).
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

    //Changes the room status 
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

    //As the name tell us, it deletes a room after checking that no patient is assigned in there
    //Note: didnt think on a better name
    public void deleteRoomAfterSafetyCheck(int roomId) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try {
                String roomStatus = findRoomStatusBeforeDelete(connection, roomId);

                if (roomStatus.equalsIgnoreCase("Occupied")) {
                    throw new RuntimeException("This room is occupied and cannot be deleted.");
                }

                if (someoneIsStillAssignedToRoom(connection, roomId)) {
                    throw new RuntimeException("This room still has a patient assigned to it.");
                }

                removeTheRoomRecord(connection, roomId);
                connection.commit();

            } catch (RuntimeException | SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to delete room: " + e.getMessage(), e);
        }
    }

    //An old method that is used for compatibility, the most used method is using the dashboard.
    public void removeRoom(int id) {
        deleteRoomAfterSafetyCheck(id);
    }

    //As it names tell us again, it checks the room status before deleting it.
    private String findRoomStatusBeforeDelete(Connection connection, int roomId) throws SQLException {
        String sql = "SELECT RoomStatus FROM Room WHERE RoomID = ? FOR UPDATE";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new RuntimeException("Room not found.");
                }

                return result.getString("RoomStatus");
            }
        }
    }

    //Checks that the room is not connected with a patient (double check)
    private boolean someoneIsStillAssignedToRoom(Connection connection, int roomId) throws SQLException {
        String sql = "SELECT PatientID FROM Patient WHERE RoomID = ? LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    //After passing all the checks it runs the deletion 
    private void removeTheRoomRecord(Connection connection, int roomId) throws SQLException {
        String sql = "DELETE FROM Room WHERE RoomID = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, roomId);
            statement.executeUpdate();
        }
    }

    //Converts a database row into a room object. 
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