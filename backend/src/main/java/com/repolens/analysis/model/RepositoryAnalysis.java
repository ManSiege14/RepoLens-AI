package com.repolens.analysis.model;

import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.Framework;
import com.repolens.analysis.detector.Infrastructure;
import com.repolens.analysis.detector.ProgrammingLanguage;

import java.util.Set;

public record RepositoryAnalysis(

        Set<BuildTool> buildTools,

        Set<Framework> frameworks,

        Set<Infrastructure> infrastructure,

        Set<ProgrammingLanguage> languages

) {
}