package com.repolens.analysis.architecture;

import com.repolens.analysis.snapshot.RepositorySnapshot;

public interface ArchitectureDetector {

    ArchitectureAnalysis detect(
            RepositorySnapshot snapshot
    );
}