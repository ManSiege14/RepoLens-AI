package com.repolens.repository.service;

import com.repolens.repository.persistence.GitRepositoryRepository;
import com.repolens.repository.web.dto.RepositorySummaryResponse;
import com.repolens.repository.mapper.RepositorySummaryResponseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<RepositorySummaryResponse> getRepositories() {

        return repositoryRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}