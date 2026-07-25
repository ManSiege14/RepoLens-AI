package com.repolens.repository.web.mapper;

import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.web.dto.AnalysisDetailsResponse;
import com.repolens.repository.web.dto.RepositoryDetailsResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RepositoryDetailsResponseMapper {

    public RepositoryDetailsResponse toResponse(
            GitRepository repository,
            RepositoryAnalysisEntity analysis
    ) {

        AnalysisDetailsResponse analysisResponse = null;

        if (analysis != null) {

            analysisResponse = new AnalysisDetailsResponse(

                    analysis.getAnalyzedAt(),

                    toStringSet(analysis.getBuildTools()),

                    toStringSet(analysis.getFrameworks()),

                    toStringSet(analysis.getLanguages()),

                    toStringSet(analysis.getInfrastructure())
            );
        }

        return new RepositoryDetailsResponse(

                repository.getId(),

                repository.getGithubRepositoryId(),

                repository.getOwner(),

                repository.getName(),

                repository.getFullName(),

                repository.getDescription(),

                repository.getHtmlUrl(),

                repository.getDefaultBranch(),

                repository.getVisibility(),

                repository.getStars(),

                repository.getForks(),

                repository.getPrimaryLanguage(),

                analysisResponse
        );
    }

    private Set<String> toStringSet(Set<? extends Enum<?>> values) {
        if (values == null || values.isEmpty()) {
            return Set.of();
        }
        return values.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}