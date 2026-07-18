package com.repolens.repository.mapper;

import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.repository.domain.GitRepository;

import java.util.Objects;

public final class RepositoryMapper {

    private RepositoryMapper() {
    }

    public static GitRepository toEntity(GitHubRepositoryResponse response) {

        Objects.requireNonNull(response, "GitHubRepositoryResponse must not be null");

        GitRepository repository = new GitRepository();

        repository.setGithubRepositoryId(response.id());
        repository.setOwner(response.owner().login());
        repository.setName(response.name());
        repository.setFullName(response.fullName());
        repository.setHtmlUrl(response.htmlUrl());
        repository.setDescription(response.description());
        repository.setDefaultBranch(response.defaultBranch());
        repository.setVisibility(response.visibility());
        repository.setStars(response.stars());
        repository.setForks(response.forks());
        repository.setPrimaryLanguage(response.language());

        return repository;
    }
}