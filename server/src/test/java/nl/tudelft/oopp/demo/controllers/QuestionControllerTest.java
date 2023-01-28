package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.async.DeferredResult;


@ExtendWith(MockitoExtension.class)
public class QuestionControllerTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private QuestionController questionController = new QuestionController();

    private Question q1;
    private Question q2;
    private LocalDateTime time = LocalDateTime.parse("2021-04-19T18:35:08");
    private Long id1 = 123457L;
    private Long id2 = 135795L;
    private Room room;
    private Long userId = 654321L;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        room = new Room("g");
        user = new User(userId, "username", "127.0.0.1", false, "g", "moderator");
        q1 = new Question(id1, "g", "username", "what?", time, "127.0.0.1");
        q2 = new Question(id2, "g", "pog", "hello?", time, "IP2");
    }

    /**
     * test for getting all the questions in a lecture room.
     */

    @Test
    void getAllQuestionsTest() {
        List<Question> list = new ArrayList<>(List.of(q1, q2));

        when(questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc("g")).thenReturn(list);
        Iterable<Question> actual = questionController.getQuestions("g");

        assertEquals(actual, list);
    }

    @Test
    void randomCodeGenerator() {
        String string = null;
        string = questionController.generateClass();
        Scanner sc = new Scanner(string);
        sc.useDelimiter("-");
        String id = sc.next();
        String token = sc.next();
        assertNotNull(id);
        assertNotNull(token);
    }

    /**
     * test for getting a single question in a lecture room.
     */

    @Test
    void getSingleQuestionTest() {

        when(questionRepository.findByIdEquals(q1.getId())).thenReturn(q1);

        assertEquals(q1, questionController.getQuestion(id1));
    }

    /**
     * test for adding a new question.
     */
    @Test
    void addQuestionTest() {
        Room r = new Room(q1.getRoomID());
        r.setActive(true);
        when(roomRepository.findByIdEquals(q1.getRoomID())).thenReturn(r);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/addQuestion");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        assertEquals("", questionController.addQuestion(q1, request));
    }

    /**
     * test for adding a new question.
     */
    @Test
    void addQuestionTestRoomClosed() {
        when(roomRepository.findByIdEquals(q1.getRoomID())).thenReturn(new Room(q1.getRoomID()));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/addQuestion");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        assertEquals("closed", questionController.addQuestion(q1, request));
    }

    /**
     * test for upvoting a question.
     */
    @Test
    void upvoteQuestionTest() {
        Room r = new Room(q1.getRoomID());
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        questionController.upvoteQuestion(q1.getId(), userId);


        //for assertEquals, to check if repositories actually added the upvote:
        user.addQuestionUpvoted(q1);
        q1.addUpvote(user);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Question> questionArgumentCaptor = ArgumentCaptor.forClass(Question.class);

        verify(questionRepository).save(questionArgumentCaptor.capture());
        verify(userRepository).save(userArgumentCaptor.capture());

        Question capturedQuestion = questionArgumentCaptor.getValue();
        User capturedUser = userArgumentCaptor.getValue();

        assertEquals(q1, capturedQuestion);
        assertEquals(user, capturedUser);

        //remove upvote after assertion:
        user.addQuestionUpvoted(q1);
        q1.addUpvote(user);
    }

    /**
     * test for upvoting a question with empty user and question.
     */
    @Test
    void upvoteQuestionTestEmptyUserQuestion() {
        Room r = new Room(q1.getRoomID());


        Question newq = new Question(q1.getRoomID(), q1.getUsername(), q2.getQuestion(), LocalDateTime.now(), "127.0.0.1");
        Long l = 21312312L;
        assertFalse(questionController.upvoteQuestion(l,user.getId()));
    }

    /**
     * test for marking the question as answered.
     */

    @Test
    void markAsAnsweredTest() {
        questionController.markQuestionAsAnswered(q1.getId());

        verify(questionRepository).markAsAnswered(q1.getId());
    }

    /**
     * successfully edits question test.
     */
    @Test
    void editQuestionTrueTest() {
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));

        questionController.editTheQuestion(q1.getId(), "new question here");

        verify(questionRepository, times(1)).editQuestion(q1.getId(), "new question here");
    }

    /**
     * testing the deletion of a null question.
     */
    @Test
    void deleteQuestionNullTest() {

        when(questionRepository.findByIdEquals(q1.getId())).thenReturn(null);

        assertNull(questionController.getQuestion(id1));
    }

    /**
     * throws exception when deleting non-existing question.
     */
    @Test
    void deleteQuestionFalseQuestionTest() {
        q1 = null;

        assertThrows(Exception.class, () -> {
            questionController.deleteQuestion(q1.getId(), user.getId());
        });

    }

    /**
     * export method test for all questions.
     */
    @Test
    void exportTrueTest() {
        List<Question> list = new ArrayList<>(List.of(q1, q2));
        room = new Room("g");
        when(questionRepository.findAllByRoomIdEqualsOrderByTimeAsked("g")).thenReturn(list);

        assertEquals(list, questionController.export(room.getId()));
    }

    /**
     * make sure that export function is accurate.
     */
    @Test
    void exportNotEqualsTest() {
        List<Question> list = new ArrayList<>(List.of(q1));
        room = new Room("g");
        q2.setRoomID("h");

        List<Question> actual = questionController.export("h");

        assertNotEquals(list, actual);
    }

    /**
     * test for deleting a question.
     */
    @Test
    void deleteQuestionTrueQuestionTest() {
        Long userId = 654321L;
        User u = new User(userId, "username", q1.getCreatorIP(), false, q1.getRoomID(), "moderator");

        when(questionRepository.findById(q1.getId())).thenReturn(Optional.of(q1));
        when(userRepository.findById(userId)).thenReturn(Optional.of(u));
        when(userRepository.findByUsernameAndIp(q1.getUsername(), q1.getCreatorIP())).thenReturn(u);

        questionController.deleteQuestion(q1.getId(), u.getId());

        verify(questionRepository, times(1)).deleteById(q1.getId());
    }

    /**
     * trying to add a question from a banned IP address.
     */
    @Test
    void addSingleQuestionBannedTest() {

        Room r = new Room(q1.getRoomID());
        Set<String> bannedIps = new HashSet<String>();
        bannedIps.add("127.0.0.1");
        r.setBannedIps(bannedIps);

        when(roomRepository.findByIdEquals(q1.getRoomID())).thenReturn(r);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/addQuestion");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        assertEquals("banned", questionController.addQuestion(q1, request));
    }

    /**
     * falsely edit a question where the question is not in the database.
     */
    @Test
    void editQuestionFalseTest() {
        //this is when the question is not in the repository:
        when(questionRepository.findById(q1.getId())).thenReturn(Optional.empty()); //not in the repository

        questionController.editTheQuestion(q1.getId(), "something");

        verify(questionRepository, never()).editQuestion(q1.getId(), "something");
    }

    /**
     * test check for questions.
     */
    @Test
    void checkQuestionsTest() {
        when(questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(room.getId()))
                .thenReturn(new ArrayList<Question>());

        DeferredResult<List<Question>> expected = new DeferredResult<>(200000L);
        expected.setResult(new ArrayList<Question>());

        assertNotEquals(expected, questionController.checkQuestions(room.getId()));
    }
}