package com.parentalcontrol.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Movie class.
 */
class MovieTest {

    @Test
    @DisplayName("Should create movie with valid parameters")
    void testValidMovieCreation() {
        Movie movie = new Movie(1, "Test Movie", Rating.PG, "Comedy", 2023);
        
        assertEquals(1, movie.getId());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals(Rating.PG, movie.getRating());
        assertEquals("Comedy", movie.getGenre());
        assertEquals(2023, movie.getReleaseYear());
    }

    @Test
    @DisplayName("Should throw exception for invalid title")
    void testInvalidTitle() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Movie(1, null, Rating.PG, "Comedy", 2023));
        assertThrows(IllegalArgumentException.class, () -> 
            new Movie(1, "", Rating.PG, "Comedy", 2023));
        assertThrows(IllegalArgumentException.class, () -> 
            new Movie(1, "   ", Rating.PG, "Comedy", 2023));
    }

    @Test
    @DisplayName("Should throw exception for null rating")
    void testNullRating() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Movie(1, "Test Movie", null, "Comedy", 2023));
    }

    @Test
    @DisplayName("Should handle null genre gracefully")
    void testNullGenre() {
        Movie movie = new Movie(1, "Test Movie", Rating.PG, null, 2023);
        assertEquals("", movie.getGenre());
    }

    @Test
    @DisplayName("Should trim title and genre")
    void testTrimming() {
        Movie movie = new Movie(1, "  Test Movie  ", Rating.PG, "  Comedy  ", 2023);
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("Comedy", movie.getGenre());
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void testEqualsAndHashCode() {
        Movie movie1 = new Movie(1, "Test Movie", Rating.PG, "Comedy", 2023);
        Movie movie2 = new Movie(1, "Test Movie", Rating.R, "Drama", 2024); // Same id and title
        Movie movie3 = new Movie(2, "Different Movie", Rating.PG, "Comedy", 2023);
        Movie movie4 = new Movie(1, "Different Title", Rating.PG, "Comedy", 2023);
        
        assertEquals(movie1, movie2); // Same id and title
        assertNotEquals(movie1, movie3); // Different id
        assertNotEquals(movie1, movie4); // Different title
        assertEquals(movie1.hashCode(), movie2.hashCode());
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void testToString() {
        Movie movie = new Movie(1, "Test Movie", Rating.PG, "Comedy", 2023);
        String toString = movie.toString();
        
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("Test Movie"));
        assertTrue(toString.contains("PG"));
        assertTrue(toString.contains("Comedy"));
        assertTrue(toString.contains("2023"));
    }

    @Test
    @DisplayName("Should handle edge cases in constructor")
    void testEdgeCases() {
        // Should work with empty genre after trimming
        Movie movie1 = new Movie(1, "Movie", Rating.U, "  ", 1900);
        assertEquals("", movie1.getGenre());
        
        // Should work with old year
        assertEquals(1900, movie1.getReleaseYear());
        
        // Should work with future year
        Movie movie2 = new Movie(2, "Future Movie", Rating.PG, "Sci-Fi", 3000);
        assertEquals(3000, movie2.getReleaseYear());
    }
}