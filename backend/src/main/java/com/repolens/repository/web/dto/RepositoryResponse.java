package com.repolens.repository.web.dto;

import java.util.UUID;

public record RepositoryResponse(

        UUID id,

        Long githubRepositoryId,

        String owner,

        String name,

        String fullName,

        String description,

        String htmlUrl,

        String primaryLanguage,

        Integer stars,

        Integer forks

) {
}