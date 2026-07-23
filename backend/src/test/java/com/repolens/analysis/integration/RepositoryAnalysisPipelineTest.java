package com.repolens.analysis.integration;

import com.repolens.analysis.clone.RepositoryCloner;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.BuildToolDetector;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.FrameworkDetector;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.InfrastructureDetector;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.detector.LanguageDetector;
import com.repolens.analysis.scanner.FileScanner;
import com.repolens.analysis.scanner.ScannedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RepositoryAnalysisPipelineTest {

    @Autowired
    private RepositoryCloner repositoryCloner;

    @Autowired
    private FileScanner fileScanner;

    @Autowired
    private BuildToolDetector buildToolDetector;

    @Autowired
    private FrameworkDetector frameworkDetector;

    @Autowired
    private InfrastructureDetector infrastructureDetector;

    @Autowired
    private LanguageDetector languageDetector;

    @Test
    void shouldAnalyzeRealRepository() {

        String repositoryUrl = "https://github.com/ManSiege14/Vector_java";

        Path repositoryPath = repositoryCloner.cloneRepository(repositoryUrl);

        try {
            ScannedRepository repository = fileScanner.scan(repositoryPath);

            Set<BuildTool> buildTools = buildToolDetector.detect(repository);
            Set<Framework> frameworks = frameworkDetector.detect(repository);
            Set<Infrastructure> infrastructure = infrastructureDetector.detect(repository);
            Set<ProgrammingLanguage> languages = languageDetector.detect(repository);

            System.out.println("========== Vector_java Analysis ==========");
            System.out.println("Build Tools    : " + buildTools);
            System.out.println("Frameworks     : " + frameworks);
            System.out.println("Infrastructure : " + infrastructure);
            System.out.println("Languages      : " + languages);
            System.out.println("==========================================");

            assertFalse(buildTools.isEmpty());
            assertFalse(languages.isEmpty());

            assertTrue(buildTools.contains(BuildTool.MAVEN));
            assertTrue(frameworks.contains(Framework.SPRING_BOOT));
            assertTrue(languages.contains(ProgrammingLanguage.JAVA));

            // Uncomment these only if they exist in your repo.
            // assertTrue(infrastructure.contains(Infrastructure.GITHUB_ACTIONS));
            // assertTrue(infrastructure.contains(Infrastructure.DOCKER));

        } finally {
            // If your cloner creates a temp directory, clean it up here.
            // Replace with your actual cleanup method if you have one.
            // Example:
            // FileUtils.deleteDirectory(repositoryPath.toFile());
        }
    }
}