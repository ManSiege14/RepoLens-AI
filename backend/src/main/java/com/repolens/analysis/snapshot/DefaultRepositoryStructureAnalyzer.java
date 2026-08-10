package com.repolens.analysis.snapshot;

import org.springframework.stereotype.Component;

@Component
public class DefaultRepositoryStructureAnalyzer
        implements RepositoryStructureAnalyzer {

    @Override
    public RepositoryStructure analyze(
            RepositorySnapshot snapshot
    ) {

        int sourceFiles = 0;
        int documentationFiles = 0;
        int configurationFiles = 0;

        for (RepositoryFile file : snapshot.files()) {

            String extension = file.extension();

            if (isSourceFile(extension)) {
                sourceFiles++;
            }

            if (isDocumentationFile(file)) {
                documentationFiles++;
            }

            if (isConfigurationFile(file)) {
                configurationFiles++;
            }
        }

        return new RepositoryStructure(
                snapshot.files().size(),
                snapshot.directories().size(),
                sourceFiles,
                documentationFiles,
                configurationFiles
        );
    }

    private boolean isSourceFile(String extension) {

        return switch (extension.toLowerCase()) {
            case "java",
                 "kt",
                 "js",
                 "ts",
                 "tsx",
                 "py",
                 "go",
                 "rs",
                 "cs",
                 "cpp",
                 "c",
                 "h",
                 "hpp" -> true;

            default -> false;
        };
    }

    private boolean isDocumentationFile(
            RepositoryFile file
    ) {

        String name = file.name().toLowerCase();

        return name.equals("readme.md")
                || name.equals("readme.txt")
                || name.equals("readme")
                || name.equals("changelog.md")
                || name.equals("contributing.md");
    }

    private boolean isConfigurationFile(
            RepositoryFile file
    ) {

        String name = file.name().toLowerCase();

        return name.equals("pom.xml")
                || name.equals("build.gradle")
                || name.equals("build.gradle.kts")
                || name.equals("package.json")
                || name.equals("dockerfile")
                || name.equals("docker-compose.yml")
                || name.equals("docker-compose.yaml")
                || name.equals("application.yml")
                || name.equals("application.yaml")
                || name.equals("application.properties");
    }
}