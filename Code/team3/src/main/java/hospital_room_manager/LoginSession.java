package hospital_room_manager;

import backend.Client;

public class LoginSession {

    private static Client currentClient;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static boolean isLoggedIn() {
        return currentClient != null;
    }

    public static void logout() {
        currentClient = null;
    }
}