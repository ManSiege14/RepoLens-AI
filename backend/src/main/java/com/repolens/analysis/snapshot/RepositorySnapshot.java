package com.repolens.analysis.snapshot;

import java.nio.file.Path;
import java.util.List;

public record RepositorySnapshot(

        Path root,

        List<RepositoryDirectory> directories,

        List<RepositoryFile> files

) {
}