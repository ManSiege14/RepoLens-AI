package com.repolens.repository.web.dto;

import java.time.Instant;
import java.util.Set;

public record AnalysisDetailsResponse(

        Instant analyzedAt,

        Set<String> buildTools,

        Set<String> frameworks,

        Set<String> languages,

        Set<String> infrastructure

) {
}