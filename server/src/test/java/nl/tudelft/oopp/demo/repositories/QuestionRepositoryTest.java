package nl.tudelft.oopp.demo.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import nl.tudelft.oopp.demo.entities.Question;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    private QuestionRepository underTest;

    //we need this because when executing a modifying query jpa still gets the entity
    //from the cached version of the database, so we need to call entityManager.clear()
    @PersistenceContext
    EntityManager entityManager;

    private Question question1;
    private Question question2;
    private Question question3;
    private Question question4;

    @BeforeEach
    void setup() {
        // Hibernate sets IDs before storing entities, so questionX entity/object will get an id
        // when underTest.save gets executed

        question1 = new Question("ABCDEF1234", "Bill Gates", "Hello World?",
                LocalDateTime.now(), 4, false, "127.0.0.1");
        underTest.save(question1);
        //------------------------
        question2 = new Question("ABCDEF1234", "Elon Musk", "What's the homework?",
                LocalDateTime.now().plusSeconds(5L), 100000, true, "127.0.0.1");
        underTest.save(question2);
        //------------------------
        //2 questions asked at the same time:
        //------------------------
        LocalDateTime time = LocalDateTime.now();
        question3 = new Question("P8fLH7xxRl", "Bill Gates", "Hello World?",
                time, 4, false, "127.0.0.1");
        underTest.save(question3);
        //------------------------
        question4 = new Question("P8fLH7xxRl", "Elon Musk", "What's the homework?",
                time, 100000, true, "127.0.0.1");
        underTest.save(question4);
        //------------------------
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();  //this is so the database doesn't get loaded with duplicates
    }


    /**
     * Test for getting question by its id (the default method returns an optional).
     */
    @Test
    void findQuestionByIdWhichReturnsOptional() {
        Optional<Question> q = underTest.findById(question1.getId());

        Question actual = null;
        if (q.isPresent()) {
            actual = q.get();
        }

        //System.out.println(underTest.findAll());
        assertEquals(question1, actual);
    }

    /**
     * Test for getting question by its id (the default method returns an optional).
     * Test for an invalid id.
     */
    @Test
    void findQuestionByIdWhichReturnsOptionalNotExistent() {
        Optional<Question> q = underTest.findById(-1L);

        Question actual = null;
        if (q.isPresent()) {
            actual = q.get();
        }

        //System.out.println(underTest.findAll());
        assertNull(actual);
    }

    /**
     * Test for getting all question by their room id.
     */
    @Test
    void findAllQuestionsByRoomId() {
        List<Question> expected = List.of(question1, question2);
        List<Question> actual = underTest.findAllByRoomIdEquals("ABCDEF1234");

        assertEquals(expected, actual);
    }

    /**
     * Test for getting all question by their room id.
     * Test for an non existent room id.
     */
    @Test
    void findAllQuestionsByRoomIdInvalid() {
        List<Question> expected = List.of();        //empty list
        List<Question> actual = underTest.findAllByRoomIdEquals("0000000000");

        assertEquals(expected, actual);
    }

    /**
     * Test for getting a question by its id.
     */
    @Test
    void findQuestionByIdEquals() {
        Question actual = underTest.findByIdEquals(question2.getId());

        assertEquals(question2, actual);
    }

    /**
     * Test for getting a question by its id.
     * Test for an invalid id.
     */
    @Test
    void findQuestionByIdEqualsNonExistentId() {
        Question actual = underTest.findByIdEquals(-1L);

        assertNull(actual);
    }

    /**
     * Test for getting all questions in a room sorted by relevance.
     */
    @Test
    void findAllQuestionsByRoomIdSortByRelevance() {
        List<Question> expected = List.of(question1, question2);
        List<Question> actual = underTest.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc("ABCDEF1234");

        assertEquals(expected, actual);
    }

    /**
     * Test for getting all questions in a room sorted by relevance.
     * Test for 2 questions asked at the same time.
     * Should appear sorted by upvotes.
     */
    @Test
    void findAllQuestionsByRoomIdWithSameTimeAskedSortedByRelevance() {
        List<Question> expected = List.of(question4, question3);
        List<Question> actual = underTest.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc("P8fLH7xxRl");

        assertEquals(expected, actual);
    }

    /**
     * Test for getting all questions in a room sorted by the time they were asked.
     */
    @Test
    void findAllByRoomIdSortByTimeAsked() {
        List<Question> expected = List.of(question1, question2);
        List<Question> actual = underTest.findAllByRoomIdEqualsOrderByTimeAsked("ABCDEF1234");

        assertEquals(expected, actual);
    }

    /**
     * Test for editing a question by its id.
     */
    @Test
    void editQuestionById() {
        underTest.editQuestion(question1.getId(), "Hello to you too?");
        entityManager.clear();  //resets the cache
        //JPA just get the entity from the cache not from database
        //and because the method is modifying, the cache needs to be reset

        //for some reason the localdatetime in the database gets approximated, so after editing a question,
        //the new time will have an error of a couple milliseconds

        Question expected = new Question(question1.getId(), question1.getRoomID(), question1.getUsername(),
                "Hello to you too?", underTest.findByIdEquals(question1.getId()).getTimeAsked(), question1.getUpvotes(),
                question1.isAnswered(), question1.getCreatorIP());
        Question actual = underTest.findByIdEquals(question1.getId());

        assertEquals(expected, actual);
    }

    /**
     * Test for marking a question as answered.
     */
    @Test
    void markAsAnsweredById() {
        underTest.markAsAnswered(question1.getId());
        entityManager.clear();

        //again, the localdatetime in the database gets approximated, so after upvoting a question,
        //the new time will have an error of a couple milliseconds

        Question expected = new Question(question1.getId(), question1.getRoomID(), question1.getUsername(),
                question1.getQuestion(), underTest.findByIdEquals(question1.getId()).getTimeAsked(), question1.getUpvotes(),
                true, question1.getCreatorIP());
        Question actual = underTest.findByIdEquals(question1.getId());

        assertEquals(expected, actual);
    }

    /**
     * Test for marking a question as answered.
     * Test for a question that is already answered.
     */
    @Test
    void markAsAnsweredByIdWhichIsAlreadyAnswered() {
        underTest.markAsAnswered(question2.getId());
        entityManager.clear();

        //again, the localdatetime in the database gets approximated, so after upvoting a question,
        //the new time will have an error of a couple milliseconds

        Question expected = new Question(question2.getId(), question2.getRoomID(), question2.getUsername(),
                question2.getQuestion(), underTest.findByIdEquals(question2.getId()).getTimeAsked(), question2.getUpvotes(),
                question2.isAnswered(), question2.getCreatorIP());
        Question actual = underTest.findByIdEquals(question2.getId());

        assertEquals(expected, actual);
    }

    /**
     * Test for removing all questions which have a particular ip address.
     */
    @Test
    void removeAllQuestionsFromTheSameIp() {
        underTest.removeQuestionsFromIp(question1.getRoomID(), question1.getCreatorIP());

        //the list retrieved should be empty
        List<Question> expected = new LinkedList<>();
        List<Question> actual = underTest.findAllByRoomIdEquals(question1.getRoomID());

        assertEquals(expected, actual);
    }

    /**
     * Test for deleting a question by its id.
     */
    @Test
    void deleteQuestionById() {
        underTest.deleteById(question1.getId());

        Question actual = underTest.findByIdEquals(question1.getId());

        assertNull(actual);
    }
}