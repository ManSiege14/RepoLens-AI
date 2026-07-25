package com.repolens.analysis;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.persistence.RepositoryAnalysisRepository;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryAnalysisRepositoryTest {

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    @Autowired
    private RepositoryAnalysisRepository repositoryAnalysisRepository;

    @Test
    void shouldSaveAndLoadRepositoryAnalysis() {

        GitRepository repository = new GitRepository();

        repository.setGithubRepositoryId(12345L);
        repository.setOwner("spring-projects");
        repository.setName("spring-petclinic");
        repository.setFullName("spring-projects/spring-petclinic");
        repository.setHtmlUrl("https://github.com/spring-projects/spring-petclinic");
        repository.setDescription("Sample Spring application");
        repository.setDefaultBranch("main");
        repository.setVisibility("public");
        repository.setStars(100);
        repository.setForks(20);
        repository.setPrimaryLanguage("Java");

        repository = gitRepositoryRepository.save(repository);

        RepositoryAnalysisEntity analysis = new RepositoryAnalysisEntity();

        analysis.setRepository(repository);
        analysis.setAnalyzedAt(Instant.now());

        analysis.setBuildTools(Set.of(BuildTool.MAVEN));
        analysis.setFrameworks(Set.of(Framework.SPRING_BOOT));
        analysis.setInfrastructure(Set.of(Infrastructure.DOCKER));
        analysis.setLanguages(Set.of(
                ProgrammingLanguage.JAVA,
                ProgrammingLanguage.JAVASCRIPT
        ));

        repositoryAnalysisRepository.save(analysis);

        RepositoryAnalysisEntity loaded =
                repositoryAnalysisRepository
                        .findByRepository(repository)
                        .orElseThrow();

        assertEquals(repository.getId(), loaded.getRepository().getId());

        assertEquals(
                Set.of(BuildTool.MAVEN),
                loaded.getBuildTools()
        );

        assertEquals(
                Set.of(Framework.SPRING_BOOT),
                loaded.getFrameworks()
        );

        assertEquals(
                Set.of(Infrastructure.DOCKER),
                loaded.getInfrastructure()
        );

        assertEquals(
                Set.of(
                        ProgrammingLanguage.JAVA,
                        ProgrammingLanguage.JAVASCRIPT
                ),
                loaded.getLanguages()
        );

        assertNotNull(loaded.getAnalyzedAt());

        assertTrue(
                loaded.getAnalyzedAt()
                        .isBefore(Instant.now().plusSeconds(1))
        );
    }
}