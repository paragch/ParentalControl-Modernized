package com.parentalcontrol.model;

import java.util.Objects;

/**
 * Represents a movie with its metadata and parental control rating.
 * 
 * @author Parag Chatterjee (Modernized)
 * @version 2.0
 */
public class Movie {
    private final int id;
    private final String title;
    private final Rating rating;
    private final String genre;
    private final int releaseYear;

    public Movie(int id, String title, Rating rating, String genre, int releaseYear) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Movie title cannot be null or empty");
        }
        if (rating == null) {
            throw new IllegalArgumentException("Movie rating cannot be null");
        }
        
        this.id = id;
        this.title = title.trim();
        this.rating = rating;
        this.genre = genre != null ? genre.trim() : "";
        this.releaseYear = releaseYear;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Rating getRating() {
        return rating;
    }

    public String getGenre() {
        return genre;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Movie movie = (Movie) obj;
        return id == movie.id && Objects.equals(title, movie.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', rating=%s, genre='%s', year=%d}", 
                           id, title, rating, genre, releaseYear);
    }
}