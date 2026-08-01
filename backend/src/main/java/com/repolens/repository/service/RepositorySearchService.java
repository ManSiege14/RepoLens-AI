package com.repolens.repository.service;

import com.repolens.repository.mapper.RepositorySummaryResponseMapper;
import com.repolens.repository.persistence.GitRepositoryRepository;
import com.repolens.repository.web.dto.RepositorySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RepositorySearchService {

    private final GitRepositoryRepository repositoryRepository;
    private final RepositorySummaryResponseMapper mapper;

    public RepositorySearchService(
            GitRepositoryRepository repositoryRepository,
            RepositorySummaryResponseMapper mapper
    ) {
        this.repositoryRepository = repositoryRepository;
        this.mapper = mapper;
    }

    public Page<RepositorySummaryResponse> searchRepositories(
            String query,
            Pageable pageable
    ) {

        return repositoryRepository
                .findByNameContainingIgnoreCaseOrOwnerContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                        query,
                        query,
                        query,
                        pageable
                )
                .map(mapper::toResponse);
    }
}