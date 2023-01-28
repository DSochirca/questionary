package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

class RoomTest {

    private Room room;
    private LocalDateTime startingTime;
    private User creator;
    private Set<String> bannedIps;

    @BeforeEach
    void setup() {
        startingTime = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
        creator = new User(1L, "John Doe", "2001:0db8:0000:0000:0000:8a2e:0370:7334", false,
                "abc", "student");
        bannedIps = new HashSet<>();
        bannedIps.add("0000:0000:0000:0000:0000:0000:0000:0000");
    }

    //--------------------------------------------
    // Constructor section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for constructors")
    class ConstructorTests {
        @Test
        @DisplayName("Test empty constructor")
        @Tag("unit")
        void testEmptyConstructor() {
            room = new Room();
            assertNotNull(room);
        }

        @Test
        @DisplayName("Test constructor with one local variable")
        @Tag("unit")
        void testConstructorOne() {
            room = new Room("abc");
            assertNotNull(room);
        }

        @Test
        @DisplayName("Test constructor with two local variables")
        @Tag("unit")
        void testConstructorTwo() {
            room = new Room("abc", "def");
            assertNotNull(room);
        }

        @Test
        @DisplayName("Test constructor with three local variables")
        @Tag("unit")
        void testConstructorThree() {
            room = new Room("abc", "def", startingTime);
            assertNotNull(room);
        }

        @Test
        @DisplayName("Test constructor with eight local variables")
        @Tag("unit")
        void testConstructorEight() {
            room = new Room("def", startingTime, creator, bannedIps, true, 3,
                    2, 1);
            assertNotNull(room);
        }

        @Test
        @DisplayName("Test constructor with ten local variables")
        @Tag("unit")
        void testConstructorTen() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertNotNull(room);
        }
    }

    //--------------------------------------------
    // Getter section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for getters")
    class GetterTests {
        @Test
        @DisplayName("Test id getter with empty constructor")
        @Tag("unit")
        void testGetIdEmptyConstructor() {
            room = new Room();
            assertNull(room.getId());
        }

        @Test
        @DisplayName("Test id getter with non empty constructor")
        @Tag("unit")
        void testGetIdNonEmptyConstructor() {
            room = new Room("abc", "def", startingTime);
            assertEquals("abc", room.getId());
        }

        @Test
        @DisplayName("Test accessToken getter with empty constructor")
        @Tag("unit")
        void testGetAccessTokenEmptyConstructor() {
            room = new Room();
            assertNull(room.getAccessToken());
        }

        @Test
        @DisplayName("Test accessToken getter with non empty constructor")
        @Tag("unit")
        void testGetAccessTokenNonEmptyConstructor() {
            room = new Room("abc", "def", startingTime);
            assertEquals("def", room.getAccessToken());
        }

        @Test
        @DisplayName("Test startingTime getter with empty constructor")
        @Tag("unit")
        void testGetStartingTimeEmptyConstructor() {
            room = new Room();
            assertNull(room.getStartingTime());
        }

        @Test
        @DisplayName("Test startingTime getter with non empty constructor")
        @Tag("unit")
        void testGetStartingTimeNonEmptyConstructor() {
            room = new Room("abc", "def", startingTime);
            assertEquals(startingTime, room.getStartingTime());
        }

        @Test
        @DisplayName("Test creator getter with empty constructor")
        @Tag("unit")
        void testCreatorEmptyConstructor() {
            room = new Room();
            assertNull(room.getCreator());
        }

        @Test
        @DisplayName("Test creator getter with non empty constructor")
        @Tag("unit")
        void testCreatorNonEmptyConstructor() {
            room = new Room("abc", "def", startingTime);
            assertNull(room.getCreator());
        }

        @Test
        @DisplayName("Test isActive getter with empty constructor")
        @Tag("unit")
        void testIsOngoingEmptyConstructor() {
            room = new Room();
            assertFalse(room.isOngoing());
        }

        @Test
        @DisplayName("Test isActive getter with non empty constructor")
        @Tag("unit")
        void testOnGoingNonEmptyConstructor() {
            room = new Room("abc", "def", startingTime);
            assertFalse(room.isOngoing());
        }

        @Test
        @DisplayName("Test isClosedByMod getter with empty constructor")
        @Tag("unit")
        void testisClosedByModEmptyConstructor() {
            room = new Room();
            assertFalse(room.isClosedByMod());
        }

        @Test
        @DisplayName("Test isClosedByMod getter with non empty constructor")
        @Tag("unit")
        void testisClosedByModNonEmptyConstructor() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertFalse(room.isClosedByMod());
        }

        @Test
        @DisplayName("Test getBannedIps getter with non empty constructor")
        @Tag("unit")
        void testgetBannedIpsNonEmptyConstructor() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertTrue(room.getBannedIps().size() == 1);
        }

        @Test
        @DisplayName("Test getBannedIps getter with  empty constructor")
        @Tag("unit")
        void testgetBannedIpsEmptyConstructor() {
            room = new Room();
            assertFalse(room.getBannedIps().size() == 1);
        }

        @Test
        @DisplayName("Test checkQuiz getter with non empty constructor")
        @Tag("unit")
        void testCheckQuizNonEmptyConstructor() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertFalse(room.checkQuiz());
        }

        @Test
        @DisplayName("Test checkQuiz getter with empty constructor")
        @Tag("unit")
        void testCheckQuizEmptyConstructor() {
            room = new Room();
            assertFalse(room.checkQuiz());
        }

        @Test
        @DisplayName("Test getClosedByMod setter with non empty constructor")
        @Tag("unit")
        void testGetClosedByModNonEmptyConstructor() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertFalse(room.getClosedByMod());
            room.setClosedByMod(true);
            assertTrue(room.getClosedByMod());
        }

        @Test
        @DisplayName("Test getClosedByMod setter with empty constructor")
        @Tag("unit")
        void testGetClosedByModEmptyConstructor() {
            room = new Room();
            assertFalse(room.getClosedByMod());
            room.setClosedByMod(true);
            assertTrue(room.getClosedByMod());
        }

        @Test
        @DisplayName("Test getRightAnswer setter with empty constructor")
        @Tag("unit")
        void testGetRightAnswerByModEmptyConstructor() {
            room = new Room();
            room.toggleQuiz(10);
            room.setRightAnswer(4);
            assertTrue(room.getRightAnswer() == 4);
        }

        @Test
        @DisplayName("Test getRightAnswer setter with non empty constructor")
        @Tag("unit")
        void testGetRightAnswerByModNonEmptyConstructor() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            room.toggleQuiz(10);
            room.setRightAnswer(4);
            assertTrue(room.getRightAnswer() == 4);
        }

    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for setters")
    class SetterTests {
        @Test
        @DisplayName("Test id setter")
        @Tag("unit")
        void testSetId() {
            room = new Room("abc", "def", startingTime);
            room.setId("cba");
            assertEquals("cba", room.getId());
        }

        @Test
        @DisplayName("Test accessToken setter")
        @Tag("unit")
        void testSetAccessToken() {
            room = new Room("abc", "def", startingTime);
            room.setAccessToken("fed");
            assertEquals("fed", room.getAccessToken());
        }

        @Test
        @DisplayName("Test startingTime setter")
        @Tag("unit")
        void testSetStartingTime() {
            room = new Room("abc", "def", null);
            room.setStartingTime(startingTime);
            assertEquals(startingTime, room.getStartingTime());
        }

        @Test
        @DisplayName("Test creator setter")
        @Tag("unit")
        void testSetCreator() {
            room = new Room("abc", "def", startingTime);
            User creator = new User("John Doe", "abc");
            room.setCreator(creator);
            assertEquals(creator, room.getCreator());
        }

        @Test
        @DisplayName("Test isActive setter")
        @Tag("unit")
        void testSetActive() {
            room = new Room("abc", "def", startingTime);
            room.setActive(true);
            assertTrue(room.isOngoing());
        }

        @Test
        @DisplayName("Test bannedIps setter")
        @Tag("unit")
        void testSetBannedIps() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            HashSet hashSet = new HashSet<>();
            hashSet.add("dada");
            room.setBannedIps(hashSet);
            assertTrue(room.getBannedIps().contains("dada"));
        }

        @Test
        @DisplayName("Test setClosedByMod setter")
        @Tag("unit")
        void testSetClosedByMod() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            assertFalse(room.getClosedByMod());
            room.setClosedByMod(true);
            assertTrue(room.getClosedByMod());
        }

        @Test
        @DisplayName("Test setRightAnswer integer above max size")
        @Tag("unit")
        void testSetRightAnswerAboveSize() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,3,2,1);
            room.toggleQuiz(10);
            room.setRightAnswer(4);
            room.setRightAnswer(11);
            room.setRightAnswer(-1);
            assertTrue(room.getRightAnswer() == 4);
        }
    }

    //--------------------------------------------
    // Equals, hashCode, toString methods section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for equals method")
    class EqualsTests {
        @BeforeEach
        void inClassSetup() {
            room = new Room("abc", "def", startingTime);
        }

        @Test
        @DisplayName("Test equals method for true")
        @Tag("unit")
        void testEqualsTrue() {
            User creator = new User("John Doe", "abc");
            room.setCreator(creator);
            Room room2 = new Room("abc", "def", startingTime);
            room2.setCreator(creator);
            assertTrue(room.equals(room2));
        }

        @Test
        @DisplayName("Test equals method for false")
        @Tag("unit")
        void testEqualsFalse() {
            Room room2 = new Room("abc", "fed", startingTime);
            assertFalse(room.equals(room2));
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsItself() {
            assertTrue(room.equals(room));
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsDifferentType() {
            Object o = new Object();
            assertFalse(room.equals(o));
        }

    }

    @Nested
    @DisplayName("Tests for hashCode method")
    class HashCodeTests {
        @Test
        @DisplayName("Test hashCode method")
        @Tag("unit")
        void testHashCode() {
            room = new Room("abc", "def", startingTime);
            Room room2 = new Room("abc", "def", startingTime);
            assertEquals(room.hashCode(), room2.hashCode());
        }
    }

    @Nested
    @DisplayName("Tests for toString method")
    class ToStringTests {
        @Test
        @DisplayName("Test toString method")
        @Tag("unit")
        void testToStringAnsweredFalse() {
            room = new Room("abc", "def", startingTime);
            String expected = "Room{id='abc', accessToken='def', startingTime=2000-01-01T12:00, "
                    + "creator=null, isActive=false}";
            assertEquals(expected, room.toString());
        }
    }

    //--------------------------------------------
    // voting section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for voting")
    class VotingTests {
        @BeforeEach
        void setup() {
            room = new Room("abc","def",startingTime,false,creator,bannedIps,
                    true,1,1,1);
        }

        @Test
        @DisplayName("Test for adding fast vote")
        @Tag("unit")
        void addFastVote() {
            room.addVote('f');
            assertTrue(room.getVotes().get(0) == 2);
        }

        @Test
        @DisplayName("Test for adding alright vote")
        @Tag("unit")
        void addAlrightVote() {
            room.addVote('a');
            assertTrue(room.getVotes().get(1) == 2);
        }

        @Test
        @DisplayName("Test for adding slow vote")
        @Tag("unit")
        void addSlowVote() {
            room.addVote('s');
            assertTrue(room.getVotes().get(2) == 2);
        }

        @Test
        @DisplayName("Test for removing fast vote")
        @Tag("unit")
        void removeFastVote() {
            room.removeVote('f');
            assertTrue(room.getVotes().get(0) == 0);
        }

        @Test
        @DisplayName("Test for removing alright vote")
        @Tag("unit")
        void removeAlrightVote() {
            room.removeVote('a');
            assertTrue(room.getVotes().get(1) == 0);
        }

        @Test
        @DisplayName("Test for removing slow vote")
        @Tag("unit")
        void removeSlowVote() {
            room.removeVote('s');
            assertTrue(room.getVotes().get(2) == 0);
        }
    }


    //--------------------------------------------
    // quiz section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for quiz")
    class QuizTests {
        @BeforeEach
        void setup() {
            room = new Room("abc", "def", startingTime, false, creator, bannedIps,
                    true, 1, 1, 1);
            room.toggleQuiz(10);
            room.addQuizAnswer(4);
            room.addQuizAnswer(2);
        }

        @Test
        @DisplayName("Test for toggling quizzing with no size of answer")
        @Tag("unit")
        void toggleQuizOngoing() {
            room.setIsQuiz(true);
            room.toggleQuiz();
            assertFalse(room.checkQuiz());
        }

        @Test
        @DisplayName("Test for toggling quizzing with no size of answer")
        @Tag("unit")
        void toggleQuizNotOngoing() {
            room.setIsQuiz(false);
            room.toggleQuiz();
            assertTrue(room.checkQuiz());
        }

        @Test
        @DisplayName("Test for toggling quizzing with size of answer")
        @Tag("unit")
        void toggleQuizOngoingAnswerSize() {
            room.setIsQuiz(true);
            room.toggleQuiz(5);
            assertFalse(room.checkQuiz());
        }

        @Test
        @DisplayName("Test for toggling quizzing with size of answer")
        @Tag("unit")
        void toggleQuizNotOngoingAnswerSize() {
            room.setIsQuiz(false);
            room.toggleQuiz(5);
            assertTrue(room.checkQuiz());
        }

        @Test
        @DisplayName("Test for getMaxSize of answers")
        @Tag("unit")
        void testgetMaxSize() {
            assertTrue(room.getMaxSize() == 10);
        }

        @Test
        @DisplayName("Test for removing an answer")
        @Tag("unit")
        void testremoveAnswer() {
            room.removeQuizAnswer(4);
            assertTrue(room.getQuizResults().get(3) != 1);
        }

        @Test
        @DisplayName("Test for adding an answer")
        @Tag("unit")
        void testaddingAnswer() {
            room.addQuizAnswer(5);
            assertTrue(room.getQuizResults().get(4) == 1);
        }
    }


    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    @Test
    @DisplayName("test adding banned Ips")
    @Tag("unit")
    void testAddBannedIp() {
        room = room = new Room("abc", "def", startingTime, false, creator, bannedIps,
                true, 1, 1, 1);
        room.addBannedIp("0001:0000:0000:0000:0000:0000:0000:0000");
        assertTrue(room.getBannedIps().contains("0001:0000:0000:0000:0000:0000:0000:0000"));
    }

}