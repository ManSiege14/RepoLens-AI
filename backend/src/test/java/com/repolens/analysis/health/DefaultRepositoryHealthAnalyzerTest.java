package com.repolens.analysis.health;

import com.repolens.analysis.architecture.ArchitectureAnalysis;
import com.repolens.analysis.architecture.ArchitectureType;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import com.repolens.analysis.snapshot.RepositoryStructure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultRepositoryHealthAnalyzerTest {

    @Test
    void shouldCalculatePerfectScore() {

        RepositorySnapshotAnalysis analysis =
                new RepositorySnapshotAnalysis(
                        true,
                        true,
                        true,
                        true,
                        Set.of(BuildTool.MAVEN),
                        Set.of(ProgrammingLanguage.JAVA),
                        new RepositoryStructure(
                                20,
                                5,
                                10,
                                2,
                                4
                        ),
                        new ArchitectureAnalysis(
                                ArchitectureType.LAYERED,
                                List.of(
                                        ArchitectureType.LAYERED,
                                        ArchitectureType.REST_API
                                ),
                                List.of()
                        ),
                        null
                );

        RepositoryHealthAnalysis result =
                new DefaultRepositoryHealthAnalyzer()
                        .analyze(analysis);

        assertEquals(100, result.score());
        assertEquals(
                HealthGrade.EXCELLENT,
                result.grade()
        );

        assertEquals(
                8,
                result.findings().size()
        );
    }

    @Test
    void shouldCalculateZeroScore() {

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
                        null
                );

        RepositoryHealthAnalysis result =
                new DefaultRepositoryHealthAnalyzer()
                        .analyze(analysis);

        assertEquals(0, result.score());
        assertEquals(
                HealthGrade.POOR,
                result.grade()
        );
    }
}