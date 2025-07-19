package com.parentalcontrol.model;

/**
 * Enum representing parental control ratings in order of restrictiveness.
 * Lower ordinal values are less restrictive.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public enum Rating {
    U("Universal", "Suitable for all ages", 0),
    PG("Parental Guidance", "General viewing, some scenes may be unsuitable for young children", 8),
    PG_13("Parental Guidance 13", "Some material may be inappropriate for children under 13", 13),
    TWELVE("12", "Suitable only for persons of 12 years and over", 12),
    FIFTEEN("15", "Suitable only for persons of 15 years and over", 15),
    EIGHTEEN("18", "Suitable only for persons of 18 years and over", 18),
    R("Restricted", "Under 17 requires accompanying parent or adult guardian", 17);

    private final String displayName;
    private final String description;
    private final int minimumAge;

    Rating(String displayName, String description, int minimumAge) {
        this.displayName = displayName;
        this.description = description;
        this.minimumAge = minimumAge;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getMinimumAge() {
        return minimumAge;
    }

    /**
     * Checks if this rating is more restrictive than the given rating.
     * 
     * @param other the rating to compare against
     * @return true if this rating is more restrictive
     */
    public boolean isMoreRestrictiveThan(Rating other) {
        return this.ordinal() > other.ordinal();
    }

    /**
     * Checks if this rating is less restrictive than or equal to the given rating.
     * 
     * @param other the rating to compare against
     * @return true if this rating is accessible with the given parental control level
     */
    public boolean isAccessibleWith(Rating other) {
        return this.ordinal() <= other.ordinal();
    }

    /**
     * Finds a Rating by its display name or enum name.
     * 
     * @param name the name to search for
     * @return the matching Rating
     * @throws IllegalArgumentException if no matching rating is found
     */
    public static Rating fromString(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Rating name cannot be null or empty");
        }
        
        String trimmedName = name.trim().toUpperCase();
        
        // Try exact enum name match first
        try {
            return Rating.valueOf(trimmedName.replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            // Try display name match
            for (Rating rating : Rating.values()) {
                if (rating.displayName.equalsIgnoreCase(name.trim()) || 
                    rating.name().equalsIgnoreCase(trimmedName)) {
                    return rating;
                }
            }
        }
        
        throw new IllegalArgumentException("Unknown rating: " + name);
    }

    @Override
    public String toString() {
        return displayName;
    }
}