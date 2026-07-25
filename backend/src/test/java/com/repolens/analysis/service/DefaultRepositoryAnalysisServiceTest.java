package com.repolens.analysis.service;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
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

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    @Test
    void shouldAnalyzeRepository() {

        GitRepository repository = gitRepositoryRepository
                .findByHtmlUrl(REPOSITORY_URL)
                .orElseGet(() -> {
                    GitRepository repo = new GitRepository();
                    repo.setGithubRepositoryId(1L);
                    repo.setOwner("ManSiege14");
                    repo.setName("Vector_java");
                    repo.setFullName("ManSiege14/Vector_java");
                    repo.setHtmlUrl(REPOSITORY_URL);
                    repo.setDefaultBranch("main");
                    repo.setVisibility("public");
                    repo.setStars(0);
                    repo.setForks(0);
                    return gitRepositoryRepository.save(repo);
                });
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