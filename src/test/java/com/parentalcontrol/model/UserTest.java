package com.parentalcontrol.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 */
class UserTest {

    @Test
    @DisplayName("Should create user with valid parameters")
    void testValidUserCreation() {
        User user = new User("Alice", 15, Rating.PG_13);
        
        assertEquals("Alice", user.getUsername());
        assertEquals(15, user.getAge());
        assertEquals(Rating.PG_13, user.getMaxAllowedRating());
        assertFalse(user.isAdult());
    }

    @Test
    @DisplayName("Should identify adults correctly")
    void testAdultIdentification() {
        User child = new User("Child", 10, Rating.PG);
        User teenager = new User("Teen", 16, Rating.PG_13);
        User adult = new User("Adult", 25, Rating.EIGHTEEN);
        
        assertFalse(child.isAdult());
        assertFalse(teenager.isAdult());
        assertTrue(adult.isAdult());
    }

    @Test
    @DisplayName("Should throw exception for invalid username")
    void testInvalidUsername() {
        assertThrows(IllegalArgumentException.class, () -> 
            new User(null, 25, Rating.EIGHTEEN));
        assertThrows(IllegalArgumentException.class, () -> 
            new User("", 25, Rating.EIGHTEEN));
        assertThrows(IllegalArgumentException.class, () -> 
            new User("   ", 25, Rating.EIGHTEEN));
    }

    @Test
    @DisplayName("Should throw exception for invalid age")
    void testInvalidAge() {
        assertThrows(IllegalArgumentException.class, () -> 
            new User("Alice", -1, Rating.U));
        assertThrows(IllegalArgumentException.class, () -> 
            new User("Alice", 151, Rating.U));
    }

    @Test
    @DisplayName("Should throw exception for null rating")
    void testNullRating() {
        assertThrows(IllegalArgumentException.class, () -> 
            new User("Alice", 25, null));
    }

    @Test
    @DisplayName("Should determine movie access correctly")
    void testCanWatch() {
        User childUser = new User("Child", 10, Rating.PG);
        User adultUser = new User("Adult", 25, Rating.R);
        
        assertTrue(childUser.canWatch(Rating.U));
        assertTrue(childUser.canWatch(Rating.PG));
        assertFalse(childUser.canWatch(Rating.PG_13));
        assertFalse(childUser.canWatch(Rating.R));
        
        assertTrue(adultUser.canWatch(Rating.U));
        assertTrue(adultUser.canWatch(Rating.PG_13));
        assertTrue(adultUser.canWatch(Rating.R));
    }

    @Test
    @DisplayName("Should create user with age-appropriate default rating")
    void testCreateWithDefaultRating() {
        assertEquals(Rating.U, User.createWithDefaultRating("Baby", 5).getMaxAllowedRating());
        assertEquals(Rating.PG, User.createWithDefaultRating("Child", 10).getMaxAllowedRating());
        assertEquals(Rating.TWELVE, User.createWithDefaultRating("Preteen", 12).getMaxAllowedRating());
        assertEquals(Rating.PG_13, User.createWithDefaultRating("Teen", 13).getMaxAllowedRating());
        assertEquals(Rating.FIFTEEN, User.createWithDefaultRating("Teenager", 15).getMaxAllowedRating());
        assertEquals(Rating.EIGHTEEN, User.createWithDefaultRating("Adult", 25).getMaxAllowedRating());
    }

    @Test
    @DisplayName("Should handle username trimming")
    void testUsernameTrimming() {
        User user = new User("  Alice  ", 25, Rating.EIGHTEEN);
        assertEquals("Alice", user.getUsername());
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        User user1 = new User("Alice", 25, Rating.EIGHTEEN);
        User user2 = new User("Alice", 30, Rating.PG); // Different age and rating
        User user3 = new User("Bob", 25, Rating.EIGHTEEN);
        
        assertEquals(user1, user2); // Same username
        assertNotEquals(user1, user3); // Different username
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void testToString() {
        User user = new User("Alice", 25, Rating.EIGHTEEN);
        String toString = user.toString();
        
        assertTrue(toString.contains("Alice"));
        assertTrue(toString.contains("25"));
        assertTrue(toString.contains("EIGHTEEN"));
        assertTrue(toString.contains("true")); // isAdult
    }
}