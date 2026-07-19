package com.repolens.repository.service;

import com.repolens.github.client.GitHubApiClient;
import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Mock
    private GitRepositoryRepository gitRepositoryRepository;

    @InjectMocks
    private RepositoryService repositoryService;

    @Test
    void shouldImportRepositorySuccessfully() {
        // Arrange
        GitHubRepositoryCoordinates coordinates =
                new GitHubRepositoryCoordinates("octocat", "Hello-World");

        GitHubRepositoryResponse response = createResponse();

        GitRepository savedRepository = new GitRepository();
        savedRepository.setId(UUID.randomUUID());
        savedRepository.setName("Hello-World");

        when(gitHubApiClient.getRepository(coordinates))
                .thenReturn(response);

        when(gitRepositoryRepository.save(any(GitRepository.class)))
                .thenReturn(savedRepository);

        // Act
        GitRepository result =
                repositoryService.importRepository(coordinates);

        // Assert
        assertEquals(savedRepository, result);
    }

    @Test
    void shouldCallGitHubApiClient() {
        // Arrange
        GitHubRepositoryCoordinates coordinates =
                new GitHubRepositoryCoordinates("octocat", "Hello-World");

        when(gitHubApiClient.getRepository(coordinates))
                .thenReturn(createResponse());

        when(gitRepositoryRepository.save(any(GitRepository.class)))
                .thenReturn(new GitRepository());

        // Act
        repositoryService.importRepository(coordinates);

        // Assert
        verify(gitHubApiClient, times(1))
                .getRepository(coordinates);
    }

    @Test
    void shouldSaveRepository() {
        // Arrange
        GitHubRepositoryCoordinates coordinates =
                new GitHubRepositoryCoordinates("octocat", "Hello-World");

        when(gitHubApiClient.getRepository(coordinates))
                .thenReturn(createResponse());

        when(gitRepositoryRepository.save(any(GitRepository.class)))
                .thenReturn(new GitRepository());

        // Act
        repositoryService.importRepository(coordinates);

        // Assert
        verify(gitRepositoryRepository, times(1))
                .save(any(GitRepository.class));
    }

    private GitHubRepositoryResponse createResponse() {
        return new GitHubRepositoryResponse(
                12345L,
                "Hello-World",
                "octocat/Hello-World",
                "https://github.com/octocat/Hello-World",
                "Sample repository",
                "main",
                "public",
                100,
                25,
                "Java",
                new GitHubRepositoryResponse.Owner("octocat")
        );
    }
}