package nl.tudelft.oopp.demo.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;


class ServerCommunicationTest {

    private static ClientAndServer mockServer;
    private static User user;
    private static String idToken;
    private static String roomId;
    private static String modToken;
    private static LocalDateTime dateTime;

    private static Question q1;
    private static List<Question> questionList;

    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(8080);
        //System.out.println(mockServer.isRunning());
        user = new User("user01", "127.0.0.1", "lecturer");
        user.setId(1L);
        idToken = "ABCDEFGHI-XYZ12345";
        roomId = "ABCDEFGHI";
        modToken = "XYZ12345";
        dateTime = LocalDateTime.now();

        q1 = new Question(1L, roomId, "Bill Gates", "Hello World?",
                dateTime, 4, false, "127.0.0.1");
        Question q2 = new Question(2L, roomId, "Elon Musk", "What's the homework?",
                dateTime.plusSeconds(5L), 100000, true, "127.0.0.1");
        questionList = List.of(q2, q1);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    void submitUser() {
        createExpectationForSubmitUser();
        Long actual = ServerCommunication.submitUser(user);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/submitUser")
                        .withQueryStringParameter("username", "user01")
                        .withQueryStringParameter("usertype", "lecturer"),
                VerificationTimes.exactly(1)
        );

        assertEquals(1, actual);
    }

    @Test
    void generateCodes() {
        createExpectationForGenerateCodes();
        String actual = ServerCommunication.generateCodes();
        String expected = idToken;

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/generateRoomCode"),
                VerificationTimes.exactly(1)
        );

        assertEquals(expected, actual);
    }

    @Test
    void createRoom() throws JsonProcessingException {
        ServerCommunication.createRoom(idToken, dateTime, user);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("PUT")
                        .withPath("/createRoom/" + idToken)
                        .withQueryStringParameter("time", dateTime.format(DateTimeFormatter.ISO_DATE_TIME))
                        .withBody(mapper.writeValueAsString(user)),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void joinRoom() {
        createExpectationForJoinRoom();
        String actual = ServerCommunication.joinRoom(user.getId(), roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/joinRoom")
                        .withQueryStringParameter("userId", "1")
                        .withQueryStringParameter("roomId", roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals("success", actual);
    }

    @Test
    void modJoinRoom() throws JsonProcessingException {
        createExpectationForModJoinRoom();
        boolean actual = ServerCommunication.modJoinRoom(roomId, modToken, user);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("POST")
                        .withPath("/modJoinRoom/" + idToken)
                        .withBody(mapper.writeValueAsString(user)),
                VerificationTimes.exactly(1)
        );

        assertTrue(actual);
    }

    @Test
    void getQuestions() throws JsonProcessingException {
        createExpectationForGetQuestions();
        List<Question> actual = ServerCommunication.getQuestions(roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/getQuestions/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(questionList, actual);
    }

    @Test
    void closeRoom() {
        createExpectationForCloseRoom();
        boolean actual = ServerCommunication.closeRoom(roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/closeRoom/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertTrue(actual);
    }

    @Test
    void export() throws JsonProcessingException {
        createExpectationForExport();
        List<Question> actual = ServerCommunication.export(roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/export/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(questionList, actual);
    }

    @Test
    void addQuestion() throws JsonProcessingException {
        createExpectationForAddQuestion();
        String actual = ServerCommunication.addQuestion(q1);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("POST")
                        .withPath("/addQuestion")
                        .withBody(mapper.writeValueAsString(q1)),
                VerificationTimes.exactly(1)
        );

        assertEquals("", actual);
    }

    @Test
    void editQuestion() {
        ServerCommunication.editQuestion(q1, "Goodbye");

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/editQuestionAsStudent")
                        .withQueryStringParameter("questionid", "1")
                        .withQueryStringParameter("newquestion", "Goodbye"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void markAsAnswered() {
        ServerCommunication.markAsAnswered(q1.getId());

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/setAnswered/1"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void leaveRoom() {
        ServerCommunication.leaveRoom(user.getId());

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/leaveRoom/1"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void addUpvote() {
        ServerCommunication.addUpvote(1L, 1L);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/upvote")
                        .withQueryStringParameter("questionid", "1")
                        .withQueryStringParameter("userId", "1"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void getIdOfUser() {
        createExpectationForGetIdOfUser();
        Long actual = ServerCommunication.getIdOfUser(user.getUsername(), user.getip());

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/getId")
                        .withQueryStringParameter("username", user.getUsername())
                        .withQueryStringParameter("Ip", user.getip()),
                VerificationTimes.exactly(1)
        );

        assertEquals(1L, actual);
    }

    @Test
    void deleteQuestionStudent() {
        ServerCommunication.deleteQuestionStudent(q1.getId(), user.getId());

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/deleteQuestionAsStudent/" + q1.getId())
                        .withQueryStringParameter("userId", "1"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void addSpeedVote() {
        ServerCommunication.addSpeedVote(roomId, 'f');

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/speedLecture/" + roomId)
                        .withQueryStringParameter("speed", "f"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void removeSpeedVote() {
        ServerCommunication.removeSpeedVote(roomId, 'f');

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/speedLectureRemove/" + roomId)
                        .withQueryStringParameter("speed", "f"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void getSpeedLecture() throws JsonProcessingException {
        createExpectationForGetSpeedLecture();
        List<Integer> actual = ServerCommunication.getSpeedLecture(roomId);
        List<Integer> expected = List.of(1, 2, 3, 4, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/speedLectureGet/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(expected, actual);
    }

    @Test
    void banIpAddress() {
        ServerCommunication.banIpAddress(roomId, user.getip());

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("POST")
                        .withPath("/banIp/" + roomId)
                        .withBody(user.getip()),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void checkQuiz() throws JsonProcessingException {
        createExpectationForCheckQuiz();
        List<Object> actual = ServerCommunication.checkQuiz(roomId);
        List<Object> expected = List.of(true, 1);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/checkQuiz/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(expected, actual);
    }

    @Test
    void getQuizStatus() {
        createExpectationForGetQuizStatus();
        boolean actual = ServerCommunication.getQuizStatus(roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/getCurrentStatusQuiz/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertTrue(actual);
    }

    @Test
    void toggleQuiz() {
        ServerCommunication.toggleQuiz(roomId, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/toggleQuiz/" + roomId)
                        .withQueryStringParameter("max", "5"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void addQuizAnswer() {
        ServerCommunication.addQuizAnswer(roomId, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/quizAnswer/" + roomId)
                        .withQueryStringParameter("answer", "5"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void removeQuizAnswer() {
        ServerCommunication.removeQuizAnswer(roomId, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/removeQuizAnswer/" + roomId)
                        .withQueryStringParameter("answer", "5"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void getQuizAnswers() throws JsonProcessingException {
        createExpectationForGetQuizAnswers();
        List<Integer> actual = ServerCommunication.getQuizAnswers(roomId);
        List<Integer> expected = List.of(1, 2, 3, 4, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/quizAnswersGet/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(expected, actual);
    }

    @Test
    void updateQuestions() throws JsonProcessingException {
        createExpectationForUpdateQuestions();
        List<Question> actual = ServerCommunication.updateQuestions(roomId);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/checkQuestions/" + roomId),
                VerificationTimes.exactly(1)
        );

        assertEquals(questionList, actual);
    }

    @Test
    void setRightAnswer() {
        ServerCommunication.setRightAnswer(roomId, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/setAnswer/" + roomId)
                        .withQueryStringParameter("answer", "5"),
                VerificationTimes.exactly(1)
        );
    }

    @Test
    void checkAnswerCorrect() {
        createExpectationForCheckAnswerCorrect();
        boolean actual = ServerCommunication.checkAnswerCorrect(roomId, 5);

        new MockServerClient("localhost", 8080).verify(
                request()
                        .withMethod("GET")
                        .withPath("/checkAnswer/" + roomId)
                        .withQueryStringParameter("answer", "5"),
                VerificationTimes.exactly(1)
        );

        assertTrue(actual);
    }

    //--------------------------------------------------------------
    //                    EXPECTATION METHODS:
    //--------------------------------------------------------------


    private void createExpectationForSubmitUser() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/submitUser")
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                )
                                .withQueryStringParameter("username", user.getUsername())
                                .withQueryStringParameter("usertype", user.getUserType()),
                        //.withBody(exact("{username: 'foo', password: 'bar'}")),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("1")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGenerateCodes() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/generateRoomCode"),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(idToken)
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForJoinRoom() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/joinRoom")
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                )
                                .withQueryStringParameter("userId", "1")
                                .withQueryStringParameter("roomId", roomId),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("success")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForModJoinRoom() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("POST")
                                .withPath("/modJoinRoom/" + idToken)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                )
                                .withBody(mapper.writeValueAsString(user)),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("true")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGetQuestions() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/getQuestions/" + roomId),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(questionList))
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForCloseRoom() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/closeRoom/" + roomId),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("true")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForExport() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/export/" + roomId),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(questionList))
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForAddQuestion() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("POST")
                                .withPath("/addQuestion")
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                )
                                .withBody(mapper.writeValueAsString(q1)),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGetIdOfUser() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/getId")
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                )
                                .withQueryStringParameter("username", user.getUsername())
                                .withQueryStringParameter("Ip", user.getip()),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("1")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGetSpeedLecture() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/speedLectureGet/" + roomId)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                ),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(List.of(1, 2, 3, 4, 5)))
                                .withDelay(TimeUnit.SECONDS, 1)
            );

    }

    private void createExpectationForCheckQuiz() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/checkQuiz/" + roomId)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                ),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(List.of("true", 1)))
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGetQuizStatus() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/getCurrentStatusQuiz/" + roomId)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                ),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("true")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForGetQuizAnswers() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/quizAnswersGet/" + roomId)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                ),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(List.of(1, 2, 3, 4, 5)))
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForUpdateQuestions() throws JsonProcessingException {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/checkQuestions/" + roomId),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody(mapper.writeValueAsString(questionList))
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void createExpectationForCheckAnswerCorrect() {
        new MockServerClient("localhost", 8080)
                .when(
                        new HttpRequest()
                                .withMethod("GET")
                                .withPath("/checkAnswer/" + roomId)
                                .withHeaders(
                                        new Header("accept-charset", "UTF-8"),
                                        new Header("Content-type", "application/json")
                                ),
                        exactly(1)
                )
                .respond(
                        new HttpResponse()
                                .withStatusCode(HttpStatusCode.OK_200.code())
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("true")
                                .withDelay(TimeUnit.SECONDS, 1)
            );
    }
}