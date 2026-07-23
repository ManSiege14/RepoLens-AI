package com.repolens.analysis.service;

import com.repolens.analysis.clone.RepositoryCloner;
import com.repolens.analysis.detector.BuildToolDetector;
import com.repolens.analysis.detector.FrameworkDetector;
import com.repolens.analysis.detector.InfrastructureDetector;
import com.repolens.analysis.detector.LanguageDetector;
import com.repolens.analysis.model.RepositoryAnalysis;
import com.repolens.analysis.scanner.FileScanner;
import com.repolens.analysis.scanner.ScannedRepository;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
public class DefaultRepositoryAnalysisService implements RepositoryAnalysisService {

    private final RepositoryCloner repositoryCloner;
    private final FileScanner fileScanner;
    private final BuildToolDetector buildToolDetector;
    private final FrameworkDetector frameworkDetector;
    private final InfrastructureDetector infrastructureDetector;
    private final LanguageDetector languageDetector;

    public DefaultRepositoryAnalysisService(
            RepositoryCloner repositoryCloner,
            FileScanner fileScanner,
            BuildToolDetector buildToolDetector,
            FrameworkDetector frameworkDetector,
            InfrastructureDetector infrastructureDetector,
            LanguageDetector languageDetector
    ) {
        this.repositoryCloner = repositoryCloner;
        this.fileScanner = fileScanner;
        this.buildToolDetector = buildToolDetector;
        this.frameworkDetector = frameworkDetector;
        this.infrastructureDetector = infrastructureDetector;
        this.languageDetector = languageDetector;
    }
    @Override
    public RepositoryAnalysis analyze(String repositoryUrl) {

        Path repositoryPath = repositoryCloner.cloneRepository(repositoryUrl);

        ScannedRepository scannedRepository =
                fileScanner.scan(repositoryPath);

        return new RepositoryAnalysis(
                buildToolDetector.detect(scannedRepository),
                frameworkDetector.detect(scannedRepository),
                infrastructureDetector.detect(scannedRepository),
                languageDetector.detect(scannedRepository)
        );
    }
}