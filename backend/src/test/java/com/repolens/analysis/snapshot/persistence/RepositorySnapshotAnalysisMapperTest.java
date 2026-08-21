package com.repolens.analysis.snapshot.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repolens.analysis.architecture.ArchitectureAnalysis;
import com.repolens.analysis.architecture.ArchitectureEvidence;
import com.repolens.analysis.architecture.ArchitectureType;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.health.HealthFinding;
import com.repolens.analysis.health.HealthGrade;
import com.repolens.analysis.health.RepositoryHealthAnalysis;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import com.repolens.analysis.snapshot.RepositoryStructure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositorySnapshotAnalysisMapperTest {

    private final RepositorySnapshotAnalysisMapper mapper =
            new RepositorySnapshotAnalysisMapper(
                    new ObjectMapper()
            );

    @Test
    void shouldMapSnapshotAnalysisToEntity() {

        RepositoryStructure structure =
                new RepositoryStructure(
                        20,
                        5,
                        10,
                        3,
                        2
                );

        ArchitectureAnalysis architecture =
                new ArchitectureAnalysis(
                        ArchitectureType.LAYERED,
                        List.of(
                                ArchitectureType.LAYERED,
                                ArchitectureType.REST_API
                        ),
                        List.of(
                                new ArchitectureEvidence(
                                        "controller",
                                        "REST controller detected"
                                )
                        )
                );

        RepositoryHealthAnalysis health =
                new RepositoryHealthAnalysis(
                        85,
                        HealthGrade.GOOD,
                        List.of(
                                new HealthFinding(
                                        "documentation",
                                        10,
                                        "README is present"
                                )
                        )
                );

        RepositorySnapshotAnalysis analysis =
                new RepositorySnapshotAnalysis(
                        true,
                        true,
                        true,
                        true,
                        Set.of(BuildTool.MAVEN),
                        Set.of(ProgrammingLanguage.JAVA),
                        structure,
                        architecture,
                        health
                );

        RepositorySnapshotAnalysisEntity entity =
                mapper.toEntity(analysis);

        assertNotNull(entity);

        assertTrue(entity.isReadmePresent());
        assertTrue(entity.isDockerPresent());
        assertTrue(entity.isGithubActionsPresent());
        assertTrue(entity.isLicensePresent());

        assertEquals(
                Set.of(BuildTool.MAVEN),
                entity.getBuildTools()
        );

        assertEquals(
                Set.of(ProgrammingLanguage.JAVA),
                entity.getLanguages()
        );

        assertEquals(20, entity.getTotalFiles());
        assertEquals(5, entity.getTotalDirectories());
        assertEquals(10, entity.getSourceFiles());
        assertEquals(3, entity.getDocumentationFiles());
        assertEquals(2, entity.getConfigurationFiles());

        assertEquals(
                ArchitectureType.LAYERED,
                entity.getPrimaryArchitecture()
        );

        assertEquals(85, entity.getHealthScore());
        assertEquals(HealthGrade.GOOD, entity.getHealthGrade());

        assertNotNull(entity.getArchitectureData());
        assertNotNull(entity.getHealthData());

        assertTrue(
                entity.getArchitectureData()
                        .contains("LAYERED")
        );

        assertTrue(
                entity.getHealthData()
                        .contains("documentation")
        );
    }

    @Test
    void shouldHandleEmptyCollections() {

        RepositorySnapshotAnalysis analysis =
                new RepositorySnapshotAnalysis(
                        false,
                        false,
                        false,
                        false,
                        Set.of(),
                        Set.of(),
                        new RepositoryStructure(
                                0,
                                0,
                                0,
                                0,
                                0
                        ),
                        new ArchitectureAnalysis(
                                ArchitectureType.UNKNOWN,
                                List.of(),
                                List.of()
                        ),
                        new RepositoryHealthAnalysis(
                                0,
                                HealthGrade.POOR,
                                List.of()
                        )
                );

        RepositorySnapshotAnalysisEntity entity =
                mapper.toEntity(analysis);

        assertNotNull(entity);

        assertFalse(entity.isReadmePresent());
        assertFalse(entity.isDockerPresent());
        assertFalse(entity.isGithubActionsPresent());
        assertFalse(entity.isLicensePresent());

        assertTrue(entity.getBuildTools().isEmpty());
        assertTrue(entity.getLanguages().isEmpty());

        assertEquals(
                ArchitectureType.UNKNOWN,
                entity.getPrimaryArchitecture()
        );

        assertEquals(0, entity.getHealthScore());
        assertEquals(HealthGrade.POOR, entity.getHealthGrade());

        assertNotNull(entity.getArchitectureData());
        assertNotNull(entity.getHealthData());
    }
}