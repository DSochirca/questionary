package nl.tudelft.oopp.demo.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import nl.tudelft.oopp.demo.entities.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    Optional<Question> findById(Long questionId);

    List<Question> findAllByRoomIdEquals(String roomId);

    Question findByIdEquals(Long id);

    //timeAsked gets prioritized
    List<Question> findAllByRoomIdEqualsOrderByTimeAskedAscUpvotesDesc(String roomId); //?

    List<Question> findAllByRoomIdEqualsOrderByTimeAsked(String roomId); //?

    //-------------------------------------------------------
    //quotation marks "" are not being recognized within query so new variable is necessary
    //this method must be executed within a transaction (hence transactional)
    //for spring to perform update/delete queries we need to include modifying
    String value1 = "UPDATE Question u SET u.question = ?2 WHERE u.id = ?1";

    @Transactional
    @Modifying
    @Query(value1)
    void editQuestion(Long questionId, String newQuestion);
    //-------------------------------------------------------

    String value2 = "UPDATE Question u SET u.answered = true WHERE u.id = ?1";

    @Transactional
    @Modifying
    @Query(value2)
    void markAsAnswered(Long questionId);
    //-------------------------------------------------------

    String value3 = "DELETE Question u WHERE u.roomId = ?1 AND u.creatorIp = ?2";

    @Transactional
    @Modifying
    @Query(value3)
    void removeQuestionsFromIp(String roomId, String creatorIp);
    //-------------------------------------------------------

    void deleteById(Long questionId);

}
