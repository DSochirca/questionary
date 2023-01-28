package nl.tudelft.oopp.demo.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;
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
 * The type Room controller.
 */
@Transactional
@RestController
public class RoomController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    private static final File f = new File("server/src/main/resources/log.txt");

    /**
     * Accessed when the create room button has been pressed.
     * Creates a new room in the database with the room codes
     * generated previously
     *
     * @param id           The id-token String
     * @param startingTime The time the room is scheduled at.
     * @param u            The user that created the new room.
     * @param request      The request received by the server (used for getting the remote ip address)
     */
    @RequestMapping("createRoom/{id}")
    public void createRoom(@PathVariable String id, @RequestParam(value = "time") String startingTime,
                           @RequestBody User u, HttpServletRequest request) {   //query as a String

        //System.out.println(id + "  " + startingTime + " " + u);

        // using a scanner to remove the '-' delimeter and separate the 2 codes from the String:
        Room r;
        Scanner sc = new Scanner(id);
        sc.useDelimiter("-");

        id = sc.next();     //id of the room

        String token;  //in case the token hasn't been sent for some reason
        if (sc.hasNext()) {
            token = sc.next(); // mods connect with token as well
        } else {
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;  //so that we can parse the startingTime

        //a room may be created without startingTime, this is fine, an endpoint for changing/adding startingTime will be made
        if (startingTime == null || startingTime.equals("")) {
            r = new Room(id, token);
        } else {
            //With starting time:
            r = new Room(id, token, LocalDateTime.parse(startingTime, formatter));
        }

        //This should be made compulsory in the future:
        if (u.getUsername() == null) { //no username field in UI
            u.setUsername("testusername");
        }
        //User u = new User(lecturerUsername, request.getRemoteAddr(), "lecturer");
        u.setIP(request.getRemoteAddr());   //store the ip of the lecturer in their user entity
        r.setCreator(u); //store the creator of this room to know who has privileges to edit the room startingTime...
        userRepository.save(u); //must save user before room since the room is dependent on the user entity (creator)
        roomRepository.save(r);
        writeToFile(r);
    }

    /**
     * POST (Submit) the room in which the client wants to join.
     * Adds the student to the user repository
     * as well as the Student list in the room repository.
     *
     * @param userId The id of the room.
     * @param roomId The user entity that wants to join a room.
     * @return the string
     */
    @RequestMapping("joinRoom")
    public String joinRoom(@RequestParam(value = "userId") Long userId,
                           @RequestParam(value = "roomId") String roomId) {

        try {
            Room r = roomRepository.findByIdEquals(roomId);

            if (r == null) {
                return "invalid id";
            }

            //sets scheduled rooms to active if time already passed
            //System.out.println(r.getStartingTime());

            if (!r.isOngoing() && r.getStartingTime() != null
                    && r.getStartingTime().isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    && !r.isClosedByMod()) {
                r.setActive(true);
                roomRepository.setActive(r.getId());
            }

            //this doesn't work atm, because when you create room with date and time fields null,
            // the time is set to now, which doesn't pass this check, but it should
            if ((r.getStartingTime() == null && !r.isOngoing() && !r.isClosedByMod())
                    || (r.getStartingTime() != null && !r.isOngoing()
                    && r.getStartingTime().isAfter(LocalDateTime.now()))) {
                return "This room isn't opened yet.";
            }

            if (!r.isOngoing()) {
                return "This room is closed.";
            }

            //user already exists and room exists (he wants to change rooms)
            userRepository.updateUserRoom(userId, roomId);

        } catch (Exception e) {
            System.out.println("room is either closed or isn't opened yet");
        }
        return "success";
    }


    /**
     * NOT SURE what this one is about.
     * WHY not a universal (just plain joinroom) path? student join room only requires id, while mod and lec also require token
     * we could have a check for id length in joinroom and deal with connection as student or mod/lec accordingly, i just thought
     * for now this would be simpler
     *
     * @param id      the id
     * @param m       the m
     * @param request the request
     */
    @PostMapping("modJoinRoom/{id}")
    public String modJoinRoom(@PathVariable String id, @RequestBody User m, HttpServletRequest request) {

        Scanner sc = new Scanner(id);
        sc.useDelimiter("-");
        id = sc.next();
        String token;
        if (sc.hasNext()) {
            token = sc.next(); // mods connect with token as well
        } else {
            return "false";
        }

        Room r = roomRepository.findByIdEquals(id);

        if (r == null) {
            System.err.println("enter a valid room ID");
            return "false"; // we'll have something else to show in the future for wrong room connects
        }

        if (!r.getAccessToken().equals(token)) {
            return "false";
        }

        if (m.getUsername() == null) {
            m.setUsername("moderatortest"); // no username field in ui
        }

        if (!r.isClosedByMod()) {
            roomRepository.setActive(id);
        }
        m.setCurrentRoomID(id);
        m.setIP(request.getRemoteAddr());
        userRepository.save(m);
        return "true";
    }

    /**
     * Leave room.
     *
     * @param userId the id of the user
     */
    @RequestMapping("leaveRoom/{userId}")
    public void leaveRoom(@PathVariable Long userId) {
        userRepository.updateUserRoom(userId, null);
    }

    /**
     * SHOULD NOT DELETE THE ROOM FROM THE REPOSITORY!.
     * JUST MARK IT AS CLOSED (via the isActive attribute)
     * because moderators should be still able to enter it after it has been closed
     *
     * @param id The id of the room that should be closed.
     */
    @GetMapping("closeRoom/{id}")
    @ResponseBody
    public String deleteLecture(@PathVariable String id) {
        try {
            Room toBeInactive = roomRepository.findByIdEquals(id);

            if (toBeInactive == null) {
                return "false";
            }

            toBeInactive.setActive(false);
            roomRepository.setInactive(id);

            if (toBeInactive.isOngoing()) {
                return "false";
            }
            return "true";
        } catch (Exception e) {
            System.err.println("false");
        }
        return "true";
    }

    /**
     * allows mod to join a lecture even after set inactive.
     *
     * @param id      the id of the user
     * @param m       the user (moderator type)
     * @param request the HTTP request
     * @return true/false/error
     */
    @PostMapping("joinRoomAfterEnding/{id}")
    public String joinRoomAfterEnding(@PathVariable String id, User m, HttpServletRequest request) {
        Scanner sc = new Scanner(id);
        sc.useDelimiter("-");
        id = sc.next();
        String token;
        if (sc.hasNext()) {
            token = sc.next(); // mods connect with token as well
        } else {
            return "false";
        }
        if (roomRepository.findByIdEquals(id) == null) {
            System.err.println("enter a valid room ID");
            return "false"; // we'll have something else to show in the future for wrong room connects
        }
        if (!roomRepository.findByIdEquals(id).getAccessToken().equals(token)) {
            return "false";
        }
        if (m.getUsername() == null) {
            m.setUsername("moderatortest"); // no username field in ui
        }

        m.setCurrentRoomID(id);
        m.setIP(request.getRemoteAddr());
        userRepository.save(m);
        return "true";

    }

    /**
     * Return the list of rooms that this user has created (that they can edit, meaning that have not happened yet).
     *
     * @param username username of the lecturer who wants to see their currently inactive rooms
     * @param request  request to get the IP and be able to get the user
     * @return returns a list with the rooms that this user has created which are scheduled to happen in the future
     */
    @GetMapping("getCurrentLectures/{username}")
    public List<Room> getCurrentLectures(@PathVariable String username, HttpServletRequest request) {
        List<Room> currentRooms = roomRepository.findByCreatorEquals(
                userRepository.findByUsernameAndIp(username, request.getRemoteAddr()));
        //Now need to only return rooms that are currently inactive and that have not happened before now
        List<Room> currentlyInactiveRooms = new ArrayList<>();
        for (Room r : currentRooms) {
            if (!r.isOngoing() && LocalDateTime.now().isBefore(r.getStartingTime())) {
                currentlyInactiveRooms.add(r);
            }
        }
        return currentlyInactiveRooms;
    }

    /**
     * Adds a vote to the speed of the lecture.
     *
     * @param id room Id
     * @param v  vote (either s for slow a for alright and f for fast)
     */
    @GetMapping("speedLecture/{id}")
    public void setSpeedLecture(@PathVariable String id,
                                @RequestParam(value = "speed") char v) {
        if (v != 's' && v != 'a' && v != 'f') {
            return;
        }
        Room r = roomRepository.findByIdEquals(id);
        r.addVote(v);
        roomRepository.save(r);
    }

    /**
     * Removes a vote from the speed of the lecture (whenever a student changes speed vote).
     *
     * @param id room Id
     * @param v  vote (either s for slow a for alright and f for fast)
     */
    @GetMapping("speedLectureRemove/{id}")
    public void removeSpeedLecture(@PathVariable String id,
                                   @RequestParam(value = "speed") char v) {
        if (v != 's' && v != 'a' && v != 'f') {
            return;
        }
        Room r = roomRepository.findByIdEquals(id);
        r.removeVote(v);
        roomRepository.save(r);
    }

    /**
     * Returns the current votes for the speed of the lecture.
     *
     * @param id room Id
     * @return the speed of the lecture in the form of an ArrayList {fastVotes, alrightVotes, slowVotes}
     */
    @GetMapping("speedLectureGet/{id}")
    public DeferredResult<List<Integer>> getLectureSpeed(@PathVariable String id) {
        Long timeOut = 200000L;
        DeferredResult<List<Integer>> output = new DeferredResult<>(timeOut);
        List<Integer> current = roomRepository.findByIdEquals(id).getVotes();
        ForkJoinPool.commonPool().submit(() -> {
            try {
                while (true) {
                    if (!current.equals(roomRepository.findByIdEquals(id).getVotes())) {
                        output.setResult(roomRepository.findByIdEquals(id).getVotes());
                        break;
                    }
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        output.onTimeout(() -> {
            output.setResult(roomRepository.findByIdEquals(id).getVotes());
        });

        return output;
    }

    /**
     * Writes room created to the file log.txt.
     * File will contain rooms created with timestamps, moderator names + ips and their access tokens.
     *
     * @param r The room logged to the file.
     */
    public static void writeToFile(Room r) {
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
            out.write("[" + LocalDateTime.now() + "] "
                    + "[" + r.getCreator().getIP() + "] "
                    + "[" + r.getCreator().getUsername() + "] "
                    + " Created room "
                    + "[" + r.getId() + "-" + r.getAccessToken() + "]  " + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Could not write to the file.");
            e.printStackTrace();
        }
    }

    /**
     * gets the answers of the quiz.
     *
     * @param id the id of the room.
     * @return a list with the answers.
     */
    @GetMapping("quizAnswersGet/{id}")
    public List<Integer> getQuizAnswers(@PathVariable String id) {
        Room r = roomRepository.findByIdEquals(id);
        return r.getQuizResults();
    }

    /**
     * sets an answers for the quiz.
     *
     * @param id the id of the room.
     * @param a  the answer that is being set.
     */
    @GetMapping("quizAnswer/{id}")
    public void setQuizAnswer(@PathVariable String id,
                              @RequestParam(value = "answer") int a) {
        Room r = roomRepository.findByIdEquals(id);
        if (a <= 0 || a > r.getMaxSize()) {
            return;
        }
        r.addQuizAnswer(a);
        roomRepository.save(r);
    }

    /**
     * removes a quiz answer.
     *
     * @param id the id of the room.
     * @param a  the answer to be removed.
     */
    @GetMapping("removeQuizAnswer/{id}")
    public void removeQuizAnswer(@PathVariable String id,
                                 @RequestParam(value = "answer") int a) {
        Room r = roomRepository.findByIdEquals(id);
        if (a <= 0 || a > r.getMaxSize()) {
            return;
        }
        r.removeQuizAnswer(a);
        roomRepository.save(r);
    }

    /**
     * to turn on or off the quiz.
     *
     * @param id the id of the room.
     */
    @GetMapping("toggleQuiz/{id}")
    public void toggleQuiz(@PathVariable String id,
                           @RequestParam(value = "max") int a) {
        Room r = roomRepository.findByIdEquals(id);
        if (a > 0) {
            r.toggleQuiz(a);
        } else {
            r.toggleQuiz();
        }
        roomRepository.save(r);
    }

    /**
     * checks if a quiz is active.
     *
     * @param id the id of the room.
     * @return returns true if the quiz iis active false otherwise
     */
    @GetMapping("checkQuiz/{id}")
    public DeferredResult<List<Object>> checkQuiz(@PathVariable String id) {
        //Room r = roomRepository.findByIdEquals(id);
        Long timeOut = 200000L;
        DeferredResult<List<Object>> output = new DeferredResult<List<Object>>(timeOut);
        List<Object> temp = new ArrayList<Object>();
        boolean current = roomRepository.findByIdEquals(id).checkQuiz();
        ForkJoinPool.commonPool().submit(() -> {
            try {
                while (true) {
                    if (current != roomRepository.findByIdEquals(id).checkQuiz()) {
                        if (roomRepository.findByIdEquals(id).checkQuiz()) {
                            temp.add(0, "true");
                            temp.add(1, roomRepository.findByIdEquals(id).getMaxSize());
                            output.setResult(temp);
                            break;
                        } else {
                            temp.add(0, "false");
                            temp.add(1, 0);
                            output.setResult(temp);
                            break;
                        }
                    }
                    TimeUnit.SECONDS.sleep(5);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        output.onTimeout(() -> {
            if (current) {
                temp.add(0, "true");
                output.setResult(temp);
            } else {
                temp.add(0, "false");
                temp.add(1, 0);
                output.setResult(temp);
            }
        });

        return output;
    }

    /**
     * get the current status of the quiz.
     *
     * @param id the id of the room.
     * @return returns true or false in correlation to the status of the quiz.
     */
    @GetMapping("getCurrentStatusQuiz/{id}")
    public String getCurrentStatusQuiz(@PathVariable String id) {
        Room r = roomRepository.findByIdEquals(id);
        if (r.checkQuiz()) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * sets an answer for the quiz.
     *
     * @param id the id of the room.
     * @param a  the answer that is to be set.
     */
    @GetMapping("setAnswer/{id}")
    public void setAnswer(@PathVariable String id,
                          @RequestParam(value = "answer") int a) {
        Room r = roomRepository.findByIdEquals(id);
        r.setRightAnswer(a);
        roomRepository.save(r);
    }

    /**
     * checks the answer.
     *
     * @param id the id of the room.
     * @param a  the answer that is checked.
     * @return returns true if answer is correct false otherwise.
     */
    @GetMapping("checkAnswer/{id}")
    public DeferredResult<String> checkAnswer(@PathVariable String id,
                                              @RequestParam(value = "answer") int a) {
        Room r = roomRepository.findByIdEquals(id);
        int ans = r.getRightAnswer();
        Long timeOut = 200000L;
        DeferredResult<String> output = new DeferredResult<>(timeOut);
        if (ans <= 0 || ans > r.getMaxSize()) {
            ForkJoinPool.commonPool().submit(() -> {
                try {
                    while (true) {
                        if (roomRepository.findByIdEquals(id).getRightAnswer() > 0
                                && roomRepository.findByIdEquals(id).getRightAnswer() <= r.getMaxSize()) {
                            if (roomRepository.findByIdEquals(id).getRightAnswer() == a) {
                                output.setResult("true");
                                break;
                            } else {
                                output.setResult("false");
                                break;
                            }
                        }
                        TimeUnit.SECONDS.sleep(5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            if (ans == a) {
                output.setResult("true");
            } else {
                output.setResult("false");
            }
        }

        output.onTimeout(() -> {
            if (ans == a) {
                output.setResult("true");
            } else {
                output.setResult("false");
            }
        });

        return output;
    }

}
