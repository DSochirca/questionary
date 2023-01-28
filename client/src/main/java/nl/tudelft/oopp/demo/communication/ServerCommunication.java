package nl.tudelft.oopp.demo.communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.oopp.demo.data.Question;
import nl.tudelft.oopp.demo.data.User;

/**
 * The type Server communication.
 */
public class ServerCommunication {

    private static final HttpClient client = HttpClient.newBuilder().build();
    //private static Gson gson = new Gson();
    private static final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    // some path variables can contain + or ' ' or '/'
    // so character escaped encoding is necessary sometimes
    // apparently the constructor with more than one parameter of the uri class escapes all the characters very nicely
    //https://stackoverflow.com/questions/724043/http-url-address-encoding-in-java


    /**
     * Submit the user to the server and retrieve the UserId.
     *
     * @return the UserId
     */
    public static Long submitUser(User user) {

        URI uri = null;
        try {
            uri = new URI(
                    "http",
                    "localhost:8080",
                    "/submitUser",
                    "username=" + user.getUsername() + "&usertype=" + user.getUserType(),
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        Long userId = null;
        try {
            userId = mapper.readValue(response.body(), Long.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userId;
    }


    /**
     * Generate unique string codes for the Rooms.
     *
     * @return the code of the room
     */
    public static String generateCodes() {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/generateRoomCode")).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }


    /**
     * Create room.
     *
     * @param idtoken  the id of the room
     * @param dateTime the date and time the room was created
     * @param u        the user
     */
    public static void createRoom(String idtoken, LocalDateTime dateTime, User u) {
        //String room = id + "-" + token;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        //String requestBody = gson.toJson(u);

        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(u);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String time = ""; //if time is null
        if (dateTime != null) {
            time = dateTime.format(formatter);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/createRoom/" + idtoken + "?time=" + time))
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Join room.
     *
     * @param userId the id of the user
     * @param id     the id of the room
     * @return String with the error message or "success" if successful.
     */
    public static String joinRoom(Long userId, String id) {
        URI uri = null;
        try {
            uri = new URI(
                    "http",
                    "localhost:8080",
                    "/joinRoom",
                    "userId=" + userId + "&roomId=" + id,
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "error joining room";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return response.body();
    }

    /**
     * Mod join room.
     *
     * @param id    the id of the room
     * @param token the privilege access token
     * @param u     the user
     */
    public static boolean modJoinRoom(String id, String token, User u) {
        String room = id + "-" + token;
        //String requestBody = gson.toJson(u);
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(u);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        //Content-Type: application/json; utf-8
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/modJoinRoom/" + room))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        if (response.body().equals("true")) {
            return true;
        }
        return false;
    }


    /**
     * Gets questions.
     *
     * @param roomID the room id
     * @return the list of questions
     */
    public static List<Question> getQuestions(String roomID) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/getQuestions/" + roomID)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        //return gson.fromJson(response.body(), new TypeToken<List<Question>>() {}.getType());

        List<Question> l;
        try {
            l = mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return l;
    }


    /**
     * close the room so no one except a mod or lecture can join.
     *
     * @param roomID the room that needs to be closed
     * @return true if the room has been closed, false otherwise
     */
    public static boolean closeRoom(String roomID) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/closeRoom/" + roomID)).build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        if (response.body().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * export the questions ordered on time asked.
     *
     * @param roomID the id of the room from which the questions will be exported
     * @return the list of questions to be exported
     */
    public static List<Question> export(String roomID) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/export/" + roomID)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        List<Question> l = null;
        try {
            l = mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * Add question.
     *
     * @param q the question that is added
     */
    public static String addQuestion(Question q) {
        String requestBody = null;
        try {
            requestBody = mapper.writeValueAsString(q);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        //Content-Type: application/json; utf-8
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/addQuestion"))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return response.body(); //will be "banned" if user is banned
    }

    /**
     * Edit question.
     *
     * @param q           the question that is edited
     * @param newQuestion the new question
     */
    public static void editQuestion(Question q, String newQuestion) {
        URI uri = null;
        try {
            uri = new URI(
                    "http",
                    "localhost:8080",
                    "/editQuestionAsStudent",
                    "questionid=" + q.getId() + "&newquestion=" + newQuestion,
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "text/plain")
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mark a question as answered.
     *
     * @param questionId The id of the question.
     */
    public static void markAsAnswered(Long questionId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/setAnswered/" + questionId))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "text/plain")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Leave room.
     *
     * @param userId the id of the user that left
     */
    public static void leaveRoom(Long userId) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/leaveRoom/" + userId))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "text/plain")
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add upvote.
     *
     * @param questionId The question's id.
     * @param userId     The user's id.
     */
    public static void addUpvote(Long questionId, Long userId) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/upvote?questionId=" + questionId + "&userId=" + userId))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Get the ID of a user by username and IP (necessary in comparisons for setting deleteIcon).
     *
     * @param username the name of the user being compared
     * @param ip       the ip of the user being compared
     * @return the Id of user
     */
    public static Long getIdOfUser(String username, String ip) {
        URI uri = null;
        try {
            uri = new URI(
                    "http",
                    "localhost:8080",
                    "/getId",
                    "username=" + username + "&Ip=" + ip,
                    null);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        try {
            return Long.parseLong(response.body());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * delete a question as a student.
     *
     * @param questionId the id of the question to be deleted
     * @param userId     the id of the user trying to delete the question
     */
    public static void deleteQuestionStudent(Long questionId, Long userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/deleteQuestionAsStudent/" + questionId + "?userId=" + userId))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }


    /**
     * Vote on speed of lecture.
     *
     * @param id the id of the user
     * @param v  the vote of the user
     */
    public static void addSpeedVote(String id, char v) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/speedLecture/" + id + "?speed=" + v))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Remove vote on speed of lecture.
     *
     * @param id the id of the user
     * @param v  the vote of the user
     */

    public static void removeSpeedVote(String id, char v) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/speedLectureRemove/" + id + "?speed=" + v))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Get the speed of the lecture.
     *
     * @param id the id of the room
     * @return the list containing number of votes for each selection type
     */

    public static List<Integer> getSpeedLecture(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/speedLectureGet/" + id))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        List<Integer> votes = null;
        try {
            if (response.body() != null) {
                votes = mapper.readValue(response.body(), new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return votes;
    }

    /**
     * Bans the ip address from entering the room.
     *
     * @param roomId    The id of the room.
     * @param ipAddress The ip address of the user.
     */
    public static void banIpAddress(String roomId, String ipAddress) {

        //Content-Type: application/json; utf-8
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/banIp/" + roomId))
                .POST(HttpRequest.BodyPublishers.ofString(ipAddress))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * get the current status of the quiz.
     *
     * @param id the id of the room.
     * @return return true if there is a quiz false if there is no quiz.
     */
    public static List<Object> checkQuiz(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/checkQuiz/" + id))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        List<Object> result = new ArrayList<>();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            result.set(0, false);
            return result;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        try {
            if (response.body() != null) {
                result = mapper.readValue(response.body(), new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        if (result.get(0).equals("true")) {
            result.set(0, true);
        } else {
            result.set(0, false);
        }
        return result;
    }

    /**
     * get the current status of the quiz.
     *
     * @param id the id of the room.
     * @return return true if quiz is going on and false if there is no quiz.
     */
    public static boolean getQuizStatus(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/getCurrentStatusQuiz/" + id))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        if (response.body().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * create or end a quiz.
     *
     * @param id the id of the room.
     */
    public static void toggleQuiz(String id, int maxVal) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/toggleQuiz/" + id + "?max=" + maxVal))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * add an answer to the quiz.
     *
     * @param id the id of the room.
     * @param a  the answer.
     */
    public static void addQuizAnswer(String id, int a) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/quizAnswer/" + id + "?answer=" + a))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * remove an answer from the quiz.
     *
     * @param id the id of the room.
     * @param a  the answer tha is being removed.
     */
    public static void removeQuizAnswer(String id, int a) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/removeQuizAnswer/" + id + "?answer=" + a))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * get the quiz answers.
     *
     * @param id the id of the room.
     * @return a list of answers.
     */
    public static List<Integer> getQuizAnswers(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/quizAnswersGet/" + id))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        List<Integer> answers = null;
        try {
            answers = mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return answers;
    }

    /**
     * update the questions list.
     *
     * @param id the id of the room.
     * @return list of questions.
     */
    public static List<Question> updateQuestions(String id) {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/checkQuestions/" + id)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        List<Question> l = null;
        try {
            if (response.body() != null) {
                l = mapper.readValue(response.body(), new TypeReference<>() {
                });
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * sets the right answer.
     *
     * @param id the id of the room.
     * @param a  the right answer.
     */
    public static void setRightAnswer(String id, int a) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/setAnswer/" + id + "?answer=" + a))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * checks if the answer is correct.
     *
     * @param id the id of the room.
     * @param a  the correct answer.
     * @return returns true if the answer was correct otherwise it returns false.
     */
    public static boolean checkAnswerCorrect(String id, int a) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/checkAnswer/" + id + "?answer=" + a))
                .setHeader("accept-charset", "UTF-8")
                .setHeader("Content-type", "application/json")
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        if (response.body().equals("true")) {
            return true;
        } else {
            return false;
        }
    }

}
