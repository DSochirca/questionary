package nl.tudelft.oopp.demo.controllers;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RestController;

/**
 * The type User controller.
 */
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private RoomRepository roomRepository;

    /**
     * POST (Submit) the user to the database, or change user's type.
     *
     * @param username The user's name.
     * @param usertype The role of the user.
     * @param request  The request received by the server (used for getting the remote ip address)
     * @return the id of the user
     */
    @RequestMapping("submitUser")
    public Long submitUser(@RequestParam(value = "username") String username,
                           @RequestParam(value = "usertype") String usertype, HttpServletRequest request) {

        String ip = request.getRemoteAddr();    //get the ip for the user

        User userStored = userRepository.findByUsernameAndIp(username, ip);

        if (userStored != null) {
            //user already exists, so maybe he wants to change type
            if (!usertype.equals(userStored.getUserType())) {
                userRepository.updateUserType(userStored.getId(), usertype);
            }
            return userStored.getId();      //return the assigned id
        } else {
            User user = new User(username, ip, usertype);
            userRepository.save(user);

            return userRepository.findByUsernameAndIp(username, ip).getId(); //return the assigned id
        }
    }

    /**
     * POST Ban the ip address from a specific room.
     *
     * @param roomId    The id of the room.
     * @param ipAddress The ip address which will get banned.
     */
    @PostMapping("banIp/{roomId}")
    public void banIpAddress(@PathVariable String roomId, @RequestBody String ipAddress) {
        Room r = roomRepository.findByIdEquals(roomId);

        //System.out.println("Banned: " + roomId + "  " + ipAddress);

        r.addBannedIp(ipAddress);
        roomRepository.save(r);
        questionRepository.removeQuestionsFromIp(roomId, ipAddress);
    }

    @GetMapping("getId")
    public Long getIdOfStudent(@RequestParam(value = "username") String username,
                               @RequestParam(value = "Ip") String ip) {
        return userRepository.findByUsernameAndIp(username, ip).getId();
    }
}
