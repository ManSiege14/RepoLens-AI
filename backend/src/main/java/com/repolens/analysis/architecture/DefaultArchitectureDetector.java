package com.repolens.analysis.architecture;

import com.repolens.analysis.snapshot.RepositoryFile;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Component
public class DefaultArchitectureDetector
        implements ArchitectureDetector {

    @Override
    public ArchitectureAnalysis detect(
            RepositorySnapshot snapshot
    ) {

        Set<ArchitectureType> detected =
                EnumSet.noneOf(ArchitectureType.class);

        List<ArchitectureEvidence> evidence =
                new ArrayList<>();

        boolean controllerPresent =
                containsDirectoryOrFile(
                        snapshot,
                        "controller"
                );

        boolean servicePresent =
                containsDirectoryOrFile(
                        snapshot,
                        "service"
                );

        boolean repositoryPresent =
                containsDirectoryOrFile(
                        snapshot,
                        "repository"
                );

        boolean restIndicator =
                containsRestIndicator(snapshot);

        boolean frontendIndicator =
                containsFrontendIndicator(snapshot);

        if (controllerPresent
                && servicePresent
                && repositoryPresent) {

            detected.add(ArchitectureType.LAYERED);

            evidence.add(
                    new ArchitectureEvidence(
                            "layered-packages",
                            "Controller, service, and repository layers were detected."
                    )
            );
        }

        if (controllerPresent && restIndicator) {

            detected.add(ArchitectureType.REST_API);

            evidence.add(
                    new ArchitectureEvidence(
                            "rest-indicators",
                            "Controller classes and REST endpoint indicators were detected."
                    )
            );
        }

        if (frontendIndicator) {

            detected.add(ArchitectureType.FRONTEND);

            evidence.add(
                    new ArchitectureEvidence(
                            "frontend-indicators",
                            "Frontend project indicators such as package.json, component directories, or frontend build configuration were detected."
                    )
            );
        }

        ArchitectureType primary =
                detected.contains(ArchitectureType.LAYERED)
                        ? ArchitectureType.LAYERED
                        : detected.contains(ArchitectureType.REST_API)
                        ? ArchitectureType.REST_API
                        : detected.contains(ArchitectureType.FRONTEND)
                        ? ArchitectureType.FRONTEND
                        : ArchitectureType.UNKNOWN;

        if (detected.isEmpty()) {

            evidence.add(
                    new ArchitectureEvidence(
                            "no-known-pattern",
                            "No recognised architectural pattern was detected from the repository snapshot."
                    )
            );
        }

        return new ArchitectureAnalysis(
                primary,
                List.copyOf(detected),
                List.copyOf(evidence)
        );
    }

    private boolean containsDirectoryOrFile(
            RepositorySnapshot snapshot,
            String keyword
    ) {

        String normalizedKeyword =
                keyword.toLowerCase();

        return snapshot.directories()
                .stream()
                .anyMatch(directory ->
                        directory.name()
                                .toLowerCase()
                                .equals(normalizedKeyword)
                                || directory.relativePath()
                                .toLowerCase()
                                .contains(
                                        "/" + normalizedKeyword + "/"
                                )
                );
    }

    private boolean containsRestIndicator(
            RepositorySnapshot snapshot
    ) {

        return snapshot.files()
                .stream()
                .map(RepositoryFile::name)
                .anyMatch(name ->
                        name.endsWith("Controller.java")
                                || name.endsWith("Controller.kt")
                                || name.equalsIgnoreCase("routes.js")
                                || name.equalsIgnoreCase("routes.ts")
                );
    }

    private boolean containsFrontendIndicator(
            RepositorySnapshot snapshot
    ) {

        boolean packageJsonPresent =
                snapshot.files()
                        .stream()
                        .anyMatch(file ->
                                file.name()
                                        .equalsIgnoreCase("package.json")
                        );

        boolean frontendDirectoryPresent =
                snapshot.directories()
                        .stream()
                        .anyMatch(directory -> {

                            String name =
                                    directory.name().toLowerCase();

                            return name.equals("components")
                                    || name.equals("pages");
                        });

        boolean frontendConfigPresent =
                snapshot.files()
                        .stream()
                        .anyMatch(file -> {

                            String name =
                                    file.name().toLowerCase();

                            return name.equals("vite.config.js")
                                    || name.equals("vite.config.ts")
                                    || name.equals("next.config.js")
                                    || name.equals("next.config.mjs")
                                    || name.equals("next.config.ts");
                        });

        return packageJsonPresent
                || frontendDirectoryPresent
                || frontendConfigPresent;
    }
}