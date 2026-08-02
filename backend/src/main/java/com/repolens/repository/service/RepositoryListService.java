package com.repolens.repository.service;

import com.repolens.repository.mapper.RepositorySummaryResponseMapper;
import com.repolens.repository.persistence.GitRepositoryRepository;
import com.repolens.repository.web.dto.RepositorySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.repolens.repository.specification.RepositorySpecification;

@Service
public class RepositoryListService {

    private final GitRepositoryRepository repositoryRepository;
    private final RepositorySummaryResponseMapper mapper;

    public RepositoryListService(
            GitRepositoryRepository repositoryRepository,
            RepositorySummaryResponseMapper mapper
    ) {
        this.repositoryRepository = repositoryRepository;
        this.mapper = mapper;
    }

    public Page<RepositorySummaryResponse> getRepositories(
            RepositoryFilter filter,
            Pageable pageable
    ) {

        return repositoryRepository.findAll(
                        RepositorySpecification.withFilter(filter),
                        pageable
                )
                .map(mapper::toResponse);
    }
}