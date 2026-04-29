package backend;

import java.util.HashMap;
import java.util.Map;

class ClientManager {
    //store clients
    private Map<Integer, Client> Clients = new HashMap<>();
    //check if phone number already exists
    public boolean checkPhone(String phone) {
        for (Client c : Clients.values()) {
            if (c.Phone != null && c.Phone.equals(phone)) {
                return true;
            }
        }
        return false;
    }
    //login using phone and password
    public Client authenticate(String phone, String pass) {
        for (Client c : Clients.values()) {
            if (c.Phone != null && c.Pass != null && c.Phone.equals(phone) && c.Pass.equals(pass)) {
                return c;
            }
        }
        return null;
    }
        //checks if user is admin
        public boolean isAdmin(Client c){
            return c.UserRole != null && c.UserRole.equalsIgnoreCase("Admin");
        }
        public boolean isNurse(Client c){
            return c.UserRole != null && c.UserRole.equalsIgnoreCase("Nurse");
        }
        public boolean isDoctor(Client c){
            return c.UserRole != null && c.UserRole.equalsIgnoreCase("Doctor");
        }
    }
}
