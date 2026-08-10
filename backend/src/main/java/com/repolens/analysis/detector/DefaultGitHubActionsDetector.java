package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

@Component
public class DefaultGitHubActionsDetector
        implements GitHubActionsDetector {

    @Override
    public Boolean detect(RepositorySnapshot snapshot) {

        return snapshot.files()
                .stream()
                .anyMatch(file ->
                        file.relativePath().startsWith(".github/workflows/")
                );
    }
}