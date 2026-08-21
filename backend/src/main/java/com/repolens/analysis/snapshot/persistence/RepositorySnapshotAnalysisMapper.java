package com.repolens.analysis.snapshot.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import org.springframework.stereotype.Component;

@Component
public class RepositorySnapshotAnalysisMapper {

    private final ObjectMapper objectMapper;

    public RepositorySnapshotAnalysisMapper(
            ObjectMapper objectMapper
    ) {
        this.objectMapper = objectMapper;
    }

    public RepositorySnapshotAnalysisEntity toEntity(
            RepositorySnapshotAnalysis analysis
    ) {
        if (analysis == null) {
            throw new IllegalArgumentException(
                    "Analysis must not be null"
            );
        }

        RepositorySnapshotAnalysisEntity entity =
                new RepositorySnapshotAnalysisEntity();

        entity.setReadmePresent(analysis.readmePresent());
        entity.setDockerPresent(analysis.dockerPresent());
        entity.setGithubActionsPresent(
                analysis.githubActionsPresent()
        );
        entity.setLicensePresent(analysis.licensePresent());

        entity.setBuildTools(analysis.buildTools());
        entity.setLanguages(analysis.languages());

        entity.setTotalFiles(
                analysis.structure().totalFiles()
        );
        entity.setTotalDirectories(
                analysis.structure().totalDirectories()
        );
        entity.setSourceFiles(
                analysis.structure().sourceFiles()
        );
        entity.setDocumentationFiles(
                analysis.structure().documentationFiles()
        );
        entity.setConfigurationFiles(
                analysis.structure().configurationFiles()
        );

        entity.setPrimaryArchitecture(
                analysis.architecture().primaryArchitecture()
        );

        entity.setHealthScore(
                analysis.health().score()
        );
        entity.setHealthGrade(
                analysis.health().grade()
        );

        entity.setArchitectureData(
                writeJson(analysis.architecture())
        );

        entity.setHealthData(
                writeJson(analysis.health())
        );

        return entity;
    }

    private String writeJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Failed to serialize snapshot analysis",
                    exception
            );
        }
    }
}