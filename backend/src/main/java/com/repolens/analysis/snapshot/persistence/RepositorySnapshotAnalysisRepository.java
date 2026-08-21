package com.repolens.analysis.snapshot.persistence;

import com.repolens.repository.domain.GitRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RepositorySnapshotAnalysisRepository
        extends JpaRepository<RepositorySnapshotAnalysisEntity, UUID> {

    Optional<RepositorySnapshotAnalysisEntity> findByRepository(
            GitRepository repository
    );
}