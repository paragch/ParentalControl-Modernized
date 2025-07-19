package com.parentalcontrol.service;

import com.parentalcontrol.model.Movie;
import com.parentalcontrol.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for MovieService.
 */
class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should find movie by exact title")
    void testFindMovieByTitle() {
        Movie movie = movieService.findMovieByTitle("Baby's Day Out");
        
        assertNotNull(movie);
        assertEquals("Baby's Day Out", movie.getTitle());
        assertEquals(Rating.U, movie.getRating());
    }

    @Test
    @DisplayName("Should find movie by title case insensitive")
    void testFindMovieByCaseInsensitiveTitle() {
        Movie movie1 = movieService.findMovieByTitle("baby's day out");
        Movie movie2 = movieService.findMovieByTitle("BABY'S DAY OUT");
        Movie movie3 = movieService.findMovieByTitle("Baby's Day Out");
        
        assertEquals(movie1, movie2);
        assertEquals(movie2, movie3);
    }

    @Test
    @DisplayName("Should throw exception for non-existent movie")
    void testMovieNotFound() {
        assertThrows(IllegalArgumentException.class, () -> 
            movieService.findMovieByTitle("Non-existent Movie"));
    }

    @Test
    @DisplayName("Should throw exception for null or empty title")
    void testInvalidTitleInput() {
        assertThrows(IllegalArgumentException.class, () -> 
            movieService.findMovieByTitle(null));
        assertThrows(IllegalArgumentException.class, () -> 
            movieService.findMovieByTitle(""));
        assertThrows(IllegalArgumentException.class, () -> 
            movieService.findMovieByTitle("   "));
    }

    @Test
    @DisplayName("Should find movie by ID")
    void testFindMovieById() {
        Movie movie = movieService.findMovieById(1);
        
        assertNotNull(movie);
        assertEquals(1, movie.getId());
        assertEquals("Baby's Day Out", movie.getTitle());
    }

    @Test
    @DisplayName("Should throw exception for non-existent movie ID")
    void testMovieNotFoundById() {
        assertThrows(IllegalArgumentException.class, () -> 
            movieService.findMovieById(999));
    }

    @Test
    @DisplayName("Should search movies by partial title")
    void testSearchMoviesByPartialTitle() {
        List<Movie> results = movieService.searchMoviesByTitle("the");
        
        assertFalse(results.isEmpty());
        assertTrue(results.stream().anyMatch(movie -> movie.getTitle().contains("The")));
    }

    @Test
    @DisplayName("Should return empty list for empty search term")
    void testSearchWithEmptyTerm() {
        List<Movie> results = movieService.searchMoviesByTitle("");
        assertTrue(results.isEmpty());
        
        results = movieService.searchMoviesByTitle(null);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should get movies by rating")
    void testGetMoviesByRating() {
        List<Movie> uMovies = movieService.getMoviesByRating(Rating.U);
        List<Movie> rMovies = movieService.getMoviesByRating(Rating.R);
        
        assertFalse(uMovies.isEmpty());
        assertTrue(uMovies.stream().allMatch(movie -> movie.getRating() == Rating.U));
        
        assertFalse(rMovies.isEmpty());
        assertTrue(rMovies.stream().allMatch(movie -> movie.getRating() == Rating.R));
    }

    @Test
    @DisplayName("Should return empty list for null rating")
    void testGetMoviesByNullRating() {
        List<Movie> results = movieService.getMoviesByRating(null);
        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should get accessible movies for given rating")
    void testGetAccessibleMovies() {
        List<Movie> accessibleForPG = movieService.getAccessibleMovies(Rating.PG);
        List<Movie> accessibleForR = movieService.getAccessibleMovies(Rating.R);
        
        // PG should include U and PG movies
        assertTrue(accessibleForPG.stream().anyMatch(movie -> movie.getRating() == Rating.U));
        assertTrue(accessibleForPG.stream().anyMatch(movie -> movie.getRating() == Rating.PG));
        assertFalse(accessibleForPG.stream().anyMatch(movie -> movie.getRating() == Rating.R));
        
        // R rating should include all movies
        assertTrue(accessibleForR.size() >= accessibleForPG.size());
    }

    @Test
    @DisplayName("Should get all movies")
    void testGetAllMovies() {
        List<Movie> allMovies = movieService.getAllMovies();
        
        assertFalse(allMovies.isEmpty());
        assertTrue(allMovies.size() >= 8); // We initialized with 8 movies
        
        // Should be sorted by title
        for (int i = 1; i < allMovies.size(); i++) {
            assertTrue(allMovies.get(i - 1).getTitle().compareToIgnoreCase(allMovies.get(i).getTitle()) <= 0);
        }
    }

    @Test
    @DisplayName("Should get movie count by rating")
    void testGetMovieCountByRating() {
        Map<Rating, Long> counts = movieService.getMovieCountByRating();
        
        assertFalse(counts.isEmpty());
        assertTrue(counts.get(Rating.U) >= 2); // At least Baby's Day Out and Finding Nemo
        assertTrue(counts.get(Rating.R) >= 2); // At least The Matrix and Deadpool
    }

    @Test
    @DisplayName("Should check if movie exists")
    void testMovieExists() {
        assertTrue(movieService.movieExists("Baby's Day Out"));
        assertTrue(movieService.movieExists("baby's day out")); // Case insensitive
        assertFalse(movieService.movieExists("Non-existent Movie"));
        assertFalse(movieService.movieExists(null));
        assertFalse(movieService.movieExists(""));
    }

    @Test
    @DisplayName("Should handle edge cases in search")
    void testSearchEdgeCases() {
        // Search with whitespace
        List<Movie> results = movieService.searchMoviesByTitle("  the  ");
        assertFalse(results.isEmpty());
        
        // Case insensitive search
        List<Movie> results1 = movieService.searchMoviesByTitle("LION");
        List<Movie> results2 = movieService.searchMoviesByTitle("lion");
        assertEquals(results1.size(), results2.size());
    }

    @Test
    @DisplayName("Should maintain data integrity")
    void testDataIntegrity() {
        // Verify specific movies exist with correct ratings
        Movie babysDay = movieService.findMovieByTitle("Baby's Day Out");
        assertEquals(Rating.U, babysDay.getRating());
        assertEquals(1994, babysDay.getReleaseYear());
        
        Movie matrix = movieService.findMovieByTitle("The Matrix");
        assertEquals(Rating.R, matrix.getRating());
        assertEquals(1999, matrix.getReleaseYear());
        
        Movie nemo = movieService.findMovieByTitle("Finding Nemo");
        assertEquals(Rating.U, nemo.getRating());
        assertEquals("Animation", nemo.getGenre());
    }
}