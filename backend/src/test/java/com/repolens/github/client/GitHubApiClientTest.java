package com.repolens.github.client;

import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GitHubApiClientTest {

    private static final String REPOSITORY_RESPONSE = """
            {
              "id": 123456,
              "name": "test-repo",
              "full_name": "test-owner/test-repo",
              "html_url": "https://github.com/test-owner/test-repo",
              "description": "Test repository",
              "default_branch": "main",
              "visibility": "public",
              "stargazers_count": 120,
              "forks_count": 35,
              "language": "Java",
              "owner": {
                "login": "test-owner"
              }
            }
            """;

    private MockWebServer mockWebServer;
    private GitHubApiClient client;
    private GitHubClientProperties properties;

    @BeforeEach
    void setUp() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        properties = new GitHubClientProperties(
                mockWebServer.url("/").toString(),
                "test-token"
        );

        client = new GitHubApiClient(
                RestClient.builder(),
                properties
        );
    }

    @AfterEach
    void tearDown() throws IOException {

        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    void shouldFetchRepositorySuccessfully() throws Exception {

        // Arrange
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .addHeader(
                                HttpHeaders.CONTENT_TYPE,
                                MediaType.APPLICATION_JSON_VALUE
                        )
                        .setBody(REPOSITORY_RESPONSE)
        );

        // Act
        GitHubRepositoryResponse response =
                client.getRepository(coordinates());

        // Assert Response
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(123456L, response.id()),
                () -> assertEquals("test-repo", response.name()),
                () -> assertEquals("Java", response.language()),
                () -> assertNotNull(response.owner()),
                () -> assertEquals("test-owner", response.owner().login()),
                () -> assertEquals(120, response.stars()),
                () -> assertEquals(35, response.forks())
        );

        // Assert HTTP Request
        RecordedRequest request = mockWebServer.takeRequest();

        assertNotNull(request);

        assertAll(
                () -> assertEquals("GET", request.getMethod()),
                () -> assertEquals(
                        "/repos/test-owner/test-repo",
                        request.getPath()
                ),
                () -> assertEquals(
                        "Bearer test-token",
                        request.getHeader(HttpHeaders.AUTHORIZATION)
                ),
                () -> assertEquals(
                        "application/vnd.github+json",
                        request.getHeader(HttpHeaders.ACCEPT)
                ),
                () -> assertEquals(
                        "2022-11-28",
                        request.getHeader("X-GitHub-Api-Version")
                )
        );
    }

    private GitHubRepositoryCoordinates coordinates() {
        return new GitHubRepositoryCoordinates(
                "test-owner",
                "test-repo"
        );
    }
}