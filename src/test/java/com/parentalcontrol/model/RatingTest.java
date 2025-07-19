package com.parentalcontrol.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Rating enum.
 */
class RatingTest {

    @Test
    @DisplayName("Should correctly identify more restrictive ratings")
    void testIsMoreRestrictiveThan() {
        assertTrue(Rating.EIGHTEEN.isMoreRestrictiveThan(Rating.U));
        assertTrue(Rating.PG_13.isMoreRestrictiveThan(Rating.PG));
        assertFalse(Rating.U.isMoreRestrictiveThan(Rating.PG));
        assertFalse(Rating.PG.isMoreRestrictiveThan(Rating.PG)); // Same rating
    }

    @Test
    @DisplayName("Should correctly identify accessible ratings")
    void testIsAccessibleWith() {
        assertTrue(Rating.U.isAccessibleWith(Rating.PG_13));
        assertTrue(Rating.PG.isAccessibleWith(Rating.PG));
        assertTrue(Rating.PG_13.isAccessibleWith(Rating.EIGHTEEN));
        assertFalse(Rating.R.isAccessibleWith(Rating.PG));
        assertFalse(Rating.FIFTEEN.isAccessibleWith(Rating.U));
    }

    @Test
    @DisplayName("Should parse rating from string correctly")
    void testFromString() {
        assertEquals(Rating.U, Rating.fromString("U"));
        assertEquals(Rating.PG, Rating.fromString("Parental Guidance"));
        assertEquals(Rating.PG_13, Rating.fromString("pg_13"));
        assertEquals(Rating.TWELVE, Rating.fromString("12"));
        assertEquals(Rating.FIFTEEN, Rating.fromString("fifteen"));
        assertEquals(Rating.EIGHTEEN, Rating.fromString("18"));
        assertEquals(Rating.R, Rating.fromString("Restricted"));
    }

    @Test
    @DisplayName("Should throw exception for invalid rating string")
    void testFromStringInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Rating.fromString("INVALID"));
        assertThrows(IllegalArgumentException.class, () -> Rating.fromString(""));
        assertThrows(IllegalArgumentException.class, () -> Rating.fromString(null));
    }

    @Test
    @DisplayName("Should have correct minimum ages")
    void testMinimumAges() {
        assertEquals(0, Rating.U.getMinimumAge());
        assertEquals(8, Rating.PG.getMinimumAge());
        assertEquals(13, Rating.PG_13.getMinimumAge());
        assertEquals(12, Rating.TWELVE.getMinimumAge());
        assertEquals(15, Rating.FIFTEEN.getMinimumAge());
        assertEquals(18, Rating.EIGHTEEN.getMinimumAge());
        assertEquals(17, Rating.R.getMinimumAge());
    }

    @Test
    @DisplayName("Should have correct display names")
    void testDisplayNames() {
        assertEquals("Universal", Rating.U.getDisplayName());
        assertEquals("Parental Guidance", Rating.PG.getDisplayName());
        assertEquals("Parental Guidance 13", Rating.PG_13.getDisplayName());
        assertEquals("12", Rating.TWELVE.getDisplayName());
        assertEquals("15", Rating.FIFTEEN.getDisplayName());
        assertEquals("18", Rating.EIGHTEEN.getDisplayName());
        assertEquals("Restricted", Rating.R.getDisplayName());
    }

    @Test
    @DisplayName("Should return display name in toString")
    void testToString() {
        assertEquals("Universal", Rating.U.toString());
        assertEquals("Restricted", Rating.R.toString());
    }
}