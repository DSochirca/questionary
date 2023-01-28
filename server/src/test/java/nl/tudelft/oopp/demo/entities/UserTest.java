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

class UserTest {

    private User user;
    private String ip;

    @BeforeEach
    void setup() {
        ip = "2001:0db8:0000:0000:0000:8a2e:0370:7334";
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
            user = new User();
            assertNotNull(user);
        }

        @Test
        @DisplayName("Test constructor with two local variables")
        @Tag("unit")
        void testConstructorTwo() {
            user = new User("John Doe", ip);
            assertNotNull(user);
        }

        @Test
        @DisplayName("Test constructor 1 with three local variables")
        @Tag("unit")
        void testOneConstructorThree() {
            user = new User("John Doe", ip, "student");
            assertNotNull(user);
        }

        @Test
        @DisplayName("Test constructor 2 with three local variables")
        @Tag("unit")
        void testTwoConstructorThree() {
            user = new User(1L, "John Doe", ip);
            assertNotNull(user);
        }

        @Test
        @DisplayName("Test constructor with five local variables")
        @Tag("unit")
        void testTwoConstructorFive() {
            user = new User("John Doe", ip, false, "abc", "student");
            assertNotNull(user);
        }

        @Test
        @DisplayName("Test constructor with six local variables")
        @Tag("unit")
        void testTwoConstructorSix() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertNotNull(user);
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
            user = new User();
            assertNull(user.getId());
        }

        @Test
        @DisplayName("Test id getter with non empty constructor")
        @Tag("unit")
        void testGetIdNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals(1L, user.getId());
        }

        @Test
        @DisplayName("Test username getter with empty constructor")
        @Tag("unit")
        void testGetUsernameEmptyConstructor() {
            user = new User();
            assertNull(user.getUsername());
        }

        @Test
        @DisplayName("Test username getter with non empty constructor")
        @Tag("unit")
        void testGetUsernameNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals("John Doe", user.getUsername());
        }

        @Test
        @DisplayName("Test ip getter with empty constructor")
        @Tag("unit")
        void testGetIpEmptyConstructor() {
            user = new User();
            assertNull(user.getIP());
        }

        @Test
        @DisplayName("Test ip getter with non empty constructor")
        @Tag("unit")
        void testGetIpNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals("2001:0db8:0000:0000:0000:8a2e:0370:7334", user.getIP());
        }

        @Test
        @DisplayName("Test isBanned getter with empty constructor")
        @Tag("unit")
        void testGetIsBannedEmptyConstructor() {
            user = new User();
            assertFalse(user.isBanned());
        }

        @Test
        @DisplayName("Test isBanned getter with non empty constructor")
        @Tag("unit")
        void testGetIsBannedNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, true, "abc", "student");
            assertTrue(user.isBanned());
        }

        @Test
        @DisplayName("Test currentRoomId getter with empty constructor")
        @Tag("unit")
        void testGetCurrentRoomIdEmptyConstructor() {
            user = new User();
            assertNull(user.getCurrentRoomID());
        }

        @Test
        @DisplayName("Test currentRoomID getter with non empty constructor")
        @Tag("unit")
        void testCurrentRoomIdNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals("abc", user.getCurrentRoomID());
        }

        @Test
        @DisplayName("Test userType getter with empty constructor")
        @Tag("unit")
        void testGetUserTypeEmptyConstructor() {
            user = new User();
            assertNull(user.getUserType());
        }

        @Test
        @DisplayName("Test userType getter with non empty constructor")
        @Tag("unit")
        void testGetUserTypeNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals("student", user.getUserType());
        }

        @Test
        @DisplayName("Test roomsCreated getter with empty constructor")
        @Tag("unit")
        void testGetRoomsCreatedEmptyConstructor() {
            user = new User();
            assertEquals(new HashSet<Room>(), user.getRoomsCreated());
        }

        @Test
        @DisplayName("Test roomsCreated getter with non empty constructor")
        @Tag("unit")
        void testGetRoomsCreatedNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals(new HashSet<Room>(), user.getRoomsCreated());
        }

        @Test
        @DisplayName("Test questionUpvoted getter with empty constructor")
        @Tag("unit")
        void testGetQuestionsUpvotedEmptyConstructor() {
            user = new User();
            assertEquals(new HashSet<Question>(), user.getQuestionsUpvoted());
        }

        @Test
        @DisplayName("Test questionsUpvoted getter with non empty constructor")
        @Tag("unit")
        void testGetQuestionsUpvotedNonEmptyConstructor() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals(new HashSet<Question>(), user.getQuestionsUpvoted());
        }
    }

    //--------------------------------------------
    // Setter section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for setters")
    class SetterTests {
        @BeforeEach
        void inClassSetup() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
        }

        @Test
        @DisplayName("Test id setter")
        @Tag("unit")
        void testSetId() {
            user.setId(2L);
            assertEquals(2L, user.getId());
        }

        @Test
        @DisplayName("Test username setter")
        @Tag("unit")
        void testSetUsername() {
            user.setUsername("Jane Doe");
            assertEquals("Jane Doe", user.getUsername());
        }

        @Test
        @DisplayName("Test IP setter")
        @Tag("unit")
        void testSetIP() {
            user.setIP("0000:0000:0000:0000:0000:0000:0000:0000");
            assertEquals("0000:0000:0000:0000:0000:0000:0000:0000", user.getIP());
        }

        @Test
        @DisplayName("Test isBanned setter")
        @Tag("unit")
        void testSetBanned() {
            user.setBanned(true);
            assertTrue(user.isBanned());
        }

        @Test
        @DisplayName("Test currentRoomID setter")
        @Tag("unit")
        void testSetCurrentRoomID() {
            user.setCurrentRoomID("cba");
            assertEquals("cba", user.getCurrentRoomID());
        }

        @Test
        @DisplayName("Test userType setter")
        @Tag("unit")
        void testSetUserType() {
            user.setUserType("lecturer");
            assertEquals("lecturer", user.getUserType());
        }

        @Test
        @DisplayName("Test roomsCreated setter")
        @Tag("unit")
        void testSetRoomsCreated() {
            Set<Room> roomsCreated = new HashSet<>();
            roomsCreated.add(new Room("abc"));
            user.setRoomsCreated(roomsCreated);
            assertEquals(roomsCreated, user.getRoomsCreated());
        }

        @Test
        @DisplayName("Test questionsUpvoted setter")
        @Tag("unit")
        void testSetQuestionsUpvoted() {
            Set<Question> questionsUpvoted  = new HashSet<>();
            LocalDateTime timeAsked = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
            questionsUpvoted.add(new Question(1L,"abc", "John Doe", "question", timeAsked,
                    5, false, ip));
            user.setQuestionsUpvoted(questionsUpvoted);
            assertEquals(questionsUpvoted, user.getQuestionsUpvoted());
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
            user = new User(1L, "John Doe", ip, false, "abc", "student");
        }

        @Test
        @DisplayName("Test equals method for true")
        @Tag("unit")
        void testEqualsTrue() {
            User user2 = new User(1L, "John Doe", ip, false, "abc", "student");
            assertTrue(user.equals(user2));
        }

        @Test
        @DisplayName("Test equals method for false")
        @Tag("unit")
        void testEqualsFalse() {
            User user2 = new User(1L, "Jane Doe", ip, false, "abc", "student");
            assertFalse(user.equals(user2));
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsItself() {
            assertTrue(user.equals(user));
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsDifferentType() {
            Object o = new Object();
            assertFalse(user.equals(o));
        }
    }

    @Nested
    @DisplayName("Tests for hashCode method")
    class HashCodeTests {
        @Test
        @DisplayName("Test hashCode method")
        @Tag("unit")
        void testHashCode() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            User user2 = new User(1L, "John Doe", ip, false, "abc", "student");
            assertEquals(user.hashCode(), user2.hashCode());
        }
    }

    @Nested
    @DisplayName("Tests for toString method")
    class ToStringTests {
        @Test
        @DisplayName("Test toString method")
        @Tag("unit")
        void testToString() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
            String expected = "User{id=1, username='John Doe', IP='2001:0db8:0000:0000:0000:8a2e:0370:7334', "
                    + "isBanned=false, currentRoomId='abc', userType='student', roomsCreated=[], questionsUpvoted=[]}";
            assertEquals(expected, user.toString());
        }
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for roomsCreated methods")
    class RoomsCreatedTests {
        @BeforeEach
        void inClassSetup() {
            user = new User(1L, "John Doe", ip, false, "abc", "student");
        }

        @Test
        @DisplayName("Test addRoomCreated method")
        @Tag("unit")
        void testAddRoomCreated() {
            user.addRoomCreated(new Room("abc"));
            assertEquals(1, user.getRoomsCreated().size());
        }

        @Test
        @DisplayName("Test removeRoomCreated method")
        @Tag("unit")
        void testRemoveRoomCreated() {
            Room room1 = new Room("abc");
            Room room2 = new Room("def");
            Room room3 = new Room("ghi");
            user.addRoomCreated(room1);
            user.addRoomCreated(room2);
            user.addRoomCreated(room3);
            user.removeRoomCreated(room2);
            assertEquals(2, user.getRoomsCreated().size());
        }

        @Test
        @DisplayName("Test addQuestionUpvoted method question not in set")
        @Tag("unit")
        void testAddQuestionUpvotedQuestionNotInSet() {
            LocalDateTime timeAsked = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
            user.addQuestionUpvoted(new Question(1L,"abc", "John Doe", "question", timeAsked,
                    5, false, ip));
            assertEquals(1, user.getQuestionsUpvoted().size());
        }

        @Test
        @DisplayName("Test addQuestionUpvoted method question in set")
        @Tag("unit")
        void testAddQuestionUpvotedQuestionInSet() {
            LocalDateTime timeAsked = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
            user.addQuestionUpvoted(new Question(1L,"abc", "John Doe", "question", timeAsked,
                    5, false, ip));
            user.addQuestionUpvoted(new Question(1L,"abc", "John Doe", "question", timeAsked,
                    5, false, ip));
            assertEquals(0, user.getQuestionsUpvoted().size());
        }
    }
}