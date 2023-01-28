package nl.tudelft.oopp.demo.data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * The type Question.
 */
public class Question {

    private Long id;
    private String roomID;
    private String username;
    private String question;
    private LocalDateTime timeAsked;
    private int upvotes;
    private boolean answered;
    private String creatorIP;

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    /**
     * Empty constructor.
     */
    public Question() {
    }

    /**
     * Constructor for Question class with 3 local variables.
     *
     * @param roomID   room where the question is asked.
     * @param username username of the person who asked the question.
     * @param question the question itself.
     */
    public Question(String roomID, String username, String question) {
        this.roomID = roomID;
        this.username = username;
        this.question = question;
    }

    /**
     * Constructor for Question class with 8 local variables.
     *
     * @param id        generated primary key for the table.
     * @param roomID    room where the question is asked.
     * @param username  username of the person who asked the question.
     * @param question  the question itself.
     * @param timeAsked the time the question was asked.
     * @param upvotes   the total amount of unique upvotes.
     * @param answered  boolean to see if a question is answered.
     * @param creatorIP IP-address of the creator of this question.
     */
    public Question(Long id, String roomID, String username, String question,
                    LocalDateTime timeAsked, int upvotes, boolean answered, String creatorIP) {
        this.id = id;
        this.roomID = roomID;
        this.username = username;
        this.question = question;
        this.timeAsked = timeAsked;
        this.upvotes = upvotes;
        this.answered = answered;
        this.creatorIP = creatorIP;
    }

    //--------------------------------------------
    // Getter section:
    //--------------------------------------------

    public Long getId() {
        return id;
    }

    public String getRoomID() {
        return roomID;
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
        return creatorIP;
    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

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
                && Objects.deepEquals(id, question1.id)
                && Objects.deepEquals(roomID, question1.roomID)
                && Objects.deepEquals(username, question1.username)
                && Objects.deepEquals(question, question1.question)
                && Objects.deepEquals(timeAsked, question1.timeAsked)
                && Objects.deepEquals(creatorIP, question1.creatorIP);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomID, username, question, timeAsked, upvotes, answered, creatorIP);
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

}
