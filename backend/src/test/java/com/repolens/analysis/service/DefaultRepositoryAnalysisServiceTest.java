package com.repolens.analysis.service;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.model.RepositoryAnalysis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class DefaultRepositoryAnalysisServiceTest {

    private static final String REPOSITORY_URL =
            "https://github.com/ManSiege14/Vector_java";

    @Autowired
    private RepositoryAnalysisService repositoryAnalysisService;

    @Test
    void shouldAnalyzeRepository() {

        RepositoryAnalysis analysis =
                repositoryAnalysisService.analyze(REPOSITORY_URL);

        assertTrue(
                analysis.buildTools().contains(BuildTool.MAVEN)
        );

        assertTrue(
                analysis.frameworks().contains(Framework.SPRING_BOOT)
        );

        assertTrue(
                analysis.languages().contains(ProgrammingLanguage.JAVA)
        );
    }
}