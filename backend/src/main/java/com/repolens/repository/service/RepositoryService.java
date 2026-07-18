package com.repolens.repository.service;

import com.repolens.github.client.GitHubApiClient;
import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.github.client.dto.GitHubRepositoryResponse;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.mapper.RepositoryMapper;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RepositoryService {

    private final GitHubApiClient gitHubApiClient;
    private final GitRepositoryRepository gitRepositoryRepository;

    public RepositoryService(
            GitHubApiClient gitHubApiClient,
            GitRepositoryRepository gitRepositoryRepository
    ) {
        this.gitHubApiClient = gitHubApiClient;
        this.gitRepositoryRepository = gitRepositoryRepository;
    }

    public GitRepository importRepository(
            GitHubRepositoryCoordinates coordinates
    ) {

        GitHubRepositoryResponse response =
                gitHubApiClient.getRepository(coordinates);

        GitRepository gitRepository =
                RepositoryMapper.toEntity(response);

        return gitRepositoryRepository.save(gitRepository);
    }
}