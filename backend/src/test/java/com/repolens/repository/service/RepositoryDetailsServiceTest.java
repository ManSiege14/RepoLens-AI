package com.repolens.repository.service;

import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.persistence.RepositoryAnalysisRepository;
import com.repolens.analysis.snapshot.persistence.RepositorySnapshotAnalysisEntity;
import com.repolens.analysis.snapshot.persistence.RepositorySnapshotAnalysisRepository;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.mapper.RepositoryDetailsResponseMapper;
import com.repolens.repository.persistence.GitRepositoryRepository;
import com.repolens.repository.web.dto.RepositoryDetailsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepositoryDetailsServiceTest {

    @Mock
    private GitRepositoryRepository repositoryRepository;

    @Mock
    private RepositoryAnalysisRepository analysisRepository;

    @Mock
    private RepositorySnapshotAnalysisRepository snapshotAnalysisRepository;

    @Mock
    private RepositoryDetailsResponseMapper mapper;

    @InjectMocks
    private RepositoryDetailsService repositoryDetailsService;

    @Test
    void shouldReturnRepositoryDetailsWithBothAnalyses() {

        UUID repositoryId = UUID.randomUUID();

        GitRepository repository = new GitRepository();
        repository.setId(repositoryId);

        RepositoryAnalysisEntity analysis =
                new RepositoryAnalysisEntity();

        RepositorySnapshotAnalysisEntity snapshotAnalysis =
                new RepositorySnapshotAnalysisEntity();

        RepositoryDetailsResponse response =
                new RepositoryDetailsResponse(
                        repositoryId,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        when(repositoryRepository.findById(repositoryId))
                .thenReturn(Optional.of(repository));

        when(analysisRepository.findByRepository(repository))
                .thenReturn(Optional.of(analysis));

        when(snapshotAnalysisRepository.findByRepository(repository))
                .thenReturn(Optional.of(snapshotAnalysis));

        when(mapper.toResponse(
                repository,
                analysis,
                snapshotAnalysis
        )).thenReturn(response);

        RepositoryDetailsResponse result =
                repositoryDetailsService.getRepository(repositoryId);

        assertSame(response, result);

        verify(repositoryRepository)
                .findById(repositoryId);

        verify(analysisRepository)
                .findByRepository(repository);

        verify(snapshotAnalysisRepository)
                .findByRepository(repository);

        verify(mapper)
                .toResponse(
                        repository,
                        analysis,
                        snapshotAnalysis
                );
                
    }
    @Test
void shouldReturnRepositoryDetailsWhenSnapshotAnalysisDoesNotExist() {

    UUID repositoryId = UUID.randomUUID();

    GitRepository repository = new GitRepository();
    repository.setId(repositoryId);

    RepositoryAnalysisEntity analysis =
            new RepositoryAnalysisEntity();

    RepositoryDetailsResponse response =
            new RepositoryDetailsResponse(
                    repositoryId,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

    when(repositoryRepository.findById(repositoryId))
            .thenReturn(Optional.of(repository));

    when(analysisRepository.findByRepository(repository))
            .thenReturn(Optional.of(analysis));

    when(snapshotAnalysisRepository.findByRepository(repository))
            .thenReturn(Optional.empty());

    when(mapper.toResponse(
            repository,
            analysis,
            null
    )).thenReturn(response);

    RepositoryDetailsResponse result =
            repositoryDetailsService.getRepository(repositoryId);

    assertSame(response, result);

    verify(snapshotAnalysisRepository)
            .findByRepository(repository);

    verify(mapper)
            .toResponse(
                    repository,
                    analysis,
                    null
            );
}
}