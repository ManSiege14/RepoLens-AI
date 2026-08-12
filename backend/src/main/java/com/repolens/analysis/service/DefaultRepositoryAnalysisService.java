package com.repolens.analysis.service;

import com.repolens.analysis.RepositoryAnalysisOrchestrator;
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
import com.repolens.analysis.snapshot.RepositorySnapshot;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import com.repolens.analysis.snapshot.RepositorySnapshotBuilder;
import com.repolens.repository.domain.GitRepository;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.Instant;

@Service
@Transactional
public class DefaultRepositoryAnalysisService
        implements RepositoryAnalysisService {

    private final RepositoryCloner repositoryCloner;
    private final FileScanner fileScanner;
    private final BuildToolDetector buildToolDetector;
    private final FrameworkDetector frameworkDetector;
    private final InfrastructureDetector infrastructureDetector;
    private final LanguageDetector languageDetector;
    private final GitRepositoryRepository gitRepositoryRepository;
    private final RepositoryAnalysisRepository repositoryAnalysisRepository;

    private final RepositorySnapshotBuilder repositorySnapshotBuilder;
    private final RepositoryAnalysisOrchestrator repositoryAnalysisOrchestrator;

    public DefaultRepositoryAnalysisService(
            RepositoryCloner repositoryCloner,
            FileScanner fileScanner,
            BuildToolDetector buildToolDetector,
            FrameworkDetector frameworkDetector,
            InfrastructureDetector infrastructureDetector,
            LanguageDetector languageDetector,
            GitRepositoryRepository gitRepositoryRepository,
            RepositoryAnalysisRepository repositoryAnalysisRepository,
            RepositorySnapshotBuilder repositorySnapshotBuilder,
            RepositoryAnalysisOrchestrator repositoryAnalysisOrchestrator
    ) {
        this.repositoryCloner = repositoryCloner;
        this.fileScanner = fileScanner;
        this.buildToolDetector = buildToolDetector;
        this.frameworkDetector = frameworkDetector;
        this.infrastructureDetector = infrastructureDetector;
        this.languageDetector = languageDetector;
        this.gitRepositoryRepository = gitRepositoryRepository;
        this.repositoryAnalysisRepository = repositoryAnalysisRepository;
        this.repositorySnapshotBuilder = repositorySnapshotBuilder;
        this.repositoryAnalysisOrchestrator = repositoryAnalysisOrchestrator;
    }

    @Override
    public RepositoryAnalysis analyze(String repositoryUrl) {

        Path repositoryPath =
                repositoryCloner.cloneRepository(repositoryUrl);

        /*
         * Existing analysis pipeline.
         *
         * Keep this for backward compatibility while the newer
         * snapshot-analysis pipeline is being integrated.
         */
        ScannedRepository scannedRepository =
                fileScanner.scan(repositoryPath);

        RepositoryAnalysis analysis =
                new RepositoryAnalysis(
                        buildToolDetector.detect(scannedRepository),
                        frameworkDetector.detect(scannedRepository),
                        infrastructureDetector.detect(scannedRepository),
                        languageDetector.detect(scannedRepository)
                );

        /*
         * New RepoLens snapshot-analysis pipeline.
         *
         * This produces:
         *
         * RepositorySnapshotAnalysis
         * ├── documentation signals
         * ├── Docker / CI signals
         * ├── build tools
         * ├── languages
         * ├── structure
         * ├── architecture
         * └── health
         *
         * Persistence will be connected in the next step.
         */
        RepositorySnapshot snapshot =
                repositorySnapshotBuilder.build(repositoryPath);

        RepositorySnapshotAnalysis snapshotAnalysis =
                repositoryAnalysisOrchestrator.analyze(snapshot);

        /*
         * Prevent the result from being silently optimized away and
         * make it explicit that this is intentionally transient for
         * this integration step.
         */
        if (snapshotAnalysis == null) {
            throw new IllegalStateException(
                    "Repository snapshot analysis must not be null"
            );
        }

        /*
         * Existing persistence pipeline.
         */
        GitRepository repository =
                gitRepositoryRepository
                        .findByHtmlUrl(repositoryUrl)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Repository must be imported before analysis."
                                )
                        );

        RepositoryAnalysisEntity analysisEntity =
                repositoryAnalysisRepository
                        .findByRepository(repository)
                        .orElseGet(() -> {
                            RepositoryAnalysisEntity entity =
                                    new RepositoryAnalysisEntity();

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