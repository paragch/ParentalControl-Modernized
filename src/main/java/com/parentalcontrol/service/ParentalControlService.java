package com.parentalcontrol.service;

import com.parentalcontrol.model.Movie;
import com.parentalcontrol.model.Rating;
import com.parentalcontrol.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main service for parental control functionality.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public class ParentalControlService {
    private static final Logger logger = LoggerFactory.getLogger(ParentalControlService.class);
    
    private final MovieService movieService;

    public ParentalControlService(MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Default constructor with new MovieService instance.
     */
    public ParentalControlService() {
        this.movieService = new MovieService();
    }

    /**
     * Checks if a user can watch a specific movie.
     * 
     * @param user the user requesting access
     * @param movieTitle the title of the movie
     * @return AccessResult containing the decision and reason
     */
    public AccessResult checkAccess(User user, String movieTitle) {
        if (user == null) {
            logger.error("Access check failed: user is null");
            throw new IllegalArgumentException("User cannot be null");
        }
        
        if (movieTitle == null || movieTitle.trim().isEmpty()) {
            logger.error("Access check failed: movie title is null or empty");
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }

        try {
            Movie movie = movieService.findMovieByTitle(movieTitle);
            return checkAccess(user, movie);
        } catch (IllegalArgumentException e) {
            logger.warn("Movie not found for access check: {} (user: {})", movieTitle, user.getUsername());
            return AccessResult.denied("Movie not found: " + movieTitle);
        }
    }

    /**
     * Checks if a user can watch a specific movie.
     * 
     * @param user the user requesting access
     * @param movie the movie
     * @return AccessResult containing the decision and reason
     */
    public AccessResult checkAccess(User user, Movie movie) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null");
        }

        Rating movieRating = movie.getRating();
        Rating userMaxRating = user.getMaxAllowedRating();
        
        boolean canWatch = user.canWatch(movieRating);
        
        if (canWatch) {
            logger.info("Access granted: {} can watch '{}' (rating: {})", 
                       user.getUsername(), movie.getTitle(), movieRating);
            return AccessResult.allowed(
                String.format("Access granted. You can watch '%s' (rated %s)", 
                             movie.getTitle(), movieRating.getDisplayName())
            );
        } else {
            logger.info("Access denied: {} cannot watch '{}' (movie rating: {}, user max: {})", 
                       user.getUsername(), movie.getTitle(), movieRating, userMaxRating);
            return AccessResult.denied(
                String.format("Access denied. '%s' is rated %s, but your maximum allowed rating is %s", 
                             movie.getTitle(), movieRating.getDisplayName(), userMaxRating.getDisplayName())
            );
        }
    }

    /**
     * Legacy method for backward compatibility.
     * 
     * @param movieTitle the movie title
     * @param userRating the user's parental control rating as string
     * @return simple string response
     * @deprecated Use checkAccess(User, String) instead
     */
    @Deprecated
    public String getParentalControl(String movieTitle, String userRating) {
        logger.warn("Using deprecated method getParentalControl");
        
        try {
            Rating rating = Rating.fromString(userRating);
            User user = new User("legacy_user", 18, rating);
            AccessResult result = checkAccess(user, movieTitle);
            
            return result.isAllowed() ? 
                "You have permission for this movie" : 
                "You can't watch this movie.......";
        } catch (Exception e) {
            logger.error("Error in legacy method: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Gets the movie service instance.
     * 
     * @return the movie service
     */
    public MovieService getMovieService() {
        return movieService;
    }

    /**
     * Result class for access control decisions.
     */
    public static class AccessResult {
        private final boolean allowed;
        private final String reason;
        private final long timestamp;

        private AccessResult(boolean allowed, String reason) {
            this.allowed = allowed;
            this.reason = reason;
            this.timestamp = System.currentTimeMillis();
        }

        public static AccessResult allowed(String reason) {
            return new AccessResult(true, reason);
        }

        public static AccessResult denied(String reason) {
            return new AccessResult(false, reason);
        }

        public boolean isAllowed() {
            return allowed;
        }

        public String getReason() {
            return reason;
        }

        public long getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return String.format("AccessResult{allowed=%s, reason='%s'}", allowed, reason);
        }
    }
}