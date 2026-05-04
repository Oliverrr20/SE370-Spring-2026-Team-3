package backend;

import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mindrot.jbcrypt.BCrypt;

public class ClientManager {

    public boolean checkPhone(String phone) {
        String sql = "SELECT COUNT(*) FROM Client WHERE Phone = ?";

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

    public void addClient(Client client) {
        if (client == null) {
            throw new RuntimeException("Client cannot be null");
        }

        Clients.put(client.getClientID(), client);
        String sql = "INSERT INTO Client "
                + "(FirstName, LastName, UserRole, DOB, Phone, Email, Pass, LastUpdated) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getUserRole());
            statement.setString(4, client.getDOB());
            statement.setString(5, client.getPhone());
            statement.setString(6, client.getEmail());
            statement.setString(7, client.getPass());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to add client: " + e.getMessage(), e);
        }
    }

    public Client authenticate(String phone, String pass) {
        String sql = "SELECT * FROM Client WHERE Phone = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, phone);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    return null;
                }

                Client client = mapClient(result);
                String savedPassword = client.getPass();

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

                if (!passwordMatches) {
                    return null;
                }

                updateLastLogin(client.getClientID());
                return client;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Unable to authenticate client: " + e.getMessage(), e);
        }
    }

    private void updateLastLogin(int userId) {
        String sql = "UPDATE Client SET LastLogin = NOW(), LastUpdated = NOW() WHERE UserID = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Unable to update last login: " + e.getMessage(), e);
        }
    }

    private Client mapClient(ResultSet result) throws SQLException {
        Client client = new Client();

        client.setClientID(result.getInt("UserID"));
        client.setFirstName(result.getString("FirstName"));
        client.setLastName(result.getString("LastName"));
        client.setUserRole(result.getString("UserRole"));
        client.setDOB(String.valueOf(result.getDate("DOB")));
        client.setPhone(result.getString("Phone"));
        client.setEmail(result.getString("Email"));
        client.setPass(result.getString("Pass"));

        if (result.getTimestamp("LastLogin") != null) {
            client.setLastLogin(String.valueOf(result.getTimestamp("LastLogin")));
        }

        if (result.getTimestamp("LastUpdated") != null) {
            client.setLastUpdated(String.valueOf(result.getTimestamp("LastUpdated")));
        }

        return client;
    }

    public boolean isAdmin(Client c) {
        return c != null && c.getUserRole() != null && c.getUserRole().equalsIgnoreCase("Admin");
    }

    public boolean isNurse(Client c) {
        return c != null && c.getUserRole() != null && c.getUserRole().equalsIgnoreCase("Nurse");
    }

    public boolean isDoctor(Client c) {
        return c != null && c.getUserRole() != null && c.getUserRole().equalsIgnoreCase("Doctor");
    }
}