package com.repolens.github.client;

import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.shared.exception.RepositoryNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Component
public class GitHubApiClient {

    private final RestClient restClient;

    public GitHubApiClient(
            RestClient.Builder restClientBuilder,
            GitHubClientProperties properties
    ) {

        RestClient.Builder builder = restClientBuilder
                .baseUrl(properties.baseUrl())
                .defaultHeader(
                        HttpHeaders.ACCEPT,
                        "application/vnd.github+json"
                )
                .defaultHeader(
                        "X-GitHub-Api-Version",
                        "2022-11-28"
                );

        if (properties.token() != null && !properties.token().isBlank()) {
            builder.defaultHeader(
                    HttpHeaders.AUTHORIZATION,
                    "Bearer " + properties.token()
            );
        }

        this.restClient = builder.build();
    }

    public GitHubRepositoryResponse getRepository(
            GitHubRepositoryCoordinates coordinates
    ) {

        try {

            System.out.println("Calling:");
            System.out.println(
                    "https://api.github.com/repos/"
                            + coordinates.owner()
                            + "/"
                            + coordinates.repository()
            );

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
                        "GitHub returned an empty repository response."
                );
            }

            System.out.println("GitHub returned:");
            System.out.println("fullName = " + response.fullName());
            System.out.println("owner    = " + response.owner().login());
            System.out.println("name     = " + response.name());

            return response;

        } catch (RestClientResponseException exception) {

            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RepositoryNotFoundException(
                        "GitHub repository '%s/%s' was not found."
                                .formatted(
                                        coordinates.owner(),
                                        coordinates.repository()
                                )
                );
            }

            throw new GitHubApiException(
                    "GitHub API returned an error.",
                    exception
            );

        } catch (RestClientException exception) {

            throw new GitHubApiException(
                    "Failed to communicate with GitHub.",
                    exception
            );
        }
    }
}