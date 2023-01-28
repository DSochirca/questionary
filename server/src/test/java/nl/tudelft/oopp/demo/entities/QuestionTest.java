package nl.tudelft.oopp.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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

class QuestionTest {

    private Question question;
    private String creatorIP;
    private LocalDateTime timeAsked;

    @BeforeEach
    void setup() {
        creatorIP = "2001:0db8:0000:0000:0000:8a2e:0370:7334";
        timeAsked = LocalDateTime.of(2000, 1, 1, 12, 0, 0);
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
            question = new Question();
            assertNotNull(question);
        }

        @Test
        @DisplayName("Test constructor with five local variables")
        @Tag("unit")
        void testConstructorFive() {
            question = new Question("abc", "John Doe", "question", timeAsked, creatorIP);
            assertNotNull(question);
        }

        @Test
        @DisplayName("Test constructor with six local variables")
        @Tag("unit")
        void testConstructorSix() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, creatorIP);
            assertNotNull(question);
        }

        @Test
        @DisplayName("Test constructor with seven local variables")
        @Tag("unit")
        void testConstructorSeven() {
            question = new Question("abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertNotNull(question);
        }

        @Test
        @DisplayName("Test constructor with eight local variables")
        @Tag("unit")
        void testConstructorEight() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertNotNull(question);
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
            question = new Question();
            assertNull(question.getId());
        }

        @Test
        @DisplayName("Test id getter with non empty constructor")
        @Tag("unit")
        void testGetIdNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(1L, question.getId());
        }

        @Test
        @DisplayName("Test roomId getter with empty constructor")
        @Tag("unit")
        void testGetRoomIdEmptyConstructor() {
            question = new Question();
            assertNull(question.getRoomID());
        }

        @Test
        @DisplayName("Test roomID getter with non empty constructor")
        @Tag("unit")
        void testGetRoomIdNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals("abc", question.getRoomID());
        }

        @Test
        @DisplayName("Test username getter with empty constructor")
        @Tag("unit")
        void testGetUsernameEmptyConstructor() {
            question = new Question();
            assertNull(question.getUsername());
        }

        @Test
        @DisplayName("Test username getter with non empty constructor")
        @Tag("unit")
        void testGetUsernameNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals("John Doe", question.getUsername());
        }

        @Test
        @DisplayName("Test question getter with empty constructor")
        @Tag("unit")
        void testGetQuestionEmptyConstructor() {
            question = new Question();
            assertNull(question.getQuestion());
        }

        @Test
        @DisplayName("Test question getter with non empty constructor")
        @Tag("unit")
        void testGetQuestionNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals("question", question.getQuestion());
        }

        @Test
        @DisplayName("Test timeAsked getter with empty constructor")
        @Tag("unit")
        void testGetTimeAskedEmptyConstructor() {
            question = new Question();
            assertNull(question.getTimeAsked());
        }

        @Test
        @DisplayName("Test timeAsked getter with non empty constructor")
        @Tag("unit")
        void testGetTimeAskedNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(timeAsked, question.getTimeAsked());
        }

        @Test
        @DisplayName("Test upvotes getter with empty constructor")
        @Tag("unit")
        void testGetUpvotesEmptyConstructor() {
            question = new Question();
            assertEquals(0, question.getUpvotes());
        }

        @Test
        @DisplayName("Test upvotes getter with non empty constructor")
        @Tag("unit")
        void testGetUpvotesNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(5, question.getUpvotes());
        }

        @Test
        @DisplayName("Test answered getter with empty constructor")
        @Tag("unit")
        void testIsAnsweredEmptyConstructor() {
            question = new Question();
            assertFalse(question.isAnswered());
        }

        @Test
        @DisplayName("Test answered getter with non empty constructor")
        @Tag("unit")
        void testIsAnsweredNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertFalse(question.isAnswered());
        }

        @Test
        @DisplayName("Test creatorIp getter with empty constructor")
        @Tag("unit")
        void testGetCreatorIpEmptyConstructor() {
            question = new Question();
            assertNull(question.getCreatorIP());
        }

        @Test
        @DisplayName("Test creatorIp getter with non empty constructor")
        @Tag("unit")
        void testGetCreatorIpNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals("2001:0db8:0000:0000:0000:8a2e:0370:7334", question.getCreatorIP());
        }

        @Test
        @DisplayName("Test usersUpvoted getter with empty constructor")
        @Tag("unit")
        void testGetUsersUpvotedEmptyConstructor() {
            question = new Question();
            assertEquals(new HashSet<>(), question.getUsersUpvoted());
        }

        @Test
        @DisplayName("Test usersUpvoted getter with non empty constructor")
        @Tag("unit")
        void testGetUsersUpvotedNonEmptyConstructor() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(new HashSet<>(), question.getUsersUpvoted());
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
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
        }

        @Test
        @DisplayName("Test id setter")
        @Tag("unit")
        void testSetId() {
            question.setId(33L);
            assertEquals(33L, question.getId());
        }

        @Test
        @DisplayName("Test roomID setter")
        @Tag("unit")
        void testSetRoomID() {
            question.setRoomID("cba");
            assertEquals("cba", question.getRoomID());
        }

        @Test
        @DisplayName("Test username setter")
        @Tag("unit")
        void testSetUsername() {
            question.setUsername("Jane Doe");
            assertEquals("Jane Doe", question.getUsername());
        }

        @Test
        @DisplayName("Test question setter")
        @Tag("unit")
        void testSetQuestion() {
            question.setQuestion("answer");
            assertEquals("answer", question.getQuestion());
        }

        @Test
        @DisplayName("Test timeAsked setter")
        @Tag("unit")
        void testSetTimeAsked() {
            question.setTimeAsked(LocalDateTime.of(2001, 12, 22, 7, 53, 0));
            assertEquals(LocalDateTime.of(2001, 12, 22, 7, 53, 0),
                    question.getTimeAsked());
        }

        @Test
        @DisplayName("Test upvotes setter")
        @Tag("unit")
        void testSetUpvotes() {
            question.setUpvotes(10);
            assertEquals(10, question.getUpvotes());
        }

        @Test
        @DisplayName("Test answered setter")
        @Tag("unit")
        void testSetAnswered() {
            question.setAnswered(true);
            assertTrue(question.isAnswered());
        }

        @Test
        @DisplayName("Test creatorIP setter")
        @Tag("unit")
        void testSetCreatorIP() {
            question.setCreatorIP("0000:0000:0000:0000:0000:0000:0000:0000");
            assertEquals("0000:0000:0000:0000:0000:0000:0000:0000", question.getCreatorIP());
        }

        @Test
        @DisplayName("Test usersUpvoted setter")
        @Tag("unit")
        void testSetUsersUpvoted() {
            Set<User> usersUpvoted = new HashSet<>();
            usersUpvoted.add(new User("Jane Doe", "abc"));
            question.setUsersUpvoted(usersUpvoted);
            assertEquals(usersUpvoted, question.getUsersUpvoted());
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
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
        }

        @Test
        @DisplayName("Test equals method for true")
        @Tag("unit")
        void testEqualsTrue() {
            Question question2 = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(question2, question);
        }

        @Test
        @DisplayName("Test equals method for false")
        @Tag("unit")
        void testEqualsFalse() {
            Question question2 = new Question(1L, "abc", "Jane Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertNotEquals(question2, question);
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsItself() {
            assertTrue(question.equals(question));
        }

        @Test
        @DisplayName("Test equals method itself")
        @Tag("unit")
        void testEqualsDifferentType() {
            Object o = new Object();
            assertFalse(question.equals(o));
        }
    }

    @Nested
    @DisplayName("Tests for hashCode method")
    class HashCodeTests {
        @Test
        @DisplayName("Test hashCode method")
        @Tag("unit")
        void testHashCode() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            Question question2 = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            assertEquals(question.hashCode(), question2.hashCode());
        }
    }

    @Nested
    @DisplayName("Tests for toString method")
    class ToStringTests {
        @Test
        @DisplayName("Test toString method when answered=false")
        @Tag("unit")
        void testToStringAnsweredFalse() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            String expected = "John Doe\nquestion";
            assertEquals(expected, question.toString());
        }

        @Test
        @DisplayName("Test toString method when answered=true")
        @Tag("unit")
        void testToStringAnsweredTrue() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    true, creatorIP);
            String expected = "John Doe       - answered ✓\nquestion";
            assertEquals(expected, question.toString());
        }
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for upvote methods")
    class UpvoteTests {
        private User user;

        @BeforeEach
        void inClassSetup() {
            user = new User("Jane Doe", "abc");
        }

        @Test
        @DisplayName("Test addUpvote method from 0 user not in set")
        @Tag("unit")
        void testAddUpvoteFrom0UserNotInSet() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 0,
                    false, creatorIP);
            question.addUpvote(user);
            assertEquals(1, question.getUpvotes());
        }

        @Test
        @DisplayName("Test addUpvote method from 9 user in set")
        @Tag("unit")
        void testAddUpvoteFrom9UserInSet() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 9,
                    false, creatorIP);
            question.getUsersUpvoted().add(user);
            question.addUpvote(user);
            assertEquals(8, question.getUpvotes());
        }

        @Test
        @DisplayName("Test removeUpvote method from 5 user not in set")
        @Tag("unit")
        void testRemoveUpvoteFrom0UserNotInSet() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 5,
                    false, creatorIP);
            question.removeUpvote(user);
            assertEquals(5, question.getUpvotes());
        }

        @Test
        @DisplayName("Test removeUpvote method from 0 user in set")
        @Tag("unit")
        void testRemoveUpvoteFrom0UserInSet() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 0,
                    false, creatorIP);
            question.getUsersUpvoted().add(user);
            question.removeUpvote(user);
            assertEquals(0, question.getUpvotes());
        }

        @Test
        @DisplayName("Test removeUpvote method from 9 user in set")
        @Tag("unit")
        void testRemoveUpvoteFrom9UserInSet() {
            question = new Question(1L, "abc", "John Doe", "question", timeAsked, 9,
                    false, creatorIP);
            question.getUsersUpvoted().add(user);
            question.removeUpvote(user);
            assertEquals(8, question.getUpvotes());
        }
    }
}