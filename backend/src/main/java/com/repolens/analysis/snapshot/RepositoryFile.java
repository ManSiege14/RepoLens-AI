package com.repolens.analysis.snapshot;

public record RepositoryFile(

        String name,

        String relativePath,

        long size,

        String extension

) {
}