package com.repolens.analysis.web;

import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.analysis.service.RepositoryAnalysisService;
import com.repolens.analysis.web.dto.AnalyzeRepositoryRequest;
import com.repolens.analysis.web.dto.RepositoryAnalysisResponse;
import com.repolens.analysis.web.mapper.RepositoryAnalysisResponseMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {

    private final RepositoryAnalysisService repositoryAnalysisService;
    private final RepositoryAnalysisResponseMapper mapper;

    public AnalysisController(
            RepositoryAnalysisService repositoryAnalysisService,
            RepositoryAnalysisResponseMapper mapper
    ) {
        this.repositoryAnalysisService = repositoryAnalysisService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<RepositoryAnalysisResponse> analyze(
            @Valid @RequestBody AnalyzeRepositoryRequest request
    ) {

        RepositoryAnalysis analysis =
                repositoryAnalysisService.analyze(
                        request.repositoryUrl()
                );

        return ResponseEntity.ok(
                mapper.toResponse(analysis)
        );
    }
}