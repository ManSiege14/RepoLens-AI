package com.repolens.repository.web;

import com.repolens.github.model.GitHubRepositoryCoordinates;
import com.repolens.github.validation.GitHubRepositoryUrlParser;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.service.RepositoryDetailsService;
import com.repolens.repository.service.RepositoryListService;
import com.repolens.repository.service.RepositoryService;
import com.repolens.repository.web.dto.ImportRepositoryRequest;
import com.repolens.repository.web.dto.RepositoryDetailsResponse;
import com.repolens.repository.web.dto.RepositoryResponse;
import com.repolens.repository.web.dto.RepositorySummaryResponse;
import com.repolens.repository.web.mapper.RepositoryResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/repositories")
@Tag(
        name = "Repositories",
        description = "Import and manage GitHub repositories"
)
public class RepositoryController {

    private final RepositoryService repositoryService;
    private final RepositoryDetailsService repositoryDetailsService;
    private final RepositoryListService repositoryListService;
    private final GitHubRepositoryUrlParser gitHubRepositoryUrlParser;

    public RepositoryController(
            RepositoryService repositoryService,
            RepositoryDetailsService repositoryDetailsService,
            RepositoryListService repositoryListService,
            GitHubRepositoryUrlParser gitHubRepositoryUrlParser
    ) {
        this.repositoryService = repositoryService;
        this.repositoryDetailsService = repositoryDetailsService;
        this.repositoryListService = repositoryListService;
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

    @Operation(
            summary = "Get all repositories",
            description = "Returns all imported repositories"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Repositories retrieved successfully"
            )
    })
    @GetMapping
    public List<RepositorySummaryResponse> getRepositories() {
        return repositoryListService.getRepositories();
    }

    @Operation(
            summary = "Get repository details",
            description = "Returns an imported repository and its latest analysis"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Repository retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Repository not found"
            )
    })
    @GetMapping("/{id}")
    public RepositoryDetailsResponse getRepository(
            @PathVariable UUID id
    ) {
        return repositoryDetailsService.getRepository(id);
    }
}