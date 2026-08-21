package com.repolens.repository.mapper;

import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.snapshot.persistence.RepositorySnapshotAnalysisEntity;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.web.dto.AnalysisDetailsResponse;
import com.repolens.repository.web.dto.RepositoryDetailsResponse;
import com.repolens.repository.web.dto.RepositorySnapshotAnalysisResponse;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RepositoryDetailsResponseMapper {

    public RepositoryDetailsResponse toResponse(
            GitRepository repository,
            RepositoryAnalysisEntity analysis,
            RepositorySnapshotAnalysisEntity snapshotAnalysis
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

        RepositorySnapshotAnalysisResponse snapshotAnalysisResponse = null;

        if (snapshotAnalysis != null) {

            snapshotAnalysisResponse =
                    new RepositorySnapshotAnalysisResponse(

                            snapshotAnalysis.getAnalyzedAt(),

                            snapshotAnalysis.isReadmePresent(),
                            snapshotAnalysis.isDockerPresent(),
                            snapshotAnalysis.isGithubActionsPresent(),
                            snapshotAnalysis.isLicensePresent(),

                            toStringSet(snapshotAnalysis.getBuildTools()),
                            toStringSet(snapshotAnalysis.getLanguages()),

                            snapshotAnalysis.getTotalFiles(),
                            snapshotAnalysis.getTotalDirectories(),
                            snapshotAnalysis.getSourceFiles(),
                            snapshotAnalysis.getDocumentationFiles(),
                            snapshotAnalysis.getConfigurationFiles(),

                            snapshotAnalysis.getPrimaryArchitecture() != null
                                    ? snapshotAnalysis.getPrimaryArchitecture().name()
                                    : null,

                            snapshotAnalysis.getHealthScore(),

                            snapshotAnalysis.getHealthGrade() != null
                                    ? snapshotAnalysis.getHealthGrade().name()
                                    : null,

                            snapshotAnalysis.getArchitectureData(),
                            snapshotAnalysis.getHealthData()
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

                analysisResponse,

                snapshotAnalysisResponse
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