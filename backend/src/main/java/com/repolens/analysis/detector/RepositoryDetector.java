package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;

public interface RepositoryDetector<T> {

    T detect(RepositorySnapshot snapshot);
}