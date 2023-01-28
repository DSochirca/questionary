package nl.tudelft.oopp.demo.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;



class UserControllerTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private UserController userController = new UserController();

    private Question question;
    private Room room;
    private User user;
    private String roomId;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);

        question = new Question(1L, "ABCDEF1234", "Bill Gates", "Hello World?",
                LocalDateTime.now(), 4, false, "127.0.0.1");
        roomId = "ABCDEF1234";
        room = new Room("ABCDEF1234");
        user = new User(1L, "Bill Gates", "127.0.0.1", false, roomId, "moderator");
    }

    /**
     * test for submitting a new user.
     */
    @Test
    void submitUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/submitUser");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        //this has 2 calls in the submit method:
        when(userRepository.findByUsernameAndIp(user.getUsername(), user.getIP())).thenAnswer(new Answer() {
            private int count = 0;

            public User answer(InvocationOnMock invocation) {
                if (count++ == 1) {
                    return null;    //first call returns null
                }
                return user;    //2nd call returns the user stored
            }
        });

        Long actual = userController.submitUser(user.getUsername(), user.getUserType(), request);

        assertEquals(1L, actual);
    }

    /**
     * test for submitting an user that already exists.
     */
    @Test
    void submitUserAlreadyExists() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(HttpHeaders.HOST, "http://localhost:8080/submitUser");
        request.setLocalPort(8080);
        request.setRemoteAddr("127.0.0.1");

        when(userRepository.findByUsernameAndIp(user.getUsername(), user.getIP())).thenReturn(user);

        Long id = userController.submitUser(user.getUsername(), "newType", request);

        verify(userRepository).updateUserType(user.getId(), "newType");
        assertEquals(1L, id);
    }

    /**
     * test for banning an ip address from a room.
     */
    @Test
    void banIpAddress() {
        when(roomRepository.findByIdEquals(roomId)).thenReturn(room);

        userController.banIpAddress(roomId, user.getIP());
        room.addBannedIp(user.getIP());

        ArgumentCaptor<Room> roomArgumentCaptor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(roomArgumentCaptor.capture());
        verify(questionRepository).removeQuestionsFromIp(roomId, user.getIP());

        assertEquals(room, roomArgumentCaptor.getValue());
    }

    /**
     * test for getting the id of a student.
     */
    @Test
    void getIdOfStudent() {
        when(userRepository.findByUsernameAndIp(user.getUsername(), user.getIP())).thenReturn(user);
        Long actual = userController.getIdOfStudent(user.getUsername(), user.getIP());

        verify(userRepository).findByUsernameAndIp(user.getUsername(), user.getIP());
        assertEquals(user.getId(), actual);
    }
}