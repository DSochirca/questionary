package nl.tudelft.oopp.demo.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Question;
import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
import nl.tudelft.oopp.demo.repositories.QuestionRepository;
import nl.tudelft.oopp.demo.repositories.RoomRepository;
import nl.tudelft.oopp.demo.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;


/**
 * The type Question controller.
 */
@Transactional
@RestController
public class QuestionController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RoomRepository roomRepository;

    private static final File f = new File("server/src/main/resources/log.txt");


    /**
     * GET Endpoint to retrieve all questions in a specific lecture room.
     *
     * @param id The id of the room.
     * @return List of questions in that room.
     */
    @GetMapping("getQuestions/{id}")
    @ResponseBody
    public List<Question> getQuestions(@PathVariable String id) { //query as a String
        return questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(id);
    }

    /**
     * GET Endpoint to retrieve all questions in a specific lecture room.
     *
     * @param id The id of the room.
     * @return List of questions in that room.
     */
    @GetMapping("getQuestion/{id}")
    @ResponseBody
    public Question getQuestion(@PathVariable Long id) { //query as a String
        return questionRepository.findByIdEquals(id);
    }

    /**
     * GET the generated room code.
     * Accessed when the client wants to create a new room
     *
     * @return the room code + the access token
     */
    @GetMapping("generateRoomCode")
    @ResponseBody
    public String generateClass() {
        int leftLimit = 48;     // numeral '0'
        int rightLimit = 122;   // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        //generate room id
        String id = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        //generate access token
        String token = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        //if the id is already in use then we need a new id
        while (roomRepository.findByIdEquals(id) != null) {
            id = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }

        //if the token is already in use then we need a new token
        while (roomRepository.findByAccessTokenEquals(token) != null) {
            token = random.ints(leftLimit, rightLimit + 1)
                    .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        }
        //id and token sent together as Strings, separated by '-' !
        return id + "-" + token;
    }

    /**
     * POST (submit) the question to the database.
     *
     * @param q       The question asked by a user.
     * @param request The request received by the server (used for getting the remote ip address).
     * @return the string
     */
    @PostMapping("addQuestion")
    public String addQuestion(@RequestBody Question q, HttpServletRequest request) {
        Room r = roomRepository.findByIdEquals(q.getRoomID());

        if (r.getBannedIps().contains(request.getRemoteAddr())) {
            return "banned";
        }

        if (r.isClosedByMod() || !r.isOngoing()) {
            return "closed";
        }

        Question newq = new Question(q.getRoomID(), q.getUsername(), q.getQuestion(),
                LocalDateTime.now(), request.getRemoteAddr());
        questionRepository.save(newq);
        writeToFile(newq);
        return "";
    }


    /**
     * POST (submit) the upvote to the database.
     *
     * @param questionId The question id.
     * @param userId     The user id.
     */
    @RequestMapping("upvote")
    public boolean upvoteQuestion(@RequestParam(value = "questionId") Long questionId,
                                  @RequestParam(value = "userId") Long userId) {
        //Upvote newu = new Upvote(u.getParentQuestion(), u.getUsername(), request.getRemoteAddr());

        Optional<Question> question = questionRepository.findById(questionId);
        Optional<User> user = userRepository.findById(userId);
        Question q;
        User u;

        if (question.isEmpty() || user.isEmpty()) {
            return false;
        } else {
            q = question.get();
            u = user.get();
        }

        //entities handle it if the question is already upvoted:
        //no, the question didn't
        q.addUpvote(u);
        u.addQuestionUpvoted(q);

        questionRepository.save(q);
        userRepository.save(u);
        return true;
    }

    /**
     * Mark the question as answered.
     *
     * @param id The question id.
     */
    @RequestMapping("setAnswered/{id}")
    public void markQuestionAsAnswered(@PathVariable Long id) {
        questionRepository.markAsAnswered(id);
    }


    /**
     * EDIT the question as a moderator (in this case it's edit question as the student who asked the question,
     * would need another endpoint for mod since here I'm checking whether
     * the request IP matches the IP of the question asker).
     *
     * @param questionId The id of the question.
     * @param content    The new content that should be inserted.
     */
    @RequestMapping("editQuestionAsStudent")
    public void editTheQuestion(@RequestParam(value = "questionid") Long questionId,
                                @RequestParam(value = "newquestion") String content) {
        if (questionRepository.findById(questionId).isPresent()) {
            questionRepository.editQuestion(questionId, content);
        }
    }

    //      NOT USED:
    //    /**
    //     * Set the starting time of a room.
    //     *
    //     * @param id        The id of the room who's starting date should be changed
    //     * @param timeStart The starting time that the room should have after being formatted by DateTimeFormatter.ISO_DATE_TIME
    //     * @param username  The username of the lecturer (presumably) to verify if this user has privileges to edit this room
    //     * @param request   The request to get the IP and verify the request is legitimate
    //     */
    //    @PostMapping("setDate/{id}")
    //    public void setDate(@PathVariable String id, @RequestParam(value = "startingTime") String timeStart,
    //                        @RequestParam(value = "username") String username, HttpServletRequest request) {
    //        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;  //so that we can parse the startingTime
    //        LocalDateTime newTimeStart = LocalDateTime.parse(timeStart, formatter); //
    //        if (userRepository.findByUsernameAndIp(username, request.getRemoteAddr()) == null) {
    //            return; //do something in the future if this user hasn't been created in server side yet
    //        }
    //        if (roomRepository.findByIdEquals(id) == null) {
    //            return; //do something in the future if trying to change a non-existent room
    //        }
    //        List<Room> roomsByUser = roomRepository.findByCreatorEquals(
    //                userRepository.findByUsernameAndIp(username, request.getRemoteAddr()));
    //        //get the list of rooms created by user to check this room is one of them
    //
    //        for (Room r : roomsByUser) {
    //            if (roomRepository.findByIdEquals(id).equals(r)) {
    //                roomRepository.findByIdEquals(id).setStartingTime(newTimeStart);
    //
    //                return;
    //            }
    //        }
    //    }

    /**
     * Delete question.
     *
     * @param id     the question id
     * @param userId the user id
     */
    @GetMapping("deleteQuestionAsStudent/{id}")
    public void deleteQuestion(@PathVariable Long id,
                               @RequestParam(value = "userId") Long userId) {
        try {
            Optional<Question> question = questionRepository.findById(id);
            Optional<User> user = userRepository.findById(userId);
            Question q;
            User u;

            q = question.get();
            u = user.get();
            if (userRepository.findByUsernameAndIp(q.getUsername(), q.getCreatorIP()).getId().equals(userId)
                    || u.getUserType().equals("lecturer")
                    || u.getUserType().equals("moderator")) {
                questionRepository.deleteById(id);
            }
        } catch (Exception e) {
            System.out.println("Question or User is empty");
        }

    }

    /**
     * Export list.
     *
     * @param id the id
     * @return list of all the questions
     */
    @GetMapping("export/{id}")
    @ResponseBody
    public List<Question> export(@PathVariable String id) {
        return questionRepository.findAllByRoomIdEqualsOrderByTimeAsked(id);
    }

    /**
     * Writes question to the file log.txt.
     * File will contain questions asked with timestamps, username, and ip addresses.
     *
     * @param q The question logged to the file.
     */
    public static void writeToFile(Question q) {
        boolean existsFlag = f.exists();

        if (!existsFlag) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Could not create new log file.");
                e.printStackTrace();
            }
        }

        FileWriter writer;
        try {
            writer = new FileWriter(f, true);
            BufferedWriter out = new BufferedWriter(writer);
            out.write("[" + q.getTimeAsked() + "] "
                    + "[" + q.getRoomID() + "] "
                    + "[" + q.getCreatorIP() + "] "
                    + "[" + q.getUsername() + "]  " + q.getQuestion() + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Could not write to the file.");
            e.printStackTrace();
        }
    }


    /**
     * checks for questions.
     *
     * @param id the id of the room.
     * @return a list of questions.
     */
    @GetMapping("checkQuestions/{id}")
    public DeferredResult<List<Question>> checkQuestions(@PathVariable String id) {
        List<Question> current = questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(id);
        Long high;
        if (current.isEmpty()) {
            high = -1L;
        } else {
            Question currentMax = Collections.max(current, Comparator.comparing(s -> s.getId()));
            high = currentMax.getId();
        }
        Long timeOut = 200000L;
        DeferredResult<List<Question>> output = new DeferredResult<>(timeOut);
        ForkJoinPool.commonPool().submit(() -> {
            try {
                while (true) {
                    List<Question> newCurrent = questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(id);
                    Long newHigh;
                    if (newCurrent.isEmpty()) {
                        newHigh = -1L;
                    } else {
                        Question newCurrentMax = Collections.max(newCurrent, Comparator.comparing(s -> s.getId()));
                        newHigh = newCurrentMax.getId();
                    }
                    if (newHigh != high || newCurrent.size() != current.size()) {
                        output.setResult(questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(id));
                        break;
                    }
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        output.onTimeout(() -> {
            output.setResult(questionRepository.findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(id));
        });

        return output;

    }

}
