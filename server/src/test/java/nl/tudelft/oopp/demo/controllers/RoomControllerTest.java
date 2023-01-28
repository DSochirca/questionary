package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

@WebMvcTest(RoomControllerTest.class)
class RoomControllerTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoomController roomController = new RoomController();
    @InjectMocks
    private UserController userController = new UserController();

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    private Long userId;
    private String roomId;
    private String roomId2;
    private String idToken;
    private String startTime;
    private Set<String> bannedIps;
    private User user;
    private Room room;
    private Room r1;
    private Room r2;

    @BeforeEach
    void setUp() {

        userId = 654321L;
        roomId = "ABCDEF1234";
        roomId2 = "XYZWASD123";
        idToken = "ABCDEF1234-token";

        startTime = "2018-07-14T17:45:55.9483536";
        bannedIps = new HashSet<>();

        user = new User(userId, "user01", "127.0.0.1", false, roomId, "student");
        room = new Room(roomId, "token", LocalDateTime.parse(startTime, formatter));
        room.setActive(false);
        room.setCreator(user);

        r1 = new Room(roomId, "token", LocalDateTime.parse(startTime, formatter), false, user, bannedIps, true, 5, 5, 5);
        r2 = new Room(roomId2, "token", LocalDateTime.parse(startTime, formatter), true, user, bannedIps, false, 5, 5, 5);
    }

    /**
     * Test for creating a room.
     */
    @Test
    void createRoom() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/createRoom");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        roomController.createRoom(idToken, startTime, user, request);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for joining a room, when starting time already passed.
     */
    @Test
    void joinRoomShouldStart() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.joinRoom(userId, roomId);

        verify(roomRepository).setActive(roomId);
        verify(userRepository).updateUserRoom(userId, roomId);
        assertEquals("success", actual);
    }

    /**
     * Test for joining a room that isn't opened yet.
     */
    @Test
    void joinRoomNotPossible() {
        room.setStartingTime(null);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.joinRoom(userId, roomId);

        assertEquals("This room isn't opened yet.", actual);
    }

    /**
     * Test for joining a room that is closed.
     */
    @Test
    void joinRoomClosed() {
        room.setClosedByMod(true);
        room.setActive(false);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.joinRoom(userId, roomId);

        assertEquals("This room is closed.", actual);
    }

    /**
     * Test for joining a room as a moderator.
     */
    @Test
    void modJoinRoom() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/createRoom");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.modJoinRoom(idToken, user, request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(roomRepository).setActive(roomId);

        assertEquals("true", actual);
        assertEquals(user, userArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for joining a room as a moderator with invalid room id.
     */
    @Test
    void modJoinRoomInvalidRoomId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/createRoom");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(null);

        String actual = roomController.modJoinRoom(idToken, user, request);

        assertEquals("false", actual);
    }

    /**
     * Test for joining a room as a moderator with invalid token.
     */
    @Test
    void modJoinRoomInvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/createRoom");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.modJoinRoom("1234", user, request);

        assertEquals("false", actual);
    }

    /**
     * Test for successfully leave Room.
     */
    @Test
    void leaveRoomTrueTest() {
        roomController.leaveRoom(user.getId());

        verify(userRepository, times(1)).updateUserRoom(user.getId(), null);
    }

    /**
     * Test set lecture as inactive.
     */
    @Test
    void deleteLecture() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);
        roomController.deleteLecture(roomId);

        verify(roomRepository).setInactive(roomId);
    }

    /**
     * Test set lecture as inactive with room id not found.
     */
    @Test
    void deleteLectureFailed() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(null);
        String actual = roomController.deleteLecture(roomId);

        assertEquals("false", actual);
    }

    /**
     * delete lecture when lecture is active.
     */
    @Test
    void deleteLectureIsActiveTest() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);
        room.setActive(false);
        roomController.deleteLecture(roomId);
        String actual = roomController.deleteLecture(roomId);

        assertEquals("true", actual);
    }

    /**
     * system.err.println testing in deleteLecture method.
     */
    @Test
    void deleteLectureThrowExceptionTest() {

        room = null;

        assertEquals("false", roomController.deleteLecture(roomId).toString());

    }




    /**
     * Test for joining a room as a moderator after it ended.
     */
    @Test
    void joinRoomAfterEnding() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/createRoom");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.joinRoomAfterEnding(idToken, user, request);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());

        assertEquals(user, userArgumentCaptor.getValue());  //verify if the controller stored the desired room
        assertEquals("true", actual);
    }

    /**
     * Test for joining a room as a moderator with invalid room id.
     */
    @Test
    void joinRoomAfterEndingInvalidRoomId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(null);

        String actual = roomController.joinRoomAfterEnding(idToken, user, request);

        assertEquals("false", actual);
    }


    /**
     * Test for joining a room as a moderator with invalid token.
     */
    @Test
    void joinRoomAfterEndingInvalidToken() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.joinRoomAfterEnding("1234", user, request);

        assertEquals("false", actual);
    }

    /**
     * Test for getting the list of rooms that this user has created.
     */
    @Test
    void getCurrentLectures() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(userRepository.findByUsernameAndIp(user.getUsername(), request.getRemoteAddr())).thenReturn(user);
        when(roomRepository.findByCreatorEquals(user)).thenReturn(new ArrayList<Room>());

        List<Room> actual = roomController.getCurrentLectures(user.getUsername(), request);

        assertEquals(new ArrayList<Room>(), actual);
    }

    /**
     * Test for adding a vote to the speed of the lecture.
     */
    @Test
    void setSpeedLecture() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.setSpeedLecture(roomId, 'f');
        room.addVote('f');

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for removing a vote from the speed of the lecture.
     */
    @Test
    void removeSpeedLecture() {
        room.addVote('f');
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.removeSpeedLecture(roomId, 'f');
        room.removeVote('f');

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for getting the answers of the quiz.
     */
    @Test
    void getQuizAnswers() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        List<Integer> actual = roomController.getQuizAnswers(roomId);

        assertEquals(new ArrayList<Integer>(), actual);
    }

    /**
     * Test for setting the answers of the quiz.
     */
    @Test
    void setQuizAnswer() {
        room.toggleQuiz(4);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.setQuizAnswer(roomId, 1);
        room.addQuizAnswer(1);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for removing an answer of the quiz.
     */
    @Test
    void removeQuizAnswer() {
        room.toggleQuiz(4);
        room.addQuizAnswer(1);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.removeQuizAnswer(roomId, 1);
        room.removeQuizAnswer(1);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for toggling a quiz.
     */
    @Test
    void toggleQuiz() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.toggleQuiz(roomId, 4);
        room.toggleQuiz(4);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

    /**
     * Test for checking the status of a quiz.
     */
    @Test
    void getCurrentStatusQuizTrue() {
        room.toggleQuiz(4);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.getCurrentStatusQuiz(roomId);

        assertEquals("true", actual);
    }

    /**
     * Test for checking the status of a quiz.
     */
    @Test
    void getCurrentStatusQuizFalse() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        String actual = roomController.getCurrentStatusQuiz(roomId);

        assertEquals("false", actual);
    }

    /**
     * Test for setting the right answer of the quiz.
     */
    @Test
    void setAnswer() {
        room.toggleQuiz(4);
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        roomController.setAnswer(roomId, 1);
        room.setRightAnswer(1);

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());

        assertEquals(room, roomArgumentCaptor.getValue());  //verify if the controller stored the desired room
    }

}