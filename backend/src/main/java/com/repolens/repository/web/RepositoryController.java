package com.repolens.repository.web;

import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.github.validation.GitHubRepositoryUrlParser;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.service.RepositoryService;
import com.repolens.repository.web.dto.ImportRepositoryRequest;
import com.repolens.repository.web.dto.RepositoryResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;
    private final GitHubRepositoryUrlParser gitHubRepositoryUrlParser;

    public RepositoryController(
            RepositoryService repositoryService,
            GitHubRepositoryUrlParser gitHubRepositoryUrlParser
    ) {
        this.repositoryService = repositoryService;
        this.gitHubRepositoryUrlParser = gitHubRepositoryUrlParser;
    }

    @PostMapping("/import")
    public RepositoryResponse importRepository(
            @Valid @RequestBody ImportRepositoryRequest request
    ) {

        GitHubRepositoryCoordinates coordinates =
                gitHubRepositoryUrlParser.parse(request.url());

        GitRepository repository =
                repositoryService.importRepository(coordinates);

        return new RepositoryResponse(
                repository.getId(),
                repository.getGithubRepositoryId(),
                repository.getOwner(),
                repository.getName(),
                repository.getFullName(),
                repository.getDescription(),
                repository.getHtmlUrl(),
                repository.getPrimaryLanguage(),
                repository.getStars(),
                repository.getForks()
        );
    }
}