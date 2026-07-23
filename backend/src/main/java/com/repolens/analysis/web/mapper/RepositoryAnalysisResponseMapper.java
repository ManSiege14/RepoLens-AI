package com.repolens.analysis.web.mapper;

import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.analysis.web.dto.RepositoryAnalysisResponse;
import org.springframework.stereotype.Component;

@Component
public class RepositoryAnalysisResponseMapper {

    public RepositoryAnalysisResponse toResponse(
            RepositoryAnalysis analysis
    ) {
        return new RepositoryAnalysisResponse(
                analysis.buildTools(),
                analysis.frameworks(),
                analysis.infrastructure(),
                analysis.languages()
        );
    }
}