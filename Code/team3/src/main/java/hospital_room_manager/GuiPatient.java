
package hospital_room_manager;

//This is the patient view object that uses the Gui, so screens dont have
//to display raw database objects directly.
public class GuiPatient {

    private int patientId;
    private String firstName;
    private String lastName;
    private String gender;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String admissionDate;
    private Integer roomId;

    public GuiPatient(int patientId, String firstName, String lastName, String gender, String dateOfBirth, String phone, String email, String admissionDate, Integer roomId) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.admissionDate = admissionDate;
        this.roomId = roomId;
    }

    //Getters that are used by Table-View columns and ComboBox.
    public int getPatientId() {
        return patientId;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }
}