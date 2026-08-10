package com.repolens.analysis.snapshot;

public record RepositoryStructure(

        int totalFiles,

        int totalDirectories,

        int sourceFiles,

        int documentationFiles,

        int configurationFiles

) {
}