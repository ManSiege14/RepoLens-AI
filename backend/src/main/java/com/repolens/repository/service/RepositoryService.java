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

        GitRepository mapped =
                RepositoryMapper.toEntity(response);

        GitRepository repository =
                gitRepositoryRepository
                        .findByGithubRepositoryId(
                                mapped.getGithubRepositoryId()
                        )
                        .orElse(new GitRepository());

        repository.setGithubRepositoryId(mapped.getGithubRepositoryId());
        repository.setOwner(mapped.getOwner());
        repository.setName(mapped.getName());
        repository.setFullName(mapped.getFullName());
        repository.setDescription(mapped.getDescription());
        repository.setHtmlUrl(mapped.getHtmlUrl());
        repository.setDefaultBranch(mapped.getDefaultBranch());
        repository.setVisibility(mapped.getVisibility());
        repository.setPrimaryLanguage(mapped.getPrimaryLanguage());
        repository.setStars(mapped.getStars());
        repository.setForks(mapped.getForks());

        return gitRepositoryRepository.save(repository);
    }
}