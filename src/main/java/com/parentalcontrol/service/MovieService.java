package com.parentalcontrol.service;

import com.parentalcontrol.model.Movie;
import com.parentalcontrol.model.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing movie data and operations.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public class MovieService {
    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);
    
    private final Map<Integer, Movie> movies;
    private final Map<String, Movie> moviesByTitle;

    public MovieService() {
        this.movies = new HashMap<>();
        this.moviesByTitle = new HashMap<>();
        initializeMovies();
    }

    /**
     * Initialize the movie database with sample data.
     * In a real application, this would load from a database or external source.
     */
    private void initializeMovies() {
        addMovie(new Movie(1, "Baby's Day Out", Rating.U, "Comedy", 1994));
        addMovie(new Movie(2, "Notting Hill", Rating.PG_13, "Romance", 1999));
        addMovie(new Movie(3, "The Lion King", Rating.PG, "Animation", 1994));
        addMovie(new Movie(4, "Inception", Rating.PG_13, "Sci-Fi", 2010));
        addMovie(new Movie(5, "The Matrix", Rating.R, "Action", 1999));
        addMovie(new Movie(6, "Finding Nemo", Rating.U, "Animation", 2003));
        addMovie(new Movie(7, "The Dark Knight", Rating.PG_13, "Action", 2008));
        addMovie(new Movie(8, "Deadpool", Rating.R, "Action", 2016));
        
        logger.info("Initialized movie database with {} movies", movies.size());
    }

    /**
     * Adds a movie to the service.
     * 
     * @param movie the movie to add
     */
    private void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
        moviesByTitle.put(movie.getTitle().toLowerCase(), movie);
    }

    /**
     * Finds a movie by its exact title (case-insensitive).
     * 
     * @param title the movie title to search for
     * @return the movie if found
     * @throws IllegalArgumentException if the movie is not found or title is invalid
     */
    public Movie findMovieByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        
        Movie movie = moviesByTitle.get(title.trim().toLowerCase());
        if (movie == null) {
            logger.warn("Movie not found: {}", title);
            throw new IllegalArgumentException("Movie not found: " + title);
        }
        
        logger.debug("Found movie: {}", movie);
        return movie;
    }

    /**
     * Finds a movie by its ID.
     * 
     * @param movieId the movie ID
     * @return the movie if found
     * @throws IllegalArgumentException if the movie is not found
     */
    public Movie findMovieById(int movieId) {
        Movie movie = movies.get(movieId);
        if (movie == null) {
            logger.warn("Movie not found with ID: {}", movieId);
            throw new IllegalArgumentException("Movie not found with ID: " + movieId);
        }
        
        logger.debug("Found movie: {}", movie);
        return movie;
    }

    /**
     * Searches for movies by partial title match (case-insensitive).
     * 
     * @param partialTitle the partial title to search for
     * @return list of matching movies
     */
    public List<Movie> searchMoviesByTitle(String partialTitle) {
        if (partialTitle == null || partialTitle.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = partialTitle.trim().toLowerCase();
        List<Movie> results = movies.values().stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
        
        logger.debug("Search for '{}' returned {} results", partialTitle, results.size());
        return results;
    }

    /**
     * Gets all movies with a specific rating.
     * 
     * @param rating the rating to filter by
     * @return list of movies with the specified rating
     */
    public List<Movie> getMoviesByRating(Rating rating) {
        if (rating == null) {
            return new ArrayList<>();
        }
        
        return movies.values().stream()
                .filter(movie -> movie.getRating() == rating)
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Gets all movies that are accessible with the given parental control rating.
     * 
     * @param maxRating the maximum allowed rating
     * @return list of accessible movies
     */
    public List<Movie> getAccessibleMovies(Rating maxRating) {
        if (maxRating == null) {
            return new ArrayList<>();
        }
        
        return movies.values().stream()
                .filter(movie -> movie.getRating().isAccessibleWith(maxRating))
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Gets all movies in the database.
     * 
     * @return list of all movies
     */
    public List<Movie> getAllMovies() {
        return movies.values().stream()
                .sorted(Comparator.comparing(Movie::getTitle))
                .collect(Collectors.toList());
    }

    /**
     * Gets movie statistics by rating.
     * 
     * @return map of rating to count
     */
    public Map<Rating, Long> getMovieCountByRating() {
        return movies.values().stream()
                .collect(Collectors.groupingBy(Movie::getRating, Collectors.counting()));
    }

    /**
     * Checks if a movie exists by title.
     * 
     * @param title the movie title
     * @return true if the movie exists
     */
    public boolean movieExists(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        return moviesByTitle.containsKey(title.trim().toLowerCase());
    }
}