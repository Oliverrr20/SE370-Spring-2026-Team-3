package hospital_room_manager;

import backend.Client;

//Stores the staff member that is already logged in (while the app is open)
public class LoginSession {

    private static Client currentClient;

    //Stores the staff member that just logged in. 
    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    //Gives controller acces to the staff member
    public static Client getCurrentClient() {
        return currentClient;
    }

    //Used to know if someone is signed in.
    public static boolean isLoggedIn() {
        return currentClient != null;
    }

    //The staff member is cleared when the user logs out. 
    public static void logout() {
        currentClient = null;
    }
}