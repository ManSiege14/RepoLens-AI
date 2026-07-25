package com.repolens.repository.web.dto;

import java.util.UUID;

public record RepositoryDetailsResponse(

        UUID id,

        Long githubRepositoryId,

        String owner,

        String name,

        String fullName,

        String description,

        String htmlUrl,

        String defaultBranch,

        String visibility,

        Integer stars,

        Integer forks,

        String primaryLanguage,

        AnalysisDetailsResponse analysis

) {
}