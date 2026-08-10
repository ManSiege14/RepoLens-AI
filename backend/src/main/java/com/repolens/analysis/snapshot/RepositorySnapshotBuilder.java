package com.repolens.analysis.snapshot;

import java.nio.file.Path;

public interface RepositorySnapshotBuilder {

    RepositorySnapshot build(Path repositoryRoot);

}