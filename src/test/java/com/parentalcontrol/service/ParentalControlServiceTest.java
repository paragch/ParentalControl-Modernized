package com.parentalcontrol.service;

import com.parentalcontrol.model.Movie;
import com.parentalcontrol.model.Rating;
import com.parentalcontrol.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ParentalControlService.
 */
class ParentalControlServiceTest {

    private ParentalControlService service;
    private User childUser;
    private User adultUser;
    private Movie childMovie;
    private Movie adultMovie;

    @BeforeEach
    void setUp() {
        service = new ParentalControlService(); // Use default constructor with real MovieService
        childUser = new User("Alice", 8, Rating.PG);
        adultUser = new User("Bob", 25, Rating.R);
        childMovie = new Movie(1, "Finding Nemo", Rating.U, "Animation", 2003);
        adultMovie = new Movie(2, "The Matrix", Rating.R, "Action", 1999);
    }

    @Test
    @DisplayName("Should allow access when user rating permits movie")
    void testAccessAllowed() {
        ParentalControlService.AccessResult result = service.checkAccess(childUser, "Finding Nemo");
        
        assertTrue(result.isAllowed());
        assertTrue(result.getReason().contains("Access granted"));
        assertTrue(result.getReason().contains("Finding Nemo"));
    }

    @Test
    @DisplayName("Should deny access when user rating doesn't permit movie")
    void testAccessDenied() {
        ParentalControlService.AccessResult result = service.checkAccess(childUser, "The Matrix");
        
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("Access denied"));
        assertTrue(result.getReason().contains("The Matrix"));
        assertTrue(result.getReason().contains("rated R"));
    }

    @Test
    @DisplayName("Should allow adult access to any movie")
    void testAdultAccess() {
        ParentalControlService.AccessResult result = service.checkAccess(adultUser, "The Matrix");
        
        assertTrue(result.isAllowed());
    }

    @Test
    @DisplayName("Should handle movie not found")
    void testMovieNotFound() {
        ParentalControlService.AccessResult result = service.checkAccess(childUser, "NonExistent Movie");
        
        assertFalse(result.isAllowed());
        assertTrue(result.getReason().contains("Movie not found"));
    }

    @Test
    @DisplayName("Should throw exception for null user")
    void testNullUser() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.checkAccess(null, "Any Movie"));
    }

    @Test
    @DisplayName("Should throw exception for null or empty movie title")
    void testInvalidMovieTitle() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.checkAccess(childUser, (String) null));
        assertThrows(IllegalArgumentException.class, () -> 
            service.checkAccess(childUser, ""));
        assertThrows(IllegalArgumentException.class, () -> 
            service.checkAccess(childUser, "   "));
    }

    @Test
    @DisplayName("Should handle direct movie object access check")
    void testDirectMovieAccess() {
        ParentalControlService.AccessResult result = service.checkAccess(childUser, (Movie) childMovie);
        
        assertTrue(result.isAllowed());
    }

    @Test
    @DisplayName("Should throw exception for null movie object")
    void testNullMovieObject() {
        assertThrows(IllegalArgumentException.class, () -> 
            service.checkAccess(childUser, (Movie) null));
    }

    @Test
    @DisplayName("Legacy method should work correctly")
    void testLegacyMethod() {
        String result = service.getParentalControl("Finding Nemo", "PG");
        
        assertEquals("You have permission for this movie", result);
    }

    @Test
    @DisplayName("Legacy method should deny access correctly")
    void testLegacyMethodDenied() {
        String result = service.getParentalControl("The Matrix", "PG");
        
        assertEquals("You can't watch this movie.......", result);
    }

    @Test
    @DisplayName("Legacy method should handle errors")
    void testLegacyMethodError() {
        String result = service.getParentalControl("NonExistentMovie123", "PG");
        
        assertTrue(result.startsWith("Error:"));
    }

    @Test
    @DisplayName("Access result should have timestamp")
    void testAccessResultTimestamp() {
        long beforeCall = System.currentTimeMillis();
        ParentalControlService.AccessResult result = service.checkAccess(childUser, "Finding Nemo");
        long afterCall = System.currentTimeMillis();
        
        assertTrue(result.getTimestamp() >= beforeCall);
        assertTrue(result.getTimestamp() <= afterCall);
    }

    @Test
    @DisplayName("Should get movie service instance")
    void testGetMovieService() {
        assertNotNull(service.getMovieService());
    }

    @Test
    @DisplayName("Access result should have meaningful toString")
    void testAccessResultToString() {
        ParentalControlService.AccessResult result = service.checkAccess(childUser, "Finding Nemo");
        String toString = result.toString();
        
        assertTrue(toString.contains("AccessResult"));
        assertTrue(toString.contains("allowed=true"));
        assertTrue(toString.contains("reason="));
    }
}