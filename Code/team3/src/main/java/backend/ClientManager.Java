package backend;

import java.util.HashMap;
import java.util.Map;

public class ClientManager {

    private Map<Integer, Client> Clients = new HashMap<>();

    public boolean checkPhone(String phone) {
        for (Client c : Clients.values()) {
            if (c.Phone != null && c.Phone.equals(phone)) {
                return true;
            }
        }

        return false;
    }

    public void addClient(Client client) {
        if (client == null) {
            throw new RuntimeException("Client cannot be null");
        }

        Clients.put(client.UserID, client);
    }

    public Client authenticate(String phone, String pass) {
        for (Client c : Clients.values()) {
            if (c.Phone != null && c.Pass != null && c.Phone.equals(phone) && c.Pass.equals(pass)) {
                return c;
            }
        }

        return null;
    }

    public boolean isAdmin(Client c) {
        return c != null && c.UserRole != null && c.UserRole.equalsIgnoreCase("Admin");
    }

    public boolean isNurse(Client c) {
        return c != null && c.UserRole != null && c.UserRole.equalsIgnoreCase("Nurse");
    }

    public boolean isDoctor(Client c) {
        return c != null && c.UserRole != null && c.UserRole.equalsIgnoreCase("Doctor");
    }
}