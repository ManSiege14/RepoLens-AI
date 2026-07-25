package com.repolens.repository.service;

import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.persistence.RepositoryAnalysisRepository;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import com.repolens.repository.web.dto.RepositoryDetailsResponse;
import com.repolens.repository.mapper.RepositoryDetailsResponseMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RepositoryDetailsService {

    private final GitRepositoryRepository repositoryRepository;
    private final RepositoryAnalysisRepository analysisRepository;
    private final RepositoryDetailsResponseMapper mapper;

    public RepositoryDetailsService(
            GitRepositoryRepository repositoryRepository,
            RepositoryAnalysisRepository analysisRepository,
            RepositoryDetailsResponseMapper mapper
    ) {
        this.repositoryRepository = repositoryRepository;
        this.analysisRepository = analysisRepository;
        this.mapper = mapper;
    }

    public RepositoryDetailsResponse getRepository(UUID repositoryId) {

        GitRepository repository = repositoryRepository
                .findById(repositoryId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Repository not found: " + repositoryId
                        )
                );

        RepositoryAnalysisEntity analysis = analysisRepository
                .findByRepository(repository)
                .orElse(null);

        return mapper.toResponse(repository, analysis);
    }
}