package com.repolens.repository;

import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositoryRepositoryTest {

    @Autowired
    private GitRepositoryRepository gitRepositoryRepository;

    @Test
    void shouldSaveAndLoadRepository() {

        GitRepository repository = new GitRepository();

        repository.setGithubRepositoryId(12345L);
        repository.setOwner("spring-projects");
        repository.setName("spring-petclinic");
        repository.setFullName("spring-projects/spring-petclinic");
        repository.setHtmlUrl(
                "https://github.com/spring-projects/spring-petclinic"
        );
        repository.setDescription("Sample Spring application");
        repository.setDefaultBranch("main");
        repository.setVisibility("public");
        repository.setStars(100);
        repository.setForks(20);
        repository.setPrimaryLanguage("Java");

        GitRepository saved =
                gitRepositoryRepository.save(repository);

        assertNotNull(saved.getId());

        GitRepository loaded =
                gitRepositoryRepository.findById(saved.getId())
                        .orElseThrow();

        assertEquals(repository.getGithubRepositoryId(), loaded.getGithubRepositoryId());
        assertEquals(repository.getOwner(), loaded.getOwner());
        assertEquals(repository.getName(), loaded.getName());
        assertEquals(repository.getFullName(), loaded.getFullName());
        assertEquals(repository.getHtmlUrl(), loaded.getHtmlUrl());
        assertEquals(repository.getDescription(), loaded.getDescription());
        assertEquals(repository.getDefaultBranch(), loaded.getDefaultBranch());
        assertEquals(repository.getVisibility(), loaded.getVisibility());
        assertEquals(repository.getStars(), loaded.getStars());
        assertEquals(repository.getForks(), loaded.getForks());
        assertEquals(repository.getPrimaryLanguage(), loaded.getPrimaryLanguage());

        assertNotNull(loaded.getCreatedAt());
        assertNotNull(loaded.getUpdatedAt());
    }
}