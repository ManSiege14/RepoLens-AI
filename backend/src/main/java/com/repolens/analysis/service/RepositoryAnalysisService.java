package com.repolens.analysis.service;

import com.repolens.analysis.model.RepositoryAnalysis;

public interface RepositoryAnalysisService {

    RepositoryAnalysis analyze(String repositoryUrl);

}