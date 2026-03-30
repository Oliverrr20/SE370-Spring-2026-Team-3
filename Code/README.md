FRONTEND
1)  On VSCode, install these extension packs:
    Java
    Extension Pack for Java
    Maven for Java
2) On VSCode (or your preferred IDE), click Clone Git Repository…
Enter the name of this repository and open the window. You should see the Code, Journal, and Notes folders on the left.
3) Inside Code>team3>src\main>java>hospital_room_manager>App.Java (you want App.Java open), press the Run Java play button in the top right corner. The application should pop up.

Troubleshooting Frontend:
1) If thrown the error: JAVA_HOME not found in your environment.
Find your jdk datapath in files (in program files either in Java or Eclipse Adoptium)
Go to System Variables and add JAVA_HOME with the same data path as the jdk, also add %JAVA_HOME%\bin to path
Restart all terminals

BACKEND
If you need visuals, I used this YouTube video! https://www.youtube.com/watch?v=Cz3WcZLRaWc 

1) Create an account on MySQL and download the installer Community Edition - Community Server. 
    https://dev.mysql.com/downloads/mysql/ 
During the install, use the password: team3Pass!, create a user named: admin and use the same password
After installing, go to the MySQL command line. You will be prompted to enter a password, use the one above. Then type the command: 
    CREATE DATABASE se370team3; 
Hit enter.
To confirm, type the command: 
	show databases; 
It should be in the list among information_schema, mysql, performance schema, and sys. Those are default databases we won’t be using, but if they’re there, don’t worry about them.
Grant permission to the user via this in MySQL command line: GRANT ALL PRIVILEGES ON se370team3.* TO 'admin'@'%'; 
	
2) Download SQLTools extension on VSCode (or whatever IDE you are using).
3) Open SQLTools in VSCode (cyclinder at the bottom of connections on left bar).
4) Click “Add New Connection” and use these credentials:
	Connection name: hospitalRoomManager
    Database: se370team3
    Username: set to your name
    Password Mode: Ask on connect
Everything else leave blank or as is.
5) Test connection first, ensure you get “Successfully connected” before pressing Save connection.
6) Go to sqlScripts, run each script individually, only once per script, by pressing Run on active connective button.
7) If you want to add to or edit the database, create a new script each time 


Troubleshooting Backend:
1) If trying to add connection and you have no driver, install the extension pack: SQLTools MySQL/MariaDB/TiDB
Click the MySQL driver one
