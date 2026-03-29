DROP TABLE IF EXISTS PatientUserJunction;
DROP TABLE IF EXISTS Patient;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS Room;

CREATE TABLE Room(
    RoomID INT PRIMARY KEY AUTO_INCREMENT,
    RoomNumber INT NOT NULL,
    FloorNumber INT NOT NULL,
    RoomType VARCHAR(50),
    RoomStatus ENUM('Available', 'Occupied', 'Closed') NOT NULL,
    LastUpdated DATETIME
);

CREATE TABLE Patient(
    PatientID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    Phone VARCHAR(20),
    Email VARCHAR(255),
    AdmissionDate DATETIME NOT NULL,
    LeaveDate DATETIME,
    LastUpdated DATETIME,
    RoomID INT, 
    FOREIGN KEY (RoomID) REFERENCES Room(RoomID) ON DELETE SET NULL
);

CREATE TABLE Client(
    UserID INT PRIMARY KEY AUTO_INCREMENT,
    FirstName VARCHAR(100) NOT NULL,
    LastName VARCHAR(100) NOT NULL,
    UserRole VARCHAR(50) NOT NULL,
    Phone VARCHAR(20),
    Email VARCHAR(255) NOT NULL,
    Pass TEXT NOT NULL,
    LastLogin DATETIME,
    LastUpdated DATETIME
);

CREATE TABLE PatientUserJunction(
    PatientUserJunctionID INT PRIMARY KEY AUTO_INCREMENT,
    PatientID  INT NOT NULL,
    UserID INT NOT NULL,
    AssignmentTime DATETIME NOT NULL,
    AssignmentEnd DATETIME,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (UserID) REFERENCES Client(UserID) ON DELETE CASCADE
);

