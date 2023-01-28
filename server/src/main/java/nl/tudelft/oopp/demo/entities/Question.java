package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "question")
public class Question {

    @Id
    @Column(name = "id")
    @SequenceGenerator(
            name = "question_sequence",
            sequenceName = "question_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "question_sequence"
    )
    private Long id;

    @Column(name = "roomid")
    private String roomId;

    @Column(name = "username")
    private String username;

    @Column(name = "question")
    private String question;

    @Column(name = "time_asked")
    private LocalDateTime timeAsked;

    @Column(name = "upvotes")
    private int upvotes;

    @Column(name = "answered")
    private boolean answered;

    @Column(name = "creatorip")
    private String creatorIp;

    @ManyToMany
    @JoinTable(
            name = "upvoted",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonIgnore
    private Set<User> usersUpvoted = new HashSet<>();

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    /**
     * Empty constructor.
     */
    public Question() {
    }

    /**
     * Constructor for Question class with 5 local variables.
     *
     * @param roomId    room where the question is asked.
     * @param username  username of the person who asked the question.
     * @param question  the question itself.
     * @param timeAsked the time the question was asked.
     * @param creatorIp IP-address of the creator of this question.
     */
    public Question(String roomId, String username, String question, LocalDateTime timeAsked, String creatorIp) {
        this.roomId = roomId;
        this.username = username;
        this.question = question;
        this.timeAsked = timeAsked;
        this.creatorIp = creatorIp;
        this.answered = false;
        this.upvotes = 0;
    }

    /**
     * Constructor for Question class with 6 local variables.
     *
     * @param id        generated primary key for the table.
     * @param roomId    room where the question is asked.
     * @param username  username of the person who asked the question.
     * @param question  the question itself.
     * @param timeAsked the time the question was asked.
     * @param creatorIp IP-address of the creator of this question.
     */
    public Question(Long id, String roomId, String username, String question, LocalDateTime timeAsked,
                    String creatorIp) {
        this.id = id;
        this.roomId = roomId;
        this.username = username;
        this.question = question;
        this.timeAsked = timeAsked;
        this.creatorIp = creatorIp;
        this.answered = false;
        this.upvotes = 0;
    }

    /**
     * Constructor for Question class with 7 local variables.
     *
     * @param roomId    room where the question is asked.
     * @param username  username of the person who asked the question.
     * @param question  the question itself.
     * @param timeAsked the time the question was asked.
     * @param upvotes   the total amount of unique upvotes.
     * @param answered  boolean to see if a question is answered
     * @param creatorIp IP-address of the creator of this question.
     */
    public Question(String roomId, String username, String question, LocalDateTime timeAsked, int upvotes,
                    boolean answered, String creatorIp) {
        this.roomId = roomId;
        this.username = username;
        this.question = question;
        this.timeAsked = timeAsked;
        this.upvotes = upvotes;
        this.answered = answered;
        this.creatorIp = creatorIp;
    }

    /**
     * Constructor for Question class with 8 local variables.
     *
     * @param id        generated primary key for the table.
     * @param roomId    room where the question is asked.
     * @param username  username of the person who asked the question.
     * @param question  the question itself.
     * @param timeAsked the time the question was asked.
     * @param upvotes   the total amount of unique upvotes.
     * @param answered  boolean to see if a question is answered.
     * @param creatorIp IP-address of the creator of this question.
     */
    public Question(Long id, String roomId, String username, String question, LocalDateTime timeAsked,
                    int upvotes, boolean answered, String creatorIp) {
        this.id = id;
        this.roomId = roomId;
        this.username = username;
        this.question = question;
        this.timeAsked = timeAsked;
        this.upvotes = upvotes;
        this.answered = answered;
        this.creatorIp = creatorIp;
    }

    //--------------------------------------------
    // Getter section:
    //--------------------------------------------

    public Long getId() {
        return id;
    }

    public String getRoomID() {
        return roomId;
    }

    public String getUsername() {
        return username;
    }

    public String getQuestion() {
        return question;
    }

    public LocalDateTime getTimeAsked() {
        return timeAsked;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public boolean isAnswered() {
        return answered;
    }

    public String getCreatorIP() {
        return creatorIp;
    }

    public Set<User> getUsersUpvoted() {
        return usersUpvoted;
    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

    public void setId(Long id) {
        this.id = id;
    }

    public void setRoomID(String roomId) {
        this.roomId = roomId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setTimeAsked(LocalDateTime timeAsked) {
        this.timeAsked = timeAsked;
    }

    public void setUpvotes(int upvotes) {
        this.upvotes = upvotes;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public void setCreatorIP(String creatorIp) {
        this.creatorIp = creatorIp;
    }

    public void setUsersUpvoted(Set<User> usersUpvoted) {
        this.usersUpvoted = usersUpvoted;
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
        Question question1 = (Question) o;
        return upvotes == question1.upvotes
                && answered == question1.answered
                && id.equals(question1.id)
                && roomId.equals(question1.roomId)
                && username.equals(question1.username)
                && question.equals(question1.question)
                && timeAsked.equals(question1.timeAsked)
                && creatorIp.equals(question1.creatorIp)
                && usersUpvoted.equals(question1.usersUpvoted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomId, username, question, timeAsked, upvotes, answered, creatorIp);
    }

    @Override
    public String toString() {
        String s = "";
        if (this.answered) {
            s = "       - answered ✓";
        }
        return this.username + s + '\n' + question;
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    /**
     * Increment the amount of upvotes for this question by one.
     *
     * @param user the user that is adding an upvote to this question.
     */
    public void addUpvote(User user) {
        if (!usersUpvoted.contains(user)) {
            usersUpvoted.add(user);
            this.upvotes++;
        } else {
            removeUpvote(user);
        }
    }

    /**
     * Decrement the amount of upvotes for this question by one.
     *
     * @param user the user that is removing an upvote from this question.
     */
    public void removeUpvote(User user) {
        if (usersUpvoted.contains(user)) {
            usersUpvoted.remove(user);
            if (this.upvotes >= 1) {
                this.upvotes--;
            }
        }
    }
}
