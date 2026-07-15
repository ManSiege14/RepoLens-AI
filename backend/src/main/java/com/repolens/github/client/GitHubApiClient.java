package com.repolens.github.client;

import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class GitHubApiClient {

    private final RestClient restClient;

    public GitHubApiClient(
            RestClient.Builder restClientBuilder,
            GitHubClientProperties properties
    ) {
        RestClient.Builder builder = restClientBuilder
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.ACCEPT,"application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28");

        if (properties.token() != null && !properties.token().isBlank()) {
            builder.defaultHeader(
                    "Authorization",
                    "Bearer " + properties.token()
            );
        }

        this.restClient = builder.build();
    }

    public GitHubRepositoryResponse getRepository(
            GitHubRepositoryCoordinates coordinates
    ) {
        try {
            GitHubRepositoryResponse response = restClient.get()
                    .uri(
                            "/repos/{owner}/{repository}",
                            coordinates.owner(),
                            coordinates.repository()
                    )
                    .retrieve()
                    .body(GitHubRepositoryResponse.class);

            if (response == null) {
                throw new GitHubApiException(
                        "GitHub returned an empty repository response"
                );
            }

            return response;

        } catch (RestClientException exception) {
            throw new GitHubApiException(
                    "Failed to fetch repository from GitHub",
                    exception
            );
        }
    }
}