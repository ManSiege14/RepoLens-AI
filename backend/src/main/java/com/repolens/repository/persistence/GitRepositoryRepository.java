package com.repolens.repository.persistence;

import com.repolens.repository.domain.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GitRepositoryRepository
        extends JpaRepository<GitRepository, UUID> {

    Optional<GitRepository> findByHtmlUrl(String htmlUrl);
    Optional<GitRepository> findByGithubRepositoryId(Long githubRepositoryId);
}