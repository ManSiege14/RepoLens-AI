package com.repolens.github.validation;

import com.repolens.github.model.GitHubRepositoryCoordinates;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GitHubRepositoryUrlParserTest {

    private final GitHubRepositoryUrlParser parser = new GitHubRepositoryUrlParser();

    @Test
    void shouldParseCanonicalGitHubRepositoryUrl() {
        // Arrange
        String url = "https://github.com/spring-projects/spring-petclinic";

        // Act
        GitHubRepositoryCoordinates result = parser.parse(url);

        // Assert
        assertEquals("spring-projects", result.owner());
        assertEquals("spring-petclinic", result.repository());
    }

    @Test
    void shouldParseUrlWithTrailingSlash() {
        // Arrange
        String url = "https://github.com/spring-projects/spring-petclinic/";

        // Act
        GitHubRepositoryCoordinates result = parser.parse(url);

        // Assert
        assertEquals("spring-projects", result.owner());
        assertEquals("spring-petclinic", result.repository());
    }

    @Test
    void shouldParseUrlWithGitSuffix() {
        // Arrange
        String url = "https://github.com/spring-projects/spring-petclinic.git";

        // Act
        GitHubRepositoryCoordinates result = parser.parse(url);

        // Assert
        assertEquals("spring-projects", result.owner());
        assertEquals("spring-petclinic", result.repository());
    }

    @Test
    void shouldThrowExceptionWhenUrlIsNull() {
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse(null));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsBlank() {
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse("   "));
    }

    @Test
    void shouldThrowExceptionWhenUrlUsesHttpInsteadOfHttps() {
        // Arrange
        String url = "http://github.com/spring-projects/spring-petclinic";
        
        // Assert
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse(url));
    }

    @Test
    void shouldThrowExceptionWhenUrlIsNotGitHub() {
        // Arrange
        String url = "https://gitlab.com/spring-projects/spring-petclinic";
        
        // Assert
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse(url));
    }

    @Test
    void shouldThrowExceptionWhenRepositoryNameIsMissing() {
        // Arrange
        String url = "https://github.com/spring-projects";
        
        // Assert
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse(url));
    }

    @Test
    void shouldThrowExceptionWhenUrlHasExtraPath() {
        // Arrange
        String url = "https://github.com/spring-projects/spring-petclinic/issues";
        
        // Assert
        assertThrows(InvalidGitHubRepositoryUrlException.class, () -> parser.parse(url));
    }
}