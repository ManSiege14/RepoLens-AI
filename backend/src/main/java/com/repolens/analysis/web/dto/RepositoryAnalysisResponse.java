package com.repolens.analysis.web.dto;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;
import java.util.Set;
public record RepositoryAnalysisResponse(
        Set<BuildTool> buildTools,
        Set<Framework> frameworks,
        Set<Infrastructure> infrastructure,
        Set<ProgrammingLanguage> languages )
{ }