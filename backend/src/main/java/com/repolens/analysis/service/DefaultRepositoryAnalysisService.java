package com.repolens.analysis.service;

import com.repolens.analysis.clone.RepositoryCloner;
import com.repolens.analysis.detector.BuildToolDetector;
import com.repolens.analysis.detector.FrameworkDetector;
import com.repolens.analysis.detector.InfrastructureDetector;
import com.repolens.analysis.detector.LanguageDetector;
import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.persistence.RepositoryAnalysisRepository;
import com.repolens.analysis.scanner.FileScanner;
import com.repolens.analysis.scanner.ScannedRepository;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.Instant;

@Service
@Transactional
public class DefaultRepositoryAnalysisService implements RepositoryAnalysisService {

    private final RepositoryCloner repositoryCloner;
    private final FileScanner fileScanner;
    private final BuildToolDetector buildToolDetector;
    private final FrameworkDetector frameworkDetector;
    private final InfrastructureDetector infrastructureDetector;
    private final LanguageDetector languageDetector;
    private final GitRepositoryRepository gitRepositoryRepository;
    private final RepositoryAnalysisRepository repositoryAnalysisRepository;

    public DefaultRepositoryAnalysisService(
            RepositoryCloner repositoryCloner,
            FileScanner fileScanner,
            BuildToolDetector buildToolDetector,
            FrameworkDetector frameworkDetector,
            InfrastructureDetector infrastructureDetector,
            LanguageDetector languageDetector,
            GitRepositoryRepository gitRepositoryRepository,
            RepositoryAnalysisRepository repositoryAnalysisRepository
    ) {
        this.repositoryCloner = repositoryCloner;
        this.fileScanner = fileScanner;
        this.buildToolDetector = buildToolDetector;
        this.frameworkDetector = frameworkDetector;
        this.infrastructureDetector = infrastructureDetector;
        this.languageDetector = languageDetector;
        this.gitRepositoryRepository = gitRepositoryRepository;
        this.repositoryAnalysisRepository = repositoryAnalysisRepository;
    }

    @Override
    public RepositoryAnalysis analyze(String repositoryUrl) {

        Path repositoryPath = repositoryCloner.cloneRepository(repositoryUrl);

        ScannedRepository scannedRepository = fileScanner.scan(repositoryPath);

        RepositoryAnalysis analysis = new RepositoryAnalysis(
                buildToolDetector.detect(scannedRepository),
                frameworkDetector.detect(scannedRepository),
                infrastructureDetector.detect(scannedRepository),
                languageDetector.detect(scannedRepository)
        );

        GitRepository repository = gitRepositoryRepository
                .findByHtmlUrl(repositoryUrl)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Repository must be imported before analysis."
                ));

        RepositoryAnalysisEntity analysisEntity =
                repositoryAnalysisRepository
                        .findByRepository(repository)
                        .orElseGet(() -> {
                            RepositoryAnalysisEntity entity = new RepositoryAnalysisEntity();
                            entity.setRepository(repository);
                            return entity;
                        });

        analysisEntity.setAnalyzedAt(Instant.now());
        analysisEntity.setBuildTools(analysis.buildTools());
        analysisEntity.setFrameworks(analysis.frameworks());
        analysisEntity.setInfrastructure(analysis.infrastructure());
        analysisEntity.setLanguages(analysis.languages());

        repositoryAnalysisRepository.save(analysisEntity);

        return analysis;
    }
}