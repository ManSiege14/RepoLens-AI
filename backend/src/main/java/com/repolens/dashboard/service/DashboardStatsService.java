package com.repolens.dashboard.service;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.persistence.RepositoryAnalysisEntity;
import com.repolens.analysis.persistence.RepositoryAnalysisRepository;
import com.repolens.dashboard.web.dto.DashboardStatsResponse;
import com.repolens.repository.persistence.GitRepositoryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardStatsService {

    private final GitRepositoryRepository repositoryRepository;
    private final RepositoryAnalysisRepository analysisRepository;

    public DashboardStatsService(
            GitRepositoryRepository repositoryRepository,
            RepositoryAnalysisRepository analysisRepository
    ) {
        this.repositoryRepository = repositoryRepository;
        this.analysisRepository = analysisRepository;
    }

    public DashboardStatsResponse getDashboardStats() {

        List<RepositoryAnalysisEntity> analyses = analysisRepository.findAll();

        int languagesDetected = analyses.stream()
                .flatMap(a -> a.getLanguages().stream())
                .collect(java.util.stream.Collectors.toSet())
                .size();

        int frameworksDetected = analyses.stream()
                .flatMap(a -> a.getFrameworks().stream())
                .collect(java.util.stream.Collectors.toSet())
                .size();

        int buildToolsDetected = analyses.stream()
                .flatMap(a -> a.getBuildTools().stream())
                .collect(java.util.stream.Collectors.toSet())
                .size();

        int infrastructureDetected = analyses.stream()
                .flatMap(a -> a.getInfrastructure().stream())
                .collect(java.util.stream.Collectors.toSet())
                .size();

        String mostUsedLanguage = findMostUsedLanguage(analyses);

        return new DashboardStatsResponse(
                repositoryRepository.count(),
                analysisRepository.count(),
                languagesDetected,
                frameworksDetected,
                buildToolsDetected,
                infrastructureDetected,
                mostUsedLanguage
        );
    }

    private String findMostUsedLanguage(
            List<RepositoryAnalysisEntity> analyses
    ) {

        Map<ProgrammingLanguage, Integer> counts =
                new EnumMap<>(ProgrammingLanguage.class);

        for (RepositoryAnalysisEntity analysis : analyses) {
            for (ProgrammingLanguage language : analysis.getLanguages()) {
                counts.merge(language, 1, Integer::sum);
            }
        }

        return counts.entrySet()
                .stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(entry -> entry.getKey().name())
                .orElse("None");
    }
}