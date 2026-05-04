package backend;

import org.mindrot.jbcrypt.BCrypt;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int PatientID;

    private String FirstName;
    private String LastName;
    private String Gender;
    private String DOB;
    private String Phone;
    private String Email;
    private String Pass;
    private String AdmissionDate;
    private String LeaveDate;
    private String LastUpdated;
    private Integer RoomID;

    public Patient(){
    }

    public Patient(String fn, String ln, String dob, String p, String pass) {
        FirstName = fn;
        LastName = ln;
        DOB = dob;
        Phone = p;
        RoomID = null;
        Pass = BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public Patient(String fn, String ln, String g, String dob, String p, String e, String pass){
        FirstName = fn;
        LastName = ln;
        Gender = g;
        DOB = dob;
        Phone = p;
        Email = e;
        RoomID = null;
        Pass = BCrypt.hashpw(pass, BCrypt.gensalt());
    }

    public int getPatientID(){
        return PatientID;
    }
    public void setPatientID(int patientID){
        PatientID = patientID;
    }

    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName(){
        return LastName;
    }
    public void setLastName(String lastName){
        LastName = lastName;
    }

    public String getGender(){
        return Gender;
    }
    public void setGender(String gender) {
        Gender = gender;
    }

    public String getDOB() {
        return DOB;
    }
    public void setDOB(String dob){
        DOB = dob;
    }

    public String getPhone(){
        return Phone;
    }
    public void setPhone(String phone){
        Phone = phone;
    }

    public String getEmail(){
        return Email;
    }
    public void setEmail(String email){
        Email = email;
    }

    public String getPass(){
        return Pass;
    }
    public void setPass(String pass){
        Pass = pass;
    }

    public String getAdmissionDate(){
        return AdmissionDate;
    }
    public void setAdmissionDate(String admissionDate){
        AdmissionDate = admissionDate;
    }

    public String getLeaveDate(){
        return LeaveDate;
    }
    public void setLeaveDate(String leaveDate){
        LeaveDate = leaveDate;
    }

    public String getLastUpdated(){
        return LastUpdated;
    }
    public void setLastUpdated(String lastUpdated){
        LastUpdated = lastUpdated;
    }

    public Integer getRoomID(){
        return RoomID;
    }
    public void setRoomID(Integer roomID){
        RoomID = roomID;
    }
}