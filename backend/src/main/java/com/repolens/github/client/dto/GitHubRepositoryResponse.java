package com.repolens.github.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubRepositoryResponse(

        long id,

        String name,

        @JsonProperty("full_name")
        String fullName,

        @JsonProperty("html_url")
        String htmlUrl,

        String description,

        @JsonProperty("default_branch")
        String defaultBranch,

        String visibility,

        @JsonProperty("stargazers_count")
        int stars,

        @JsonProperty("forks_count")
        int forks,

        String language,

        Owner owner

) {

    public record Owner(
            String login
    ) {
    }
}