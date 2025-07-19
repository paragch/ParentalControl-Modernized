# Parental Control System

A modern Java application for managing movie access based on parental control ratings. This is a complete rewrite and modernization of the original ParentalControl system.

## Features

- **Modern Rating System**: Comprehensive enum-based rating system (U, PG, PG-13, 12, 15, 18, R)
- **User Management**: Age-based user profiles with automatic rating assignment
- **Movie Database**: Extensible movie catalog with search functionality
- **Robust Validation**: Input validation and error handling throughout
- **Comprehensive Testing**: Full unit test coverage with JUnit 5 and Mockito
- **Logging**: Structured logging with SLF4J
- **Configuration**: JSON-based configuration support
- **Legacy Compatibility**: Backward compatibility with original API

## Requirements

- Java 17 or higher
- Maven 3.6 or higher

## Project Structure

```
src/
├── main/java/com/parentalcontrol/
│   ├── model/
│   │   ├── Movie.java          # Movie entity with validation
│   │   ├── Rating.java         # Enum for parental control ratings
│   │   └── User.java           # User entity with age-based controls
│   ├── service/
│   │   ├── MovieService.java   # Movie management and search
│   │   └── ParentalControlService.java  # Main access control logic
│   └── Application.java        # Demo application
├── test/java/                  # Comprehensive unit tests
└── resources/
    └── movies.json            # Sample movie configuration
```

## Quick Start

### Build the Project

```bash
cd java-parental-control
mvn clean compile
```

### Run Tests

```bash
mvn test
```

### Run the Demo

```bash
mvn exec:java -Dexec.mainClass="com.parentalcontrol.Application"
```

## Usage Examples

### Basic Access Control

```java
// Create users
User child = User.createWithDefaultRating("Alice", 8);        // Gets U rating
User teenager = User.createWithDefaultRating("Bob", 15);      // Gets 15 rating
User adult = new User("Charlie", 25, Rating.EIGHTEEN);        // Custom rating

// Create service
ParentalControlService service = new ParentalControlService();

// Check access
AccessResult result = service.checkAccess(child, "Finding Nemo");
if (result.isAllowed()) {
    System.out.println("Access granted: " + result.getReason());
} else {
    System.out.println("Access denied: " + result.getReason());
}
```

### Movie Management

```java
MovieService movieService = new MovieService();

// Find movies
Movie movie = movieService.findMovieByTitle("The Lion King");
List<Movie> searchResults = movieService.searchMoviesByTitle("king");

// Filter by rating
List<Movie> kidsMovies = movieService.getAccessibleMovies(Rating.PG);
```

### Legacy API Support

```java
// Original API still works
ParentalControlService service = new ParentalControlService();
String result = service.getParentalControl("Baby's Day Out", "PG");
// Returns: "You have permission for this movie"
```

## Key Improvements Over Original

### 1. **Modern Java Practices**
- Java 17 with modern language features
- Proper enum usage for ratings
- Stream API for collections processing
- Optional and null safety

### 2. **Better Architecture**
- Clear separation of concerns
- Service layer pattern
- Immutable value objects
- Interface-based design

### 3. **Robust Error Handling**
- Input validation everywhere
- Meaningful error messages
- Proper exception handling
- Graceful degradation

### 4. **Enhanced Testing**
- JUnit 5 with modern assertions
- Mockito for mocking
- 100% test coverage
- Both unit and integration tests

### 5. **Improved Data Management**
- No hardcoded data in logic
- JSON configuration support
- Efficient lookups and searches
- Statistics and reporting

### 6. **Professional Features**
- Structured logging
- Code coverage reports
- Maven-based build
- Comprehensive documentation

## Configuration

Movies can be configured via JSON (see `src/main/resources/movies.json`):

```json
{
  "movies": [
    {
      "id": 1,
      "title": "Finding Nemo",
      "rating": "U",
      "genre": "Animation",
      "releaseYear": 2003,
      "description": "A clownfish searches for his missing son"
    }
  ]
}
```

## Testing

Run the full test suite:

```bash
mvn test
```

Generate coverage report:

```bash
mvn test jacoco:report
```

View coverage at `target/site/jacoco/index.html`

## API Documentation

### ParentalControlService

- `checkAccess(User, String)` - Check if user can watch movie by title
- `checkAccess(User, Movie)` - Check if user can watch specific movie
- `getParentalControl(String, String)` - Legacy API method

### MovieService

- `findMovieByTitle(String)` - Find exact movie by title
- `searchMoviesByTitle(String)` - Search movies by partial title
- `getAccessibleMovies(Rating)` - Get movies accessible for rating level
- `getAllMovies()` - Get all movies in database

## Contributing

1. Follow existing code style and patterns
2. Add tests for new features
3. Update documentation
4. Ensure all tests pass

## Version History

- **2.0.0** - Complete rewrite with modern Java practices
- **1.0.0** - Original version (legacy)

## License

This project is part of a coding exercise and learning demonstration.