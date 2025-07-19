package com.parentalcontrol.model;

import java.util.Objects;

/**
 * Represents a user with parental control settings.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public class User {
    private final String username;
    private final int age;
    private final Rating maxAllowedRating;
    private final boolean isAdult;

    public User(String username, int age, Rating maxAllowedRating) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        if (maxAllowedRating == null) {
            throw new IllegalArgumentException("Max allowed rating cannot be null");
        }
        
        this.username = username.trim();
        this.age = age;
        this.maxAllowedRating = maxAllowedRating;
        this.isAdult = age >= 18;
    }

    /**
     * Creates a user with age-appropriate default rating.
     * 
     * @param username the username
     * @param age the user's age
     * @return a new User with appropriate default rating
     */
    public static User createWithDefaultRating(String username, int age) {
        Rating defaultRating;
        
        if (age < 8) {
            defaultRating = Rating.U;
        } else if (age < 12) {
            defaultRating = Rating.PG;
        } else if (age < 13) {
            defaultRating = Rating.TWELVE;
        } else if (age < 15) {
            defaultRating = Rating.PG_13;
        } else if (age < 18) {
            defaultRating = Rating.FIFTEEN;
        } else {
            defaultRating = Rating.EIGHTEEN;
        }
        
        return new User(username, age, defaultRating);
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public Rating getMaxAllowedRating() {
        return maxAllowedRating;
    }

    public boolean isAdult() {
        return isAdult;
    }

    /**
     * Checks if this user can watch a movie with the given rating.
     * 
     * @param movieRating the rating of the movie to check
     * @return true if the user can watch the movie
     */
    public boolean canWatch(Rating movieRating) {
        return movieRating.isAccessibleWith(maxAllowedRating);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', age=%d, maxRating=%s, isAdult=%b}", 
                           username, age, maxAllowedRating, isAdult);
    }
}