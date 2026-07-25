package com.repolens.analysis.persistence;

import com.repolens.repository.domain.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RepositoryAnalysisRepository
        extends JpaRepository<RepositoryAnalysisEntity, UUID> {

    Optional<RepositoryAnalysisEntity> findByRepository(
            GitRepository repository
    );
}