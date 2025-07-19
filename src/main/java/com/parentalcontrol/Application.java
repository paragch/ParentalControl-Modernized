package com.parentalcontrol;

import com.parentalcontrol.model.Rating;
import com.parentalcontrol.model.User;
import com.parentalcontrol.service.ParentalControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class demonstrating the parental control system.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        logger.info("Starting Parental Control System Demo");
        
        ParentalControlService service = new ParentalControlService();
        
        // Create different users with various age groups and restrictions
        User child = User.createWithDefaultRating("Alice", 8);
        User teenager = User.createWithDefaultRating("Bob", 15);
        User adult = new User("Charlie", 25, Rating.EIGHTEEN);
        User restrictedAdult = new User("Diana", 30, Rating.PG);

        System.out.println("=== Parental Control System Demo ===\n");
        
        // Test different scenarios
        testMovieAccess(service, child, "Baby's Day Out");
        testMovieAccess(service, child, "The Matrix");
        testMovieAccess(service, teenager, "Inception");
        testMovieAccess(service, teenager, "Deadpool");
        testMovieAccess(service, adult, "The Dark Knight");
        testMovieAccess(service, restrictedAdult, "Finding Nemo");
        testMovieAccess(service, restrictedAdult, "The Matrix");

        // Test error handling
        System.out.println("\n=== Error Handling Tests ===");
        testMovieAccess(service, child, "NonExistentMovie");
        
        // Show movie statistics
        System.out.println("\n=== Movie Database Statistics ===");
        service.getMovieService().getMovieCountByRating().forEach((rating, count) -> 
            System.out.printf("%s: %d movies%n", rating.getDisplayName(), count));
        
        // Test legacy method
        System.out.println("\n=== Legacy Method Test ===");
        String legacyResult = service.getParentalControl("Baby's Day Out", "PG");
        System.out.println("Legacy result: " + legacyResult);
        
        logger.info("Parental Control System Demo completed");
    }
    
    private static void testMovieAccess(ParentalControlService service, User user, String movieTitle) {
        System.out.printf("User: %s (age %d, max rating: %s)%n", 
                         user.getUsername(), user.getAge(), user.getMaxAllowedRating().getDisplayName());
        System.out.printf("Movie: %s%n", movieTitle);
        
        try {
            ParentalControlService.AccessResult result = service.checkAccess(user, movieTitle);
            System.out.printf("Result: %s%n", result.getReason());
        } catch (Exception e) {
            System.out.printf("Error: %s%n", e.getMessage());
        }
        System.out.println();
    }
}