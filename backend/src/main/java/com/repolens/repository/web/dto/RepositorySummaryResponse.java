package com.repolens.repository.web.dto;

import java.util.UUID;

public record RepositorySummaryResponse(

        UUID id,

        String owner,

        String name,

        String primaryLanguage,

        Integer stars,

        Integer forks

) {
}