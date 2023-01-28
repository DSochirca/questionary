package nl.tudelft.oopp.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Service;

@Entity
@Service
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "id")
    public String id;

    @Column(name = "access_token")
    public String accessToken;

    @Column(name = "starting_time")
    private LocalDateTime startingTime;

    @Column(name = "closed_by_mod")
    @JsonIgnore
    private boolean closedByMod = false;

    @Column(name = "answerA")
    @ElementCollection(fetch = FetchType.EAGER)
    private final List<Integer> answerList = new ArrayList<>();

    @Column(name = "answerSize")
    private int answerSize = 0;

    @Column(name = "isQuiz")
    private boolean isQuiz = false;

    @Column(name = "rightAnswer")
    private int rightAnswer;

    @ManyToOne
    private User creator;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "room_banned_ips", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "banned_ips")
    @JsonIgnore
    private Set<String> bannedIps = new HashSet<>();
    //for some reason these get stored between quotation marks ""


    /**
     * The isActive.
     * this is so that we can alter it within a runnable
     */

    volatile boolean isActive;

    /**
     * The three variables to keep track of what students think the speed of the lecture is.
     */
    @Column(name = "fast_votes")
    private int fastVotes = 0;

    @Column(name = "alright_votes")
    private int alrightVotes = 0;

    @Column(name = "slow_votes")
    private int slowVotes = 0;

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    /**
     * Empty constructor.
     */
    public Room() {
    }

    /**
     * Constructor for Room class with 1 local variable.
     *
     * @param id Unique identifier to be used in the database.
     */
    public Room(String id) {
        this.id = id;
    }

    /**
     * Constructor for Room class with 2 local variables.
     *
     * @param id          Unique identifier to be used in the database.
     * @param accessToken Token to grant moderator access.
     */
    public Room(String id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.startingTime = null;
    }

    /**
     * Constructor for Room class with 3 local variables.
     *
     * @param id           Unique identifier to be used in the database.
     * @param accessToken  Token to grant moderator access.
     * @param startingTime The time the room is scheduled for.
     */
    public Room(String id, String accessToken, LocalDateTime startingTime) {
        this.id = id;
        this.accessToken = accessToken;
        this.setStartingTime(startingTime);
    }

    /**
     * Constructor for Room class with 8 local variables.
     *
     * @param accessToken  Token to grant moderator access.
     * @param startingTime The time the room is scheduled for.
     * @param creator      The creator of the room.
     * @param bannedIps    The banned ips from the room.
     * @param isActive     True if the room is open.
     * @param fastVotes    How many people voted fast.
     * @param alrightVotes How many people voted alright.
     * @param slowVotes    How many people voted slow.
     */
    public Room(String accessToken, LocalDateTime startingTime, User creator, Set<String> bannedIps,
                boolean isActive, int fastVotes, int alrightVotes, int slowVotes) {
        this.accessToken = accessToken;
        this.startingTime = startingTime;
        this.creator = creator;
        this.bannedIps = bannedIps;
        this.isActive = isActive;
        this.fastVotes = fastVotes;
        this.alrightVotes = alrightVotes;
        this.slowVotes = slowVotes;
    }

    /**
     * Constructor for Room class with all variables.
     *
     * @param id           Unique identifier to be used in the database.
     * @param accessToken  Token to grant moderator access.
     * @param startingTime The time when the room opens.
     * @param creator      The creator of the room.
     * @param bannedIps    The banned ips from the room.
     * @param isActive     True if the room is open.
     * @param fastVotes    How many people voted fast.
     * @param alrightVotes How many people voted alright.
     * @param slowVotes    How many people voted slow.
     */
    public Room(String id, String accessToken, LocalDateTime startingTime, boolean closedByMod, User creator,
                Set<String> bannedIps, boolean isActive, int fastVotes, int alrightVotes, int slowVotes) {
        this.id = id;
        this.accessToken = accessToken;
        this.startingTime = startingTime;
        this.closedByMod = closedByMod;
        this.creator = creator;
        this.bannedIps = bannedIps;
        this.isActive = isActive;
        this.fastVotes = fastVotes;
        this.alrightVotes = alrightVotes;
        this.slowVotes = slowVotes;
    }

    //--------------------------------------------
    // Getter section:
    //--------------------------------------------

    public String getId() {
        return this.id;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public LocalDateTime getStartingTime() {
        return this.startingTime;
    }

    public User getCreator() {
        return this.creator;
    }

    public boolean isOngoing() {
        return this.isActive;
    }

    public boolean isClosedByMod() {
        return closedByMod;
    }

    /**
     * Adds vote.
     *
     * @param v vote type
     */
    public void addVote(char v) {
        if (v == 'f') {
            fastVotes++;
        } else if (v == 'a') {
            alrightVotes++;
        } else {
            slowVotes++;
        }
    }

    /**
     * Removes vote.
     *
     * @param v vote type
     */
    public void removeVote(char v) {
        if (v == 'f') {
            fastVotes--;
        } else if (v == 'a') {
            alrightVotes--;
        } else {
            slowVotes--;
        }
    }

    /**
     * Gets votes.
     *
     * @return list of votes
     */
    public List<Integer> getVotes() {
        List<Integer> list = new ArrayList<>();
        list.add(fastVotes);
        list.add(alrightVotes);
        list.add(slowVotes);
        return list;
    }

    public Set<String> getBannedIps() {
        return bannedIps;
    }

    public boolean checkQuiz() {
        return isQuiz;
    }

    public void setIsQuiz(boolean isQuiz) {
        this.isQuiz = isQuiz;
    }

    /**
     * get the results.
     *
     * @return returns a list with all the answers.
     */
    public List<Integer> getQuizResults() {
        return this.answerList;
    }

    /**
     * adds an answer.
     *
     * @param a the answer that is being added.
     */
    public void addQuizAnswer(int a) {
        if (this.checkQuiz() && (a <= answerSize)) {
            int b = answerList.get(a - 1) + 1;
            answerList.set(a - 1, b);
        }
    }

    /**
     * removes an answer.
     *
     * @param a the answer that is being removed.
     */
    public void removeQuizAnswer(int a) {
        if (this.checkQuiz() && (a <= answerSize)) {
            int b = answerList.get(a - 1) - 1;
            answerList.set(a - 1, b);
        }


    }

    public int getMaxSize() {
        return this.answerSize;
    }

    /**
     * toggles the quiz on or off.
     */
    public void toggleQuiz() {
        if (this.checkQuiz()) {
            this.isQuiz = false;
            this.answerList.clear();
        } else {
            this.isQuiz = true;
        }
    }

    /**
     * toggles the quiz on or off.
     *
     * @param a the size of the answer.
     */
    public void toggleQuiz(int a) {
        if (this.checkQuiz()) {
            this.isQuiz = false;
            this.answerList.clear();
            this.answerSize = 0;
        } else {
            this.isQuiz = true;
            this.answerSize = a;
            for (int i = 0; i < a; i++) {
                this.answerList.add(0);
            }
            System.out.println(this.answerList);
        }
    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

    public void setId(String id) {
        this.id = id;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public void setCreator(User u) {
        this.creator = u;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setBannedIps(Set<String> bannedIps) {
        this.bannedIps = bannedIps;
    }

    public void setClosedByMod(boolean closedByMod) {
        this.closedByMod = closedByMod;
    }

    public boolean getClosedByMod() {
        return this.closedByMod;
    }

    /**
     * gets the right answer.
     *
     * @return returns the right answer.
     */
    public int getRightAnswer() {
        return rightAnswer;
    }

    /**
     * sets the right answer.
     *
     * @param a the correct answer.
     */
    public void setRightAnswer(int a) {
        if (a > answerSize || a <= 0) {
            return;
        }
        this.rightAnswer = a;
    }

    //--------------------------------------------
    // Equals, hashCode, toString methods section:
    //--------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return isActive == room.isActive
                && id.equals(room.id)
                && accessToken.equals(room.accessToken)
                && startingTime.equals(room.startingTime)
                && creator.equals(room.creator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accessToken, startingTime, creator, isActive);
    }

    @Override
    public String toString() {
        return "Room{"
                + "id='" + id + '\''
                + ", accessToken='" + accessToken + '\''
                + ", startingTime=" + startingTime
                + ", creator=" + creator
                + ", isActive=" + isActive
                + '}';
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    public void addBannedIp(String ip) {
        this.bannedIps.add(ip);
    }
}
