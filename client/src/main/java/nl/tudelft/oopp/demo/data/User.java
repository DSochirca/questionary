package nl.tudelft.oopp.demo.data;

import java.util.Objects;


/**
 * The type User.
 */
public class User {

    private Long id;
    private String username;
    private String ip;
    private String currentRoomID = null;
    private String userType;                // User type is always small characters

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    /**
     * Empty constructor.
     */
    public User() {
    }

    /**
     * Constructor for Room class with 3 local variables.
     * @param username  username of the user.
     * @param ip        IP-address of the user.
     * @param userType  the type of user (lecturer, moderator or student).
     */
    public User(String username, String ip, String userType) {
        this.username = username;
        this.ip = ip;
        this.userType = userType;
    }

    //--------------------------------------------
    // Getter section:
    //--------------------------------------------

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getip() {
        return ip;
    }

    public String getCurrentRoomID() {
        return currentRoomID;
    }

    public String getUserType() {
        return userType;
    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCurrentRoomID(String currentRoomID) {
        this.currentRoomID = currentRoomID;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    //--------------------------------------------
    // Equals, hashCode, toString methods section:
    //--------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(ip, user.ip)
                && Objects.equals(currentRoomID, user.currentRoomID)
                && Objects.equals(userType, user.userType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, ip, currentRoomID, userType);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", ip='" + ip + '\''
                + ", currentRoomID='" + currentRoomID + '\''
                + ", userType='" + userType + '\''
                + '}';
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

}
