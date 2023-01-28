package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByCurrentRoomId(String currentRoomId);

    List<User> findAllByCurrentRoomIdAndUserType(String currentRoomId, String userType); //?

    User findByUsernameAndIp(String username, String ip);

    //-------------------------------------------------------
    String value = "UPDATE User u SET u.currentRoomId = ?2 WHERE u.id = ?1";

    @Transactional
    @Modifying
    @Query(value)
    void updateUserRoom(Long id, String roomID);

    //-------------------------------------------------------
    /*
    String value1 = "UPDATE User u SET u.userType = ?3 WHERE u.username = ?1 AND u.ip = ?2";

    @Transactional
    @Modifying
    @Query(value1)
    void updateUserType(String username, String ip, String newType);
     */
    //-------------------------------------------------------
    String value1 = "UPDATE User u SET u.userType = ?2 WHERE u.id = ?1";

    @Transactional
    @Modifying
    @Query(value1)
    void updateUserType(Long userId, String type);
    //-------------------------------------------------------
}
