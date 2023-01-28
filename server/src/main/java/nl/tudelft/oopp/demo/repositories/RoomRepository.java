package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Room;
import nl.tudelft.oopp.demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, String> {

    Room findByIdEquals(String id);

    List<Room> findAllByIsActive(boolean isActive);

    Room findByAccessTokenEquals(String token);

    List<Room> findByCreatorEquals(User u);

    @Transactional
    void deleteRoomById(String id);

    String value = "UPDATE Room r SET r.isActive = false, r.closedByMod = true WHERE r.id = ?1";
    @Transactional
    @Modifying
    @Query(value)
    void setInactive(String id);

    String value2 = "UPDATE Room r SET r.isActive = true, r.closedByMod = false WHERE r.id = ?1";
    @Transactional
    @Modifying
    @Query(value2)
    void setActive(String id);
}