package com.repolens.repository.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.github.validation.GitHubRepositoryUrlParser;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.service.RepositoryDetailsService;
import com.repolens.repository.service.RepositoryListService;
import com.repolens.repository.service.RepositoryService;
import com.repolens.repository.web.dto.ImportRepositoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.repolens.shared.web.mapper.PagedResponseMapper;
@WebMvcTest(RepositoryController.class)
@Import(RepositoryControllerTest.TestConfig.class)
class RepositoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RepositoryService repositoryService;

    @MockBean
    private GitHubRepositoryUrlParser gitHubRepositoryUrlParser;
    @MockBean
    private PagedResponseMapper pagedResponseMapper;
    @TestConfiguration
    static class TestConfig {

        @Bean
        RepositoryDetailsService repositoryDetailsService() {
            return mock(RepositoryDetailsService.class);
        }

        @Bean
        RepositoryListService repositoryListService() {
            return mock(RepositoryListService.class);
        }
    }

    @Test
    void shouldImportRepositorySuccessfully() throws Exception {

        ImportRepositoryRequest request = createRequest();
        GitHubRepositoryCoordinates coordinates = createCoordinates();
        GitRepository repository = createRepository();

        when(gitHubRepositoryUrlParser.parse(request.url()))
                .thenReturn(coordinates);

        when(repositoryService.importRepository(coordinates))
                .thenReturn(repository);

        mockMvc.perform(
                        post("/api/repositories/import")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.owner").value("octocat"))
                .andExpect(jsonPath("$.name").value("Hello-World"))
                .andExpect(
                        jsonPath("$.githubRepositoryId")
                                .value(12345)
                );

        verify(gitHubRepositoryUrlParser)
                .parse(request.url());

        verify(repositoryService)
                .importRepository(coordinates);
    }

    @Test
    void shouldReturnBadRequestForBlankUrl() throws Exception {

        ImportRepositoryRequest request =
                new ImportRepositoryRequest("");

        mockMvc.perform(
                        post("/api/repositories/import")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(repositoryService);
    }

    private ImportRepositoryRequest createRequest() {
        return new ImportRepositoryRequest(
                "https://github.com/octocat/Hello-World"
        );
    }

    private GitHubRepositoryCoordinates createCoordinates() {
        return new GitHubRepositoryCoordinates(
                "octocat",
                "Hello-World"
        );
    }

    private GitRepository createRepository() {

        GitRepository repository = new GitRepository();

        repository.setId(UUID.randomUUID());
        repository.setGithubRepositoryId(12345L);
        repository.setOwner("octocat");
        repository.setName("Hello-World");
        repository.setFullName("octocat/Hello-World");
        repository.setHtmlUrl(
                "https://github.com/octocat/Hello-World"
        );
        repository.setDescription("Sample repository");
        repository.setPrimaryLanguage("Java");
        repository.setStars(100);
        repository.setForks(25);

        return repository;
    }
}