package com.repolens.repository.web;

import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.github.validation.GitHubRepositoryUrlParser;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.service.RepositoryService;
import com.repolens.repository.web.dto.ImportRepositoryRequest;
import com.repolens.repository.web.dto.RepositoryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.repolens.repository.web.mapper.RepositoryResponseMapper;

@RestController
@RequestMapping("/api/repositories")
@Tag(
        name = "Repositories",
        description = "Import and manage GitHub repositories"
)
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

    @Operation(
            summary = "Import GitHub repository",
            description = "Imports a GitHub repository into RepoLens"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Repository imported successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid GitHub repository URL"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "GitHub repository not found"
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "GitHub API unavailable"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/import")
    public RepositoryResponse importRepository(
            @Valid @RequestBody ImportRepositoryRequest request
    ) {

        GitHubRepositoryCoordinates coordinates =
                gitHubRepositoryUrlParser.parse(request.url());

        GitRepository repository =
                repositoryService.importRepository(coordinates);

        return RepositoryResponseMapper.toResponse(repository);
    }
}