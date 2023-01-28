package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// If the name of the table is not changed user will cause syntax errors within postgresql since
// it's already reserved.
@Entity
@Table(name = "user_entity")
public class User {

    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "ip")
    private String ip;

    @Column(name = "is_banned")
    private boolean isBanned;

    @Column(name = "current_roomid")
    private String currentRoomId = null;

    @Column(name = "user_type")
    private String userType;

    @OneToMany
    @JsonIgnore
    private Set<Room> roomsCreated = new HashSet<>();

    @ManyToMany(mappedBy = "usersUpvoted")
    @JsonIgnore
    private Set<Question> questionsUpvoted = new HashSet<>();

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    /**
     * Empty constructor.
     */
    public User() {
    }

    /**
     * Constructor for Room class with 2 local variables.
     *
     * @param username username of the user.
     * @param ip       IP-address of the user.
     */
    public User(String username, String ip) {
        this.username = username;
        this.ip = ip;
        this.isBanned = false;
    }

    /**
     * Constructor for Room class with 3 local variables.
     *
     * @param username username of the user.
     * @param ip       IP-address of the user.
     * @param userType the type of user (lecturer, moderator or student).
     */
    public User(String username, String ip, String userType) {
        this.username = username;
        this.ip = ip;
        this.userType = userType;
        this.isBanned = false;
    }

    /**
     * Constructor for Room class with 3 local variables.
     *
     * @param id       id to identify the user.
     * @param username username of the user.
     * @param ip       IP-address of the user.
     */
    public User(Long id, String username, String ip) {
        this.id = id;
        this.username = username;
        this.ip = ip;
        this.isBanned = false;
    }

    /**
     * Constructor for Room class with 5 local variables.
     *
     * @param username      username of the user.
     * @param ip            IP-address of the user.
     * @param isBanned      boolean to see whether or not the user is banned.
     * @param currentRoomId the current roomID the user is attending.
     * @param userType      the type of user (lecturer, moderator or student).
     */
    public User(String username, String ip, boolean isBanned, String currentRoomId, String userType) {
        this.username = username;
        this.ip = ip;
        this.isBanned = isBanned;
        this.currentRoomId = currentRoomId;
        this.userType = userType;
    }

    /**
     * Constructor for Room class with 6 local variables.
     *
     * @param id            id to identify the user.
     * @param username      username of the user.
     * @param ip            IP-address of the user.
     * @param isBanned      boolean to see whether or not the user is banned.
     * @param currentRoomId the current roomID the user is attending.
     * @param userType      the type of user (lecturer, moderator or student).
     */
    public User(Long id, String username, String ip, boolean isBanned, String currentRoomId, String userType) {
        this.id = id;
        this.username = username;
        this.ip = ip;
        this.isBanned = isBanned;
        this.currentRoomId = currentRoomId;
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

    public String getIP() {
        return ip;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public String getCurrentRoomID() {
        return currentRoomId;
    }

    public String getUserType() {
        return userType;
    }

    public Set<Room> getRoomsCreated() {
        return roomsCreated;
    }

    public Set<Question> getQuestionsUpvoted() {
        return questionsUpvoted;
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

    public void setIP(String ip) {
        this.ip = ip;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public void setCurrentRoomID(String currentRoomId) {
        this.currentRoomId = currentRoomId;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setRoomsCreated(Set<Room> roomsCreated) {
        this.roomsCreated = roomsCreated;
    }

    public void setQuestionsUpvoted(Set<Question> questionsUpvoted) {
        this.questionsUpvoted = questionsUpvoted;
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
        return isBanned == user.isBanned
                && Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(ip, user.ip)
                && Objects.equals(currentRoomId, user.currentRoomId)
                && Objects.equals(userType, user.userType)
                && Objects.equals(roomsCreated, user.roomsCreated)
                && Objects.equals(questionsUpvoted, user.questionsUpvoted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, ip, isBanned, currentRoomId, userType, roomsCreated);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", IP='" + ip + '\''
                + ", isBanned=" + isBanned
                + ", currentRoomId='" + currentRoomId + '\''
                + ", userType='" + userType + '\''
                + ", roomsCreated=" + roomsCreated
                + ", questionsUpvoted=" + questionsUpvoted
                + '}';
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    public void addRoomCreated(Room r) {
        this.roomsCreated.add(r);
    }

    public void removeRoomCreated(Room r) {
        this.roomsCreated.remove(r);
    }

    /**
     * Adds a question to the list of upvoted question by this user.
     *
     * @param q question that needs to be added to the list.
     */
    public void addQuestionUpvoted(Question q) {
        if (questionsUpvoted.contains(q)) {
            questionsUpvoted.remove(q);
        } else {
            questionsUpvoted.add(q);
        }
    }
}