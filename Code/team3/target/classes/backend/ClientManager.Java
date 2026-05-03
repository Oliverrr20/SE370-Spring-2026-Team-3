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

    public Client getClient(int id) {
        if (!Clients.containsKey(id)) {
            throw new RuntimeException("Client not found");
        }
        return Clients.get(id);
    }

    public Map<Integer, Client> getAllClients() {
        return Clients;
    }

    public Client authenticate(String phone, String pass) {
        for (Client c : Clients.values()) {
            if (c.Phone != null && c.Pass != null && c.Phone.equals(phone) && c.Pass.equals(pass)) {
                return c;
            }
        }

        return null;
    }

    public void updateClient(int id, Client updatedClient) {
        if (!Clients.containsKey(id)) {
            throw new RuntimeException("Client not found");
        }
        Clients.put(id, updatedClient);
    }

    public void updatePhone(int id, String newPhone) {
        Client c = getClient(id);
        c.Phone = newPhone;
    }

    public void updatePassword(int id, String newPass) {
        Client c = getClient(id);
        c.Pass = newPass;
    }

    public void updateEmail(int id, String newEmail) {
        Client c = getClient(id);
        c.Email = newEmail;
    }

    public void deleteClient(int id) {
        if (!Clients.containsKey(id)) {
            throw new RuntimeException("Client not found");
        }
        Clients.remove(id);
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