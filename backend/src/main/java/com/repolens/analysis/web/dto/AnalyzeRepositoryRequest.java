package com.repolens.analysis.web.dto;

import jakarta.validation.constraints.NotBlank;

public record AnalyzeRepositoryRequest(

        @NotBlank
        String repositoryUrl

) {
}