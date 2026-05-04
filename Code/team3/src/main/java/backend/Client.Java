package backend;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UserID;

    private String FirstName;
    private String LastName;
    private String UserRole;
    private String DOB;
    private String Phone;
    private String Email;
    private String Pass;
    private String LastLogin;
    private String LastUpdated;

    public Client() {
    }

    public Client(String fn, String ln, String ur, String dob, String p, String pass) {
        FirstName = fn;
        LastName = ln;
        UserRole = ur;
        DOB = dob;
        Phone = p;
        Pass = BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public Client(String fn, String ln, String ur, String dob, String p, String e, String pass) {
        FirstName = fn;
        LastName = ln;
        UserRole = ur;
        DOB = dob;
        Phone = p;
        Email = e;
        Pass = BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public int getClientID() {
        return UserID;
    }

    public void setClientID(int userID) {
        UserID = userID;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String dob) {
        DOB = dob;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getLastLogin() {
        return LastLogin;
    }

    public void setLastLogin(String lastLogin) {
        LastLogin = lastLogin;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }
}