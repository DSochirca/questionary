package nl.tudelft.oopp.demo.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        @DisplayName("Test constructor 1 with three local variables")
        @Tag("unit")
        void testConstructorThree() {
            user = new User("John Doe", ip, "student");
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
            user = new User("John Doe", ip, "student");
            assertNull(user.getId());
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
            user = new User("John Doe", ip, "student");
            assertEquals("John Doe", user.getUsername());
        }

        @Test
        @DisplayName("Test ip getter with empty constructor")
        @Tag("unit")
        void testGetIpEmptyConstructor() {
            user = new User();
            assertNull(user.getip());
        }

        @Test
        @DisplayName("Test ip getter with non empty constructor")
        @Tag("unit")
        void testGetIpNonEmptyConstructor() {
            user = new User("John Doe", ip, "student");
            assertEquals("2001:0db8:0000:0000:0000:8a2e:0370:7334", user.getip());
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
            user = new User("John Doe", ip, "student");
            assertNull(user.getCurrentRoomID());
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
            user = new User("John Doe", ip, "student");
            assertEquals("student", user.getUserType());
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
            user = new User("John Doe", ip, "student");
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
    }

    //--------------------------------------------
    // Equals, hashCode, toString methods section:
    //--------------------------------------------

    @Nested
    @DisplayName("Tests for equals method")
    class EqualsTests {
        @BeforeEach
        void inClassSetup() {
            user = new User("John Doe", ip, "student");
        }

        @Test
        @DisplayName("Test equals method for true")
        @Tag("unit")
        void testEqualsTrue() {
            User user2 = new User("John Doe", ip, "student");
            assertTrue(user.equals(user2));
        }

        @Test
        @DisplayName("Test equals method for false")
        @Tag("unit")
        void testEqualsFalse() {
            User user2 = new User("Jane Doe", ip, "student");
            assertFalse(user.equals(user2));
        }
    }

    @Nested
    @DisplayName("Tests for hashCode method")
    class HashCodeTests {
        @Test
        @DisplayName("Test hashCode method")
        @Tag("unit")
        void testHashCode() {
            user = new User("John Doe", ip, "student");
            User user2 = new User("John Doe", ip, "student");
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
            user = new User("John Doe", ip, "student");
            String expected = "User{id=null, username='John Doe', ip='2001:0db8:0000:0000:0000:8a2e:0370:7334', "
                    + "currentRoomID='null', userType='student'}";
            assertEquals(expected, user.toString());
        }
    }

    //--------------------------------------------
    // Other methods section:
    //--------------------------------------------

}