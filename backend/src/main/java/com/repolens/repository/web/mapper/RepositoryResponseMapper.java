package com.repolens.repository.web.mapper;

import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.web.dto.RepositoryResponse;

public final class RepositoryResponseMapper {

    private RepositoryResponseMapper() {
    }

    public static RepositoryResponse toResponse(GitRepository repository) {

        return new RepositoryResponse(
                repository.getId(),
                repository.getGithubRepositoryId(),
                repository.getOwner(),
                repository.getName(),
                repository.getFullName(),
                repository.getDescription(),
                repository.getHtmlUrl(),
                repository.getPrimaryLanguage(),
                repository.getStars(),
                repository.getForks()
        );
    }
}