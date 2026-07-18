package com.repolens.repository.web.dto;

import jakarta.validation.constraints.NotBlank;

public record ImportRepositoryRequest(

        @NotBlank
        String url

) {
}