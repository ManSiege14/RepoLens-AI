package com.repolens.repository.mapper;

import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.repository.domain.GitRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryMapperTest {

    @Test
    void shouldMapGitHubRepositoryResponseToEntity() {

        GitHubRepositoryResponse response = createResponse();

        GitRepository repository = RepositoryMapper.toEntity(response);

        assertAll(
                () -> assertEquals(123456789L, repository.getGithubRepositoryId()),
                () -> assertEquals("mansij", repository.getOwner()),
                () -> assertEquals("RepoLens", repository.getName()),
                () -> assertEquals("mansij/RepoLens", repository.getFullName()),
                () -> assertEquals("https://github.com/mansij/RepoLens", repository.getHtmlUrl()),
                () -> assertEquals("Repository analysis tool", repository.getDescription()),
                () -> assertEquals("main", repository.getDefaultBranch()),
                () -> assertEquals("public", repository.getVisibility()),
                () -> assertEquals(250, repository.getStars()),
                () -> assertEquals(35, repository.getForks()),
                () -> assertEquals("Java", repository.getPrimaryLanguage())
        );
    }

    @Test
    void shouldMapNullDescription() {

        GitHubRepositoryResponse response = createResponseWithDescription(null);

        GitRepository repository = RepositoryMapper.toEntity(response);

        assertNull(repository.getDescription());
    }

    @Test
    void shouldMapLanguageCorrectly() {

        GitHubRepositoryResponse response = createResponseWithLanguage("Kotlin");

        GitRepository repository = RepositoryMapper.toEntity(response);

        assertEquals("Kotlin", repository.getPrimaryLanguage());
    }

    @Test
    void shouldMapOwnerLogin() {

        GitHubRepositoryResponse response = createResponseWithOwner("octocat");

        GitRepository repository = RepositoryMapper.toEntity(response);

        assertEquals("octocat", repository.getOwner());
    }

    private GitHubRepositoryResponse createResponse() {
        return new GitHubRepositoryResponse(
                123456789L,
                "RepoLens",
                "mansij/RepoLens",
                "https://github.com/mansij/RepoLens",
                "Repository analysis tool",
                "main",
                "public",
                250,
                35,
                "Java",
                new GitHubRepositoryResponse.Owner("mansij")
        );
    }

    private GitHubRepositoryResponse createResponseWithDescription(String description) {
        return new GitHubRepositoryResponse(
                123456789L,
                "RepoLens",
                "mansij/RepoLens",
                "https://github.com/mansij/RepoLens",
                description,
                "main",
                "public",
                250,
                35,
                "Java",
                new GitHubRepositoryResponse.Owner("mansij")
        );
    }

    private GitHubRepositoryResponse createResponseWithLanguage(String language) {
        return new GitHubRepositoryResponse(
                123456789L,
                "RepoLens",
                "mansij/RepoLens",
                "https://github.com/mansij/RepoLens",
                "Repository analysis tool",
                "main",
                "public",
                250,
                35,
                language,
                new GitHubRepositoryResponse.Owner("mansij")
        );
    }

    private GitHubRepositoryResponse createResponseWithOwner(String owner) {
        return new GitHubRepositoryResponse(
                123456789L,
                "RepoLens",
                "mansij/RepoLens",
                "https://github.com/mansij/RepoLens",
                "Repository analysis tool",
                "main",
                "public",
                250,
                35,
                "Java",
                new GitHubRepositoryResponse.Owner(owner)
        );
    }
}